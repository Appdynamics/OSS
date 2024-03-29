package org.checkerframework.framework.util.defaults;

import org.checkerframework.framework.qual.AnnotatedFor;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.DefaultQualifiers;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedDeclaredType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedExecutableType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedIntersectionType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedNoType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedTypeVariable;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedUnionType;
import org.checkerframework.framework.type.AnnotatedTypeMirror.AnnotatedWildcardType;
import org.checkerframework.framework.type.GenericAnnotatedTypeFactory;
import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.type.visitor.AnnotatedTypeScanner;
import org.checkerframework.framework.util.CheckerMain;
import org.checkerframework.framework.util.PluginUtil;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.CollectionUtils;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.ErrorReporter;
import org.checkerframework.javacutil.InternalUtils;
import org.checkerframework.javacutil.TreeUtils;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Type.WildcardType;

/**
 * Determines the default qualifiers on a type.
 * Default qualifiers are specified via the {@link org.checkerframework.framework.qual.DefaultQualifier} annotation.
 *
 * @see org.checkerframework.framework.qual.DefaultQualifier
 */
public class QualifierDefaults {

    // TODO add visitor state to get the default annotations from the top down?
    // TODO apply from package elements also
    // TODO try to remove some dependencies (e.g. on factory)

    /**
     * This field indicates whether or not a default should be applied to type vars
     * located in the type being default.  This should only ever be true when the type variable
     * is a local variable, non-component use, i.e.
     * <pre>{@code
     * <T> void method(@NOT_HERE T tIn) {
     *     T t = tIn;
     * }
     * }</pre>
     *
     * The local variable T will be defaulted in order to allow dataflow to refine T.
     * This variable will be false if dataflow is not in use.
     */
    private boolean applyToTypeVar = false;
    private final Elements elements;
    private final AnnotatedTypeFactory atypeFactory;
    private final List<String> upstreamCheckerNames;

    private final DefaultSet checkedCodeDefaults = new DefaultSet();
    private final DefaultSet uncheckedCodeDefaults = new DefaultSet();

    /** Mapping from an Element to the source Tree of the declaration. */
    private static final int CACHE_SIZE = 300;
    protected static final Map<Element, BoundType> elementToBoundType  = CollectionUtils.createLRUCache(CACHE_SIZE);


    /**
     * Defaults that apply for a certain Element.
     * On the one hand this is used for caching (an earlier name for the field was
     * "qualifierCache"). It can also be used by type systems to set defaults for
     * certain Elements.
     */
    private final Map<Element, DefaultSet> elementDefaults = new IdentityHashMap<>();

    /**
     * A mapping of Element &rarr; Whether or not that element is AnnotatedFor this type system.
     */
    private final Map<Element, Boolean> elementAnnotatedFors = new IdentityHashMap<>();

    /**
     * CLIMB locations whose standard default is top for a given type system.
     */
    public static final TypeUseLocation[] standardClimbDefaultsTop = { TypeUseLocation.LOCAL_VARIABLE,
            TypeUseLocation.RESOURCE_VARIABLE, TypeUseLocation.EXCEPTION_PARAMETER,
            TypeUseLocation.IMPLICIT_UPPER_BOUND };

    /**
     * CLIMB locations whose standard default is bottom for a given type system.
     */
    public static final TypeUseLocation[] standardClimbDefaultsBottom = { TypeUseLocation.IMPLICIT_LOWER_BOUND };

    /**
     * List of TypeUseLocations that are valid for unchecked code defaults.
     */
    private static final TypeUseLocation[] validUncheckedCodeDefaultLocations = {
        TypeUseLocation.FIELD,
        TypeUseLocation.PARAMETER,
        TypeUseLocation.RETURN,
        TypeUseLocation.RECEIVER,
        TypeUseLocation.UPPER_BOUND,
        TypeUseLocation.LOWER_BOUND,
        TypeUseLocation.OTHERWISE,
        TypeUseLocation.ALL
    };

    /**
     * Standard unchecked default locations that should be top
     */
    // Fields are defaulted to top so that warnings are issued at field reads, which we believe are more common
    // than field writes. Future work is to specify different defaults for field reads and field writes.
    // (When a field is written to, its type should be bottom.)
    public static final TypeUseLocation[] standardUncheckedDefaultsTop = { TypeUseLocation.RETURN,
            TypeUseLocation.FIELD,
            TypeUseLocation.UPPER_BOUND };

    /**
     * Standard unchecked default locations that should be bottom
     */
    public static final TypeUseLocation[] standardUncheckedDefaultsBottom = { TypeUseLocation.PARAMETER,
            TypeUseLocation.LOWER_BOUND };

    private final boolean useUncheckedCodeDefaultsSource;
    private final boolean useUncheckedCodeDefaultsBytecode;

