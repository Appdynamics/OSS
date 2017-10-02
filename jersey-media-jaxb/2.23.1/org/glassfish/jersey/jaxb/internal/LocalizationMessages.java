
package org.glassfish.jersey.jaxb.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.jaxb.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizableSAX_CANNOT_DISABLE_PARAMETER_ENTITY_PROCESSING_FEATURE(Object arg0) {
        return messageFactory.getMessage("sax.cannot.disable.parameter.entity.processing.feature", arg0);
    }

    /**
     * Cannot disable external parameter entity processing feature on SAX parser factory [{0}].
     * 
     */
    public static String SAX_CANNOT_DISABLE_PARAMETER_ENTITY_PROCESSING_FEATURE(Object arg0) {
        return localizer.localize(localizableSAX_CANNOT_DISABLE_PARAMETER_ENTITY_PROCESSING_FEATURE(arg0));
    }

    public static Localizable localizableERROR_READING_ENTITY_MISSING() {
        return messageFactory.getMessage("error.reading.entity.missing");
    }

    /**
     * Missing entity.
     * 
     */
    public static String ERROR_READING_ENTITY_MISSING() {
        return localizer.localize(localizableERROR_READING_ENTITY_MISSING());
    }

    public static Localizable localizableERROR_UNMARSHALLING_JAXB(Object arg0) {
        return messageFactory.getMessage("error.unmarshalling.jaxb", arg0);
    }

    /**
     * Error un-marshalling JAXB object of type: {0}.
     * 
     */
    public static String ERROR_UNMARSHALLING_JAXB(Object arg0) {
        return localizer.localize(localizableERROR_UNMARSHALLING_JAXB(arg0));
    }

    public static Localizable localizableSAX_CANNOT_DISABLE_GENERAL_ENTITY_PROCESSING_FEATURE(Object arg0) {
        return messageFactory.getMessage("sax.cannot.disable.general.entity.processing.feature", arg0);
    }

    /**
     * Cannot disable external general entity processing feature on SAX parser factory [{0}].
     * 
     */
    public static String SAX_CANNOT_DISABLE_GENERAL_ENTITY_PROCESSING_FEATURE(Object arg0) {
        return localizer.localize(localizableSAX_CANNOT_DISABLE_GENERAL_ENTITY_PROCESSING_FEATURE(arg0));
    }

    public static Localizable localizableSAX_CANNOT_ENABLE_SECURE_PROCESSING_FEATURE(Object arg0) {
        return messageFactory.getMessage("sax.cannot.enable.secure.processing.feature", arg0);
    }

    /**
     * JAXP feature XMLConstants.FEATURE_SECURE_PROCESSING cannot be set on a SAX parser factory [{0}].
     * 
     */
    public static String SAX_CANNOT_ENABLE_SECURE_PROCESSING_FEATURE(Object arg0) {
        return localizer.localize(localizableSAX_CANNOT_ENABLE_SECURE_PROCESSING_FEATURE(arg0));
    }

    public static Localizable localizableUNABLE_TO_SECURE_XML_TRANSFORMER_PROCESSING() {
        return messageFactory.getMessage("unable.to.secure.xml.transformer.processing");
    }

    /**
     * Unable to configure secure XML processing feature on the XML transformer factory.
     * 
     */
    public static String UNABLE_TO_SECURE_XML_TRANSFORMER_PROCESSING() {
        return localizer.localize(localizableUNABLE_TO_SECURE_XML_TRANSFORMER_PROCESSING());
    }

    public static Localizable localizableSAX_XDK_NO_SECURITY_FEATURES() {
        return messageFactory.getMessage("sax.xdk.no.security.features");
    }

    /**
     * Using XDK. No security features will be enabled for the SAX parser.
     * 
     */
    public static String SAX_XDK_NO_SECURITY_FEATURES() {
        return localizer.localize(localizableSAX_XDK_NO_SECURITY_FEATURES());
    }

    public static Localizable localizableUNABLE_TO_ACCESS_METHODS_OF_CLASS(Object arg0) {
        return messageFactory.getMessage("unable.to.access.methods.of.class", arg0);
    }

    /**
     * Unable to access the methods of the class [{0}]. Caller's class-loader hierarchy for not in the ancestor chain of the class.
     * 
     */
    public static String UNABLE_TO_ACCESS_METHODS_OF_CLASS(Object arg0) {
        return localizer.localize(localizableUNABLE_TO_ACCESS_METHODS_OF_CLASS(arg0));
    }

    public static Localizable localizableNO_PARAM_CONSTRUCTOR_MISSING(Object arg0) {
        return messageFactory.getMessage("no.param.constructor.missing", arg0);
    }

    /**
     * No-param constructor not found in the class [{0}].
     * 
     */
    public static String NO_PARAM_CONSTRUCTOR_MISSING(Object arg0) {
        return localizer.localize(localizableNO_PARAM_CONSTRUCTOR_MISSING(arg0));
    }

    public static Localizable localizableUNABLE_TO_INSTANTIATE_CLASS(Object arg0) {
        return messageFactory.getMessage("unable.to.instantiate.class", arg0);
    }

    /**
     * Unable to instantiate the class [{0}].
     * 
     */
    public static String UNABLE_TO_INSTANTIATE_CLASS(Object arg0) {
        return localizer.localize(localizableUNABLE_TO_INSTANTIATE_CLASS(arg0));
    }

}
