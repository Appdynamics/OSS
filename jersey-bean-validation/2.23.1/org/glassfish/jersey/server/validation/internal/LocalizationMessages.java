
package org.glassfish.jersey.server.validation.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.server.validation.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizableVALIDATION_EXCEPTION_PROVIDER() {
        return messageFactory.getMessage("validation.exception.provider");
    }

    /**
     * Failed to configure the default validation provider.
     * 
     */
    public static String VALIDATION_EXCEPTION_PROVIDER() {
        return localizer.localize(localizableVALIDATION_EXCEPTION_PROVIDER());
    }

    public static Localizable localizableOVERRIDE_CHECK_ERROR(Object arg0) {
        return messageFactory.getMessage("override.check.error", arg0);
    }

    /**
     * Multiple ValidateOnExecution annotation definitions are hosted in the hierarchy of {0} method.
     * 
     */
    public static String OVERRIDE_CHECK_ERROR(Object arg0) {
        return localizer.localize(localizableOVERRIDE_CHECK_ERROR(arg0));
    }

    public static Localizable localizableCONSTRAINT_VIOLATIONS_ENCOUNTERED() {
        return messageFactory.getMessage("constraint.violations.encountered");
    }

    /**
     * Following ConstraintViolations has been encountered.
     * 
     */
    public static String CONSTRAINT_VIOLATIONS_ENCOUNTERED() {
        return localizer.localize(localizableCONSTRAINT_VIOLATIONS_ENCOUNTERED());
    }

    public static Localizable localizableVALIDATION_EXCEPTION_RAISED() {
        return messageFactory.getMessage("validation.exception.raised");
    }

    /**
     * Unexpected Bean Validation problem.
     * 
     */
    public static String VALIDATION_EXCEPTION_RAISED() {
        return localizer.localize(localizableVALIDATION_EXCEPTION_RAISED());
    }

}