    /**
     * Returns an array of locations that are valid for the unchecked value
     * defaults.  These are simply by syntax, since an entire file is typechecked,
     * it is not possible for local variables to be unchecked.
     */
    public static TypeUseLocation[] validLocationsForUncheckedCodeDefaults() {
        return validUncheckedCodeDefaultLocations;
    }

    /**
     * @param elements interface to Element data in the current processing environment
     * @param atypeFactory an annotation factory, used to get annotations by name
     */
    public QualifierDefaults(Elements elements, AnnotatedTypeFactory atypeFactory) {
        this.elements = elements;
        this.atypeFactory = atypeFactory;
        this.upstreamCheckerNames = atypeFactory.getContext().getChecker().getUpstreamCheckerNames();
        this.useUncheckedCodeDefaultsBytecode = atypeFactory.getContext().getChecker().useUncheckedCodeDefault("bytecode");
        this.useUncheckedCodeDefaultsSource = atypeFactory.getContext().getChecker().useUncheckedCodeDefault("source");
    }

    @Override
    public String toString() {
        // displays the checked and unchecked code defaults
        StringBuilder sb = new StringBuilder();
        sb.append("Checked code defaults: ");
        sb.append(System.lineSeparator());
        sb.append(PluginUtil.join(System.lineSeparator(), checkedCodeDefaults));
        sb.append(System.lineSeparator());
        sb.append("Unchecked code defaults: ");
        sb.append(System.lineSeparator());
        sb.append(PluginUtil.join(System.lineSeparator(), uncheckedCodeDefaults));
        sb.append(System.lineSeparator());
        sb.append("useUncheckedCodeDefaultsSource: ");
        sb.append(useUncheckedCodeDefaultsSource);
        sb.append(System.lineSeparator());
        sb.append("useUncheckedCodeDefaultsBytecode: ");
        sb.append(useUncheckedCodeDefaultsBytecode);
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    /**
     * Add standard unchecked defaults that do not conflict with previously added defaults.
     * @param tops AnnotationMirrors that are top
     * @param bottoms AnnotationMirrors that are bottom
     */
    public void addUncheckedStandardDefaults(Iterable<? extends AnnotationMirror> tops,
            Iterable<? extends AnnotationMirror> bottoms) {
        for (TypeUseLocation loc : standardUncheckedDefaultsTop) {
            // Only add standard defaults in locations where a default has not be specified
            for (AnnotationMirror top : tops) {
                if (!conflictsWithExistingDefaults(uncheckedCodeDefaults, top, loc)) {
                    addUncheckedCodeDefault(top, loc);
                }
            }
        }

        for (TypeUseLocation loc : standardUncheckedDefaultsBottom) {
            for (AnnotationMirror bottom : bottoms) {
                // Only add standard defaults in locations where a default has not be specified
                if (!conflictsWithExistingDefaults(uncheckedCodeDefaults, bottom, loc)) {
                    addUncheckedCodeDefault(bottom, loc);
                }
            }
        }
    }

    /**
     * Add standard CLIMB defaults that do not conflict with previously added defaults.
     *
     * @param tops    AnnotationMirrors that are top
     * @param bottoms AnnotationMirrors that are bottom
     */
    public void addClimbStandardDefaults(Iterable<? extends AnnotationMirror> tops,
            Iterable<? extends AnnotationMirror> bottoms) {
        for (TypeUseLocation loc : standardClimbDefaultsTop) {
            for (AnnotationMirror top : tops) {
                if (!conflictsWithExistingDefaults(checkedCodeDefaults, top, loc)) {
                    // Only add standard defaults in locations where a default has not been specified
                    addCheckedCodeDefault(top, loc);
                }
            }
        }

        for (TypeUseLocation loc : standardClimbDefaultsBottom) {
            for (AnnotationMirror bottom : bottoms) {
                if (!conflictsWithExistingDefaults(checkedCodeDefaults, bottom, loc)) {
                    // Only add standard defaults in locations where a default has not been specified
                    addCheckedCodeDefault(bottom, loc);
                }
            }
        }
    }

    /**
     * Sets the default annotations.  A programmer may override this by
     * writing the @DefaultQualifier annotation on an element.
     */
    public void addCheckedCodeDefault(AnnotationMirror absoluteDefaultAnno, TypeUseLocation location) {
        checkDuplicates(checkedCodeDefaults, absoluteDefaultAnno, location);
        checkedCodeDefaults.add(new Default(absoluteDefaultAnno, location));
    }

    /**
     * Sets the default annotation for unchecked elements.
     */
    public void addUncheckedCodeDefault(AnnotationMirror uncheckedDefaultAnno, TypeUseLocation location) {
        checkDuplicates(uncheckedCodeDefaults, uncheckedDefaultAnno, location);
        checkIsValidUncheckedCodeLocation(uncheckedDefaultAnno, location);

        uncheckedCodeDefaults.add(new Default(uncheckedDefaultAnno, location));
    }

    /**
     * Sets the default annotation for unchecked elements, with specific locations.
     */
    public void addUncheckedCodeDefaults(AnnotationMirror absoluteDefaultAnno, TypeUseLocation[] locations) {
        for (TypeUseLocation location : locations) {
            addUncheckedCodeDefault(absoluteDefaultAnno, location);
        }
    }

    public void addCheckedCodeDefaults(AnnotationMirror absoluteDefaultAnno, TypeUseLocation[] locations) {
        for (TypeUseLocation location : locations) {
            addCheckedCodeDefault(absoluteDefaultAnno, location);
        }
    }

    /**
     * Sets the default annotations for a certain Element.
     */
    public void addElementDefault(Element elem, AnnotationMirror elementDefaultAnno, TypeUseLocation location) {
        DefaultSet prevset = elementDefaults.get(elem);
        if (prevset != null) {
            checkDuplicates(prevset, elementDefaultAnno, location);
        } else {
            prevset = new DefaultSet();
        }
        prevset.add(new Default(elementDefaultAnno, location));
        elementDefaults.put(elem, prevset);
    }

    private void checkIsValidUncheckedCodeLocation(AnnotationMirror uncheckedDefaultAnno, TypeUseLocation location) {
        boolean isValidUntypeLocation = false;
        for (TypeUseLocation validLoc : validLocationsForUncheckedCodeDefaults()) {
            if (location == validLoc) {
                isValidUntypeLocation = true;
                break;
            }
        }

        if (!isValidUntypeLocation) {
            ErrorReporter.errorAbort(
                    "Invalid unchecked code default location: " + location + " -> " + uncheckedDefaultAnno );
        }

    }

    private void checkDuplicates(DefaultSet previousDefaults, AnnotationMirror newAnno, TypeUseLocation newLoc ) {
        if (conflictsWithExistingDefaults(previousDefaults, newAnno, newLoc)) {
            ErrorReporter.errorAbort("Only one qualifier from a hierarchy can be the default! Existing: "
                                             + previousDefaults + " and new: "
                                             + (new Default(newAnno, newLoc)));
        }
    }

    private boolean conflictsWithExistingDefaults(DefaultSet previousDefaults, AnnotationMirror newAnno,
            TypeUseLocation newLoc ) {
        final QualifierHierarchy qualHierarchy = atypeFactory.getQualifierHierarchy();

        for (Default previous : previousDefaults ) {
            if (!newAnno.equals(previous.anno) && previous.location == newLoc) {
                final AnnotationMirror previousTop = qualHierarchy.getTopAnnotation(previous.anno);
                if (qualHierarchy.isSubtype(newAnno, previousTop)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Applies default annotations to a type given an {@link javax.lang.model.element.Element}.
     *
     * @param elt the element from which the type was obtained
     * @param type the type to annotate
     */
    public void annotate(Element elt, AnnotatedTypeMirror type) {
        applyDefaultsElement(elt, type);
    }

    /**
     * Applies default annotations to a type given a {@link com.sun.source.tree.Tree}.
     *
     * @param tree the tree from which the type was obtained
     * @param type the type to annotate
     */
    public void annotate(Tree tree, AnnotatedTypeMirror type) {
        applyDefaults(tree, type);
    }

    /**
     * Determines the nearest enclosing element for a tree by climbing the tree
     * toward the root and obtaining the element for the first declaration
     * (variable, method, or class) that encloses the tree.
     * Initializers of local variables are handled in a special way:
     * within an initializer we look for the DefaultQualifier(s) annotation and
     * keep track of the previously visited tree.
     * TODO: explain the behavior better.
     *
     * @param tree the tree
     * @return the nearest enclosing element for a tree
     */
    private Element nearestEnclosingExceptLocal(Tree tree) {
        TreePath path = atypeFactory.getPath(tree);
        if (path == null) {
            Element method = atypeFactory.getEnclosingMethod(tree);
            if (method != null) {
                return method;
            } else {
                return InternalUtils.symbol(tree);
            }
        }

        Tree prev = null;

        for (Tree t : path) {
            switch (t.getKind()) {
            case VARIABLE:
                VariableTree vtree = (VariableTree)t;
                ExpressionTree vtreeInit = vtree.getInitializer();
                if (vtreeInit != null && prev == vtreeInit) {
                    Element elt = TreeUtils.elementFromDeclaration((VariableTree)t);
                    DefaultQualifier d = elt.getAnnotation(DefaultQualifier.class);
                    DefaultQualifiers ds = elt.getAnnotation(DefaultQualifiers.class);

                    if (d == null && ds == null) {
                        break;
                    }
                }
                if (prev!=null && prev.getKind() == Tree.Kind.MODIFIERS) {
                    // Annotations are modifiers. We do not want to apply the local variable default to
                    // annotations. Without this, test fenum/TestSwitch failed, because the default for
                    // an argument became incompatible with the declared type.
                    break;
                }
                return TreeUtils.elementFromDeclaration((VariableTree)t);
            case METHOD:
                return TreeUtils.elementFromDeclaration((MethodTree)t);
            case CLASS:
            case ENUM:
            case INTERFACE:
            case ANNOTATION_TYPE:
                return TreeUtils.elementFromDeclaration((ClassTree)t);
            default: // Do nothing.
            }
            prev = t;
        }

        return null;
    }

    /**
     * Applies default annotations to a type.
     * A {@link com.sun.source.tree.Tree} that determines the appropriate scope for defaults.
     * <p>
     *
     * For instance, if the tree is associated with a declaration (e.g., it's
     * the use of a field, or a method invocation), defaults in the scope of the
     * <i>declaration</i> are used; if the tree is not associated with a
     * declaration (e.g., a typecast), defaults in the scope of the tree are
     * used.
     *
     * @param tree the tree associated with the type
     * @param type the type to which defaults will be applied
     *
     * @see #applyDefaultsElement(javax.lang.model.element.Element, org.checkerframework.framework.type.AnnotatedTypeMirror)
     */
    private void applyDefaults(Tree tree, AnnotatedTypeMirror type) {

        // The location to take defaults from.
        Element elt;
        switch (tree.getKind()) {
            case MEMBER_SELECT:
                elt = TreeUtils.elementFromUse((MemberSelectTree)tree);
                break;

            case IDENTIFIER:
                elt = TreeUtils.elementFromUse((IdentifierTree)tree);
                break;

            case METHOD_INVOCATION:
                elt = TreeUtils.elementFromUse((MethodInvocationTree)tree);
                break;

            // TODO cases for array access, etc. -- every expression tree
            // (The above probably means that we should use defaults in the
            // scope of the declaration of the array.  Is that right?  -MDE)

            default:
                // If no associated symbol was found, use the tree's (lexical)
                // scope.
                elt = nearestEnclosingExceptLocal(tree);
                // elt = nearestEnclosing(tree);
        }
        // System.out.println("applyDefaults on tree " + tree +
        //        " gives elt: " + elt + "(" + elt.getKind() + ")");

        if (elt != null) {
            boolean defaultTypeVarLocals = (atypeFactory instanceof GenericAnnotatedTypeFactory<?,?,?,?>)
                           && (((GenericAnnotatedTypeFactory<?,?,?,?>) atypeFactory).getShouldDefaultTypeVarLocals());

            applyToTypeVar = defaultTypeVarLocals
                          && elt.getKind() == ElementKind.LOCAL_VARIABLE
                          && type.getKind() == TypeKind.TYPEVAR;
            applyDefaultsElement(elt, type);
            applyToTypeVar = false;
        }
    }

    private DefaultSet fromDefaultQualifier(DefaultQualifier dq) {
        // TODO: I want to simply write d.value(), but that doesn't work.
        // It works in other places, e.g. see handling of @SubtypeOf.
        // The hack below should probably be added to:
        // Class<? extends Annotation> cls = AnnotationUtils.parseTypeValue(dq, "value");
        Class<? extends Annotation> cls;
        try {
            cls = dq.value();
        } catch ( MirroredTypeException mte ) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> clscast =
                    (Class<? extends Annotation>) Class.forName(mte.getTypeMirror().toString());
                cls = clscast;
            } catch (ClassNotFoundException e) {
                ErrorReporter.errorAbort("Could not load qualifier: " + e.getMessage(), e);
                cls = null;
            }
        }

        AnnotationMirror anno = AnnotationUtils.fromClass(elements, cls);

        if (anno == null) {
            return null;
        }

        if (!atypeFactory.isSupportedQualifier(anno)) {
            anno = atypeFactory.aliasedAnnotation(anno);
        }

        if (atypeFactory.isSupportedQualifier(anno)) {
            EnumSet<TypeUseLocation> locations = EnumSet.of(dq.locations()[0], dq.locations());
            DefaultSet ret = new DefaultSet();
            for (TypeUseLocation loc : locations) {
                ret.add(new Default(anno, loc));
            }
            return ret;
        } else {
            return null;
        }
    }

    private boolean isElementAnnotatedForThisChecker(final Element elt) {
        boolean elementAnnotatedForThisChecker = false;

        if (elt == null) {
            ErrorReporter.errorAbort("Call of QualifierDefaults.isElementAnnotatedForThisChecker with null");
            return false;
        }

        if (elementAnnotatedFors.containsKey(elt)) {
            return elementAnnotatedFors.get(elt);
        }

        final AnnotationMirror af = atypeFactory.getDeclAnnotation(elt, AnnotatedFor.class);

        if (af != null) {
            List<String> checkers = AnnotationUtils.getElementValueArray(af, "value", String.class, false);

            if (checkers != null) {
                for (String checker : checkers) {
                    if (CheckerMain.matchesFullyQualifiedProcessor(checker, upstreamCheckerNames, true)) {
                        elementAnnotatedForThisChecker = true;
                        break;
                    }
                }
            }
        }

        if (!elementAnnotatedForThisChecker) {
            Element parent;
            if (elt.getKind() == ElementKind.PACKAGE) {
                // elt.getEnclosingElement() on a package is null; therefore,
                // use the dedicated method.
                parent = ElementUtils.parentPackage(elements, (PackageElement) elt);
            } else {
                parent = elt.getEnclosingElement();
            }

            if (parent != null &&
                    isElementAnnotatedForThisChecker(parent)) {
                elementAnnotatedForThisChecker = true;
            }
        }

        elementAnnotatedFors.put(elt, elementAnnotatedForThisChecker);

        return elementAnnotatedForThisChecker;
    }

    private DefaultSet defaultsAt(final Element elt) {
        if (elt == null) {
            return DefaultSet.EMPTY;
        }

        if (elementDefaults.containsKey(elt)) {
            return elementDefaults.get(elt);
        }

        DefaultSet qualifiers = null;

        {
            DefaultQualifier d = elt.getAnnotation(DefaultQualifier.class);

            if (d != null) {
                qualifiers = new DefaultSet();
                Set<Default> p = fromDefaultQualifier(d);

                if (p != null) {
                    qualifiers.addAll(p);
                }
            }
        }

        {
            DefaultQualifiers ds = elt.getAnnotation(DefaultQualifiers.class);
            if (ds != null) {
                if (qualifiers == null) {
                    qualifiers = new DefaultSet();
                }
                for (DefaultQualifier d : ds.value()) {
                    Set<Default> p = fromDefaultQualifier(d);
                    if (p != null) {
                        qualifiers.addAll(p);
                    }
                }
            }
        }

        Element parent;
        if (elt.getKind() == ElementKind.PACKAGE) {
            parent = ElementUtils.parentPackage(elements, (PackageElement) elt);
        } else {
            parent = elt.getEnclosingElement();
        }

        DefaultSet parentDefaults = defaultsAt(parent);
        if (qualifiers == null || qualifiers.isEmpty()) {
            qualifiers = parentDefaults;
        } else {
            qualifiers.addAll(parentDefaults);
        }

        if (qualifiers != null && !qualifiers.isEmpty()) {
            elementDefaults.put(elt, qualifiers);
            return qualifiers;
        } else {
            return DefaultSet.EMPTY;
        }
    }

    /*
     * Given an element, returns whether the unchecked code default (i.e. conservative defaults)
     * should be applied for it. Handles elements from bytecode or source code.
     */
    public boolean applyUncheckedCodeDefaults(final Element annotationScope) {

        if (annotationScope == null) {
            return false;
        }

        if (uncheckedCodeDefaults.size() > 0) {
            // TODO: I would expect this:
            //   atypeFactory.isFromByteCode(annotationScope)) {
            // to work instead of the
            // isElementFromByteCode/declarationFromElement/isFromStubFile calls,
            // but it doesn't work correctly and tests fail.

            boolean isFromStubFile = atypeFactory.isFromStubFile(annotationScope);
            boolean isBytecode = ElementUtils.isElementFromByteCode(annotationScope) &&
                           atypeFactory.declarationFromElement(annotationScope) == null &&
                           !isFromStubFile;
            if (isBytecode) {
                return useUncheckedCodeDefaultsBytecode;
            } else if (isFromStubFile) {
                //TODO: Types in stub files not annotated for a particular checker should be
                // treated as unchecked bytecode.   For now, all types in stub files are treated as
                // checked code. Eventually, @AnnotateFor(checker) will be programmatically added
                // to methods in stub files supplied via the @Stubfile annotation.  Stub files will
                // be treated like unchecked code except for methods in the scope for an @AnnotatedFor.
                return false;
            } else if (useUncheckedCodeDefaultsSource) {
                return !isElementAnnotatedForThisChecker(annotationScope);
            }
        }
        return false;
    }

    /**
     * Applies default annotations to a type. Conservative defaults are applied first
     * as appropriate, followed by source code defaults.
     * <p>
     *
     * For a discussion on the rules for application of source code and conservative defaults,
     * please see the linked manual sections.
     *
     * @param annotationScope the element representing the nearest enclosing
     *        default annotation scope for the type
     * @param type the type to which defaults will be applied
     *
     * @checker_framework.manual #effective-qualifier The effective qualifier on a type (defaults and inference)
     * @checker_framework.manual #annotating-libraries Annotating libraries
     */
    private void applyDefaultsElement(final Element annotationScope, final AnnotatedTypeMirror type) {
        DefaultSet defaults = defaultsAt(annotationScope);
        DefaultApplierElement applier = new DefaultApplierElement(atypeFactory, annotationScope, type, applyToTypeVar);

        for (Default def : defaults) {
            applier.apply(def);
        }

        if (applyUncheckedCodeDefaults(annotationScope)) {
            for (Default def : uncheckedCodeDefaults) {
                applier.apply(def);
            }
        }

        for (Default def : checkedCodeDefaults) {
            applier.apply(def);
        }
    }

    public static class DefaultApplierElement {

        private final AnnotatedTypeFactory atypeFactory;
        private final Element scope;
        private final AnnotatedTypeMirror type;

        // Should only be set by {@link apply}
        private TypeUseLocation location;

        private final DefaultApplierElementImpl impl;

        /*Local type variables are defaulted to top when flow is turned on
          We only want to default the top level type variable (and not type variables that are nested
          in its bounds). E.g.,
            <T extends List<E>, E extends Object> void method() {
               T t;
            }
          We would like t to have its primary annotation defaulted but NOT the E inside its upper bound.
          we use referential equality with the top level type var to determine which ones are definite
          type uses, i.e. uses which can be defaulted
        */
        private final AnnotatedTypeVariable defaultableTypeVar;

        public DefaultApplierElement(AnnotatedTypeFactory atypeFactory, Element scope, AnnotatedTypeMirror type, boolean applyToTypeVar) {
            this.atypeFactory = atypeFactory;
            this.scope = scope;
            this.type = type;
            this.impl = new DefaultApplierElementImpl();
            this.defaultableTypeVar = (applyToTypeVar) ? (AnnotatedTypeVariable) type : null;
        }

        public void apply(Default def) {
            this.location = def.location;
            impl.visit(type, def.anno);
        }

        /**
         * Returns true if the given qualifier should be applied to the given type.  Currently we do not
         * apply defaults to void types, packages, wildcards, and type variables.
         *
         * @param type Type to which qual would be applied
         * @return true if this application should proceed
         */
        private static boolean shouldBeAnnotated(final AnnotatedTypeMirror type, final boolean applyToTypeVar) {

            return !( type == null ||
                    // TODO: executables themselves should not be annotated
                    // For some reason polyall-tests failes with this.
                    // type.getKind() == TypeKind.EXECUTABLE ||
                    type.getKind() == TypeKind.NONE ||
                    type.getKind() == TypeKind.WILDCARD ||
                    (type.getKind() == TypeKind.TYPEVAR  && !applyToTypeVar) ||
                    type instanceof AnnotatedNoType );

        }

        private static void doApply(AnnotatedTypeMirror type, AnnotationMirror qual) {
            // Add the default annotation, but only if no other
            // annotation is present.
            if (!type.isAnnotatedInHierarchy(qual)) {
                type.addAnnotation(qual);
            }

            /* Intersection types, list the types in the direct supertypes.
             * Make sure to apply the default there too.
             */
            if (type.getKind() == TypeKind.INTERSECTION) {
                List<AnnotatedDeclaredType> sups = ((AnnotatedIntersectionType)type).directSuperTypesField();
                if (sups != null) {
                    for (AnnotatedTypeMirror sup : sups) {
                        if (!sup.isAnnotatedInHierarchy(qual)) {
                            sup.addAnnotation(qual);
                        }
                    }
                }
            }
        }


        private class DefaultApplierElementImpl extends AnnotatedTypeScanner<Void, AnnotationMirror> {

            @Override
            public Void scan(AnnotatedTypeMirror t, AnnotationMirror qual) {
                if (!shouldBeAnnotated(t, t == defaultableTypeVar)) {
                    return super.scan(t, qual);
                }

                switch (location) {
                case FIELD: {
                    if (scope != null && scope.getKind() == ElementKind.FIELD &&
                            t == type) {
                        doApply(t, qual);
                    }
                    break;
                }
                case LOCAL_VARIABLE: {
                    if (scope != null && scope.getKind() == ElementKind.LOCAL_VARIABLE &&
                            t == type) {
                        // TODO: how do we determine that we are in a cast or instanceof type?
                        doApply(t, qual);
                    }
                    break;
                }
                case RESOURCE_VARIABLE: {
                    if (scope != null && scope.getKind() == ElementKind.RESOURCE_VARIABLE &&
                            t == type) {
                        doApply(t, qual);
                    }
                    break;
                }
                case EXCEPTION_PARAMETER: {

                    if (scope != null && scope.getKind() == ElementKind.EXCEPTION_PARAMETER &&
                            t == type) {
                        doApply(t, qual);
                        if (t.getKind() == TypeKind.UNION) {
                            AnnotatedUnionType aut = (AnnotatedUnionType) t;
                            //Also apply the default to the alternative types
                            for (AnnotatedDeclaredType anno : aut
                                    .getAlternatives()) {
                                doApply(anno, qual);
                            }
                        }
                    }
                    break;
                }
                case PARAMETER: {
                    if (scope != null && scope.getKind() == ElementKind.PARAMETER &&
                            t == type) {
                        doApply(t, qual);
                    } else if (scope != null && (scope.getKind() == ElementKind.METHOD || scope.getKind() == ElementKind.CONSTRUCTOR) &&
                            t.getKind() == TypeKind.EXECUTABLE &&
                            t == type) {

                        for (AnnotatedTypeMirror atm : ((AnnotatedExecutableType)t).getParameterTypes()) {
                            if (shouldBeAnnotated(atm, false)) {
                                doApply(atm, qual);
                            }
                        }
                    }
                    break;
                }
                case RECEIVER: {
                    if (scope != null && scope.getKind() == ElementKind.PARAMETER &&
                            t == type && "this".equals(scope.getSimpleName())) {
                        // TODO: comparison against "this" is ugly, won't work
                        // for all possible names for receiver parameter.
                        doApply(t, qual);
                    } else if (scope != null && (scope.getKind() == ElementKind.METHOD) &&
                            t.getKind() == TypeKind.EXECUTABLE &&
                            t == type) {

                        final AnnotatedDeclaredType receiver = ((AnnotatedExecutableType) t).getReceiverType();
                        if (shouldBeAnnotated(receiver, false)) {
                            doApply(receiver, qual);
                        }
                    }
                    break;
                }
                case RETURN: {
                    if (scope != null && scope.getKind() == ElementKind.METHOD &&
                            t.getKind() == TypeKind.EXECUTABLE &&
                            t == type) {
                        final AnnotatedTypeMirror returnType = ((AnnotatedExecutableType)t).getReturnType();
                        if (shouldBeAnnotated(returnType, false)) {
                            doApply(returnType, qual);
                        }
                    }
                    break;
                }


                case IMPLICIT_LOWER_BOUND: {
                    if (isLowerBound && boundType.isOneOf(BoundType.UNBOUND, BoundType.UPPER, BoundType.UNKNOWN)) {
                        doApply(t, qual);
                    }
                    break;
                }

                case EXPLICIT_LOWER_BOUND: {
                    if (isLowerBound && boundType.isOneOf(BoundType.LOWER)) {
                        doApply(t, qual);
                    }
                    break;
                }

                case LOWER_BOUND: {
                    if (isLowerBound) {
                        doApply(t, qual);
                    }
                    break;
                }

                case IMPLICIT_UPPER_BOUND: {
                    if (isUpperBound && boundType.isOneOf(BoundType.UNBOUND, BoundType.LOWER)) {
                        doApply(t, qual);
                    }
                    break;
                }
                case EXPLICIT_UPPER_BOUND: {
                    if (isUpperBound && boundType.isOneOf(BoundType.UPPER, BoundType.UNKNOWN)) {
                        doApply(t, qual);
                    }
                    break;
                }
                case UPPER_BOUND: {
                    if (this.isUpperBound) {
                        doApply(t, qual);
                    }
                    break;
                }
                case OTHERWISE:
                case ALL: {
                    // TODO: forbid ALL if anything else was given.
                    doApply(t, qual);
                    break;
                }
                default: {
                    ErrorReporter
                            .errorAbort("QualifierDefaults.DefaultApplierElement: unhandled location: " +
                                    location);
                    return null;
                }
                }

                return super.scan(t, qual);
            }

            @Override
            public void reset() {
                super.reset();
                impl.isLowerBound = false;
                impl.isUpperBound = false;
                impl.boundType = BoundType.UNBOUND;
            }

            //are we currently defaulting the lower bound of a type variable or wildcard
            private boolean isLowerBound = false;

            //are we currently defaulting the upper bound of a type variable or wildcard
            private boolean isUpperBound  = false;

            //the bound type of the current wildcard or type variable being defaulted
            private BoundType boundType = BoundType.UNBOUND;

            @Override
            public Void visitTypeVariable(AnnotatedTypeVariable type,
                    AnnotationMirror qual) {
                if (visitedNodes.containsKey(type)) {
                    return visitedNodes.get(type);
                }

                visitBounds(type, type.getUpperBound(), type.getLowerBound(), qual);
                return null;
            }

            @Override
            public Void visitWildcard(AnnotatedWildcardType type,
                    AnnotationMirror qual) {
                if (visitedNodes.containsKey(type)) {
                    return visitedNodes.get(type);
                }

                visitBounds(type, type.getExtendsBound(), type.getSuperBound(), qual);
                return null;
            }

            /**
             * Visit the bounds of a type variable or a wildcard and potentially apply qual
             * to those bounds.  This method will also update the boundType, isLowerBound, and isUpperbound
             * fields.
             */
            protected void visitBounds(AnnotatedTypeMirror boundedType, AnnotatedTypeMirror upperBound,
                                       AnnotatedTypeMirror lowerBound, AnnotationMirror qual) {

                final boolean prevIsUpperBound = isUpperBound;
                final boolean prevIsLowerBound = isLowerBound;
                final BoundType prevBoundType = boundType;

                boundType = getBoundType(boundedType, atypeFactory);

                try {
                    isLowerBound = true;
                    isUpperBound = false;
                    scanAndReduce(lowerBound, qual, null);

                    visitedNodes.put(type, null);


                    isLowerBound = false;
                    isUpperBound = true;
                    scanAndReduce(upperBound, qual, null);

                    visitedNodes.put(type, null);

                } finally {
                    isUpperBound = prevIsUpperBound;
                    isLowerBound = prevIsLowerBound;
                    boundType = prevBoundType;
                }
            }
        }
    }

    enum BoundType {

        /**
         * Indicates an upper bounded type variable or wildcard
         */
        UPPER,

        /**
         * Indicates a lower bounded type variable or wildcard
         */
        LOWER,

        /**
         * Neither bound is specified, BOTH are implicit
         */
        UNBOUND,

        /**
         * For bytecode, or trees for which we no longer have the compilation unit.
         * We treat UNKNOWN bounds as if they are an UPPER bound.
         */
        UNKNOWN;

        public boolean isOneOf(final BoundType ... choices) {
            for (final BoundType choice : choices) {
                if (this == choice) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * @param type The type whose boundType is returned.
     *             type must be an AnnotatedWildcardType or AnnotatedTypeVariable
     * @return The boundType for type
     */
    private static BoundType getBoundType(final AnnotatedTypeMirror type,
                                          final AnnotatedTypeFactory typeFactory) {
        if (type instanceof AnnotatedTypeVariable) {
            return getTypeVarBoundType((AnnotatedTypeVariable) type, typeFactory);
        }

        if (type instanceof AnnotatedWildcardType) {
            return getWildcardBoundType((AnnotatedWildcardType) type, typeFactory);
        }

        ErrorReporter.errorAbort("Unexpected type kind: type=" + type);
        return null; //dead code
    }

    /**
     * @return the bound type of the input typeVar
     */
    private static BoundType getTypeVarBoundType(final AnnotatedTypeVariable typeVar,
                                                 final AnnotatedTypeFactory typeFactory) {
        return getTypeVarBoundType((TypeParameterElement) typeVar.getUnderlyingType().asElement(), typeFactory);
    }

    /**
     * @return The boundType (UPPER, UNBOUND, or UNKNOWN) of the declaration of typeParamElem.
     */
    private static BoundType getTypeVarBoundType(final TypeParameterElement typeParamElem,
                                                 final AnnotatedTypeFactory typeFactory) {
        final BoundType prev = elementToBoundType.get(typeParamElem);
        if (prev != null) {
            return prev;
        }

        TreePath declaredTypeVarEle = typeFactory.getTreeUtils().getPath(typeParamElem);
        Tree typeParamDecl = declaredTypeVarEle == null ? null : declaredTypeVarEle.getLeaf();

        final BoundType boundType;
        if (typeParamDecl == null) {
            // This is not only for elements from binaries, but also
            // when the compilation unit is no-longer available.
            boundType = BoundType.UNKNOWN;

        } else {
            if (typeParamDecl.getKind() == Tree.Kind.TYPE_PARAMETER) {
                final TypeParameterTree tptree = (TypeParameterTree) typeParamDecl;

                List<? extends Tree> bnds = tptree.getBounds();
                if (bnds != null && !bnds.isEmpty()) {
                    boundType = BoundType.UPPER;
                } else {
                    boundType = BoundType.UNBOUND;
                }
            } else {
                ErrorReporter.errorAbort("Unexpected tree type for typeVar Element:\n"
                                       + "typeParamElem=" + typeParamElem + "\n"
                                       + typeParamDecl);
                boundType = null; //dead code
            }
        }

        elementToBoundType.put(typeParamElem, boundType);
        return boundType;
    }

    /**
     * @return the BoundType of annotatedWildcard.  If it is unbounded, use the type parameter to
     * which its an argument
     */
    public static BoundType getWildcardBoundType(final AnnotatedWildcardType annotatedWildcard,
                                                final AnnotatedTypeFactory typeFactory) {

        final WildcardType wildcard = (WildcardType) annotatedWildcard.getUnderlyingType();

        final BoundType boundType;
        if (wildcard.isUnbound() && wildcard.bound != null) {
            boundType = getTypeVarBoundType((TypeParameterElement) wildcard.bound.asElement(), typeFactory);

        } else {
            //note: isSuperBound will be true for unbounded and lowers, but the unbounded case is already handled
            boundType = wildcard.isSuperBound() ? BoundType.LOWER : BoundType.UPPER;
        }

        return boundType;
    }
}
