
package org.glassfish.jersey.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizableERRORS_AND_WARNINGS_DETECTED(Object arg0) {
        return messageFactory.getMessage("errors.and.warnings.detected", arg0);
    }

    /**
     * Following issues have been detected: {0}
     * 
     */
    public static String ERRORS_AND_WARNINGS_DETECTED(Object arg0) {
        return localizer.localize(localizableERRORS_AND_WARNINGS_DETECTED(arg0));
    }

    public static Localizable localizableCOMMITTING_STREAM_BUFFERING_ILLEGAL_STATE() {
        return messageFactory.getMessage("committing.stream.buffering.illegal.state");
    }

    /**
     * Cannot setup buffering as bytes have already been written to the output stream. The entity buffering can be initialized only before first bytes are written to the entity output stream.
     * 
     */
    public static String COMMITTING_STREAM_BUFFERING_ILLEGAL_STATE() {
        return localizer.localize(localizableCOMMITTING_STREAM_BUFFERING_ILLEGAL_STATE());
    }

    public static Localizable localizableLOCALE_IS_NULL() {
        return messageFactory.getMessage("locale.is.null");
    }

    /**
     * Locale is null.
     * 
     */
    public static String LOCALE_IS_NULL() {
        return localizer.localize(localizableLOCALE_IS_NULL());
    }

    public static Localizable localizableSSL_KMF_PROVIDER_NOT_REGISTERED() {
        return messageFactory.getMessage("ssl.kmf.provider.not.registered");
    }

    /**
     * Error initializing key manager factory (provider not registered).
     * 
     */
    public static String SSL_KMF_PROVIDER_NOT_REGISTERED() {
        return localizer.localize(localizableSSL_KMF_PROVIDER_NOT_REGISTERED());
    }

    public static Localizable localizableURI_COMPONENT_ENCODED_OCTET_INVALID_DIGIT(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.component.encoded.octet.invalid.digit", arg0, arg1);
    }

    /**
     * Malformed percent-encoded octet at index {0}, invalid hexadecimal digit ''{1}''.
     * 
     */
    public static String URI_COMPONENT_ENCODED_OCTET_INVALID_DIGIT(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_COMPONENT_ENCODED_OCTET_INVALID_DIGIT(arg0, arg1));
    }

    public static Localizable localizableURI_PARSER_COMPONENT_DELIMITER(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.parser.component.delimiter", arg0, arg1);
    }

    /**
     * Component does not end by a delimiter ''{0}'' at index {1}.
     * 
     */
    public static String URI_PARSER_COMPONENT_DELIMITER(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_PARSER_COMPONENT_DELIMITER(arg0, arg1));
    }

    public static Localizable localizableHK_2_REIFICATION_ERROR(Object arg0, Object arg1) {
        return messageFactory.getMessage("hk2.reification.error", arg0, arg1);
    }

    /**
     * HK2 service reification failed for [{0}] with an exception:
     * {1}
     * 
     */
    public static String HK_2_REIFICATION_ERROR(Object arg0, Object arg1) {
        return localizer.localize(localizableHK_2_REIFICATION_ERROR(arg0, arg1));
    }

    public static Localizable localizableSSL_KMF_ALGORITHM_NOT_SUPPORTED() {
        return messageFactory.getMessage("ssl.kmf.algorithm.not.supported");
    }

    /**
     * Error initializing key manager factory (algorithm not supported).
     * 
     */
    public static String SSL_KMF_ALGORITHM_NOT_SUPPORTED() {
        return localizer.localize(localizableSSL_KMF_ALGORITHM_NOT_SUPPORTED());
    }

    public static Localizable localizableERROR_MBR_ISREADABLE(Object arg0) {
        return messageFactory.getMessage("error.mbr.isreadable", arg0);
    }

    /**
     * MesssageBodyReader {0} threw exception in isReadable - skipping.
     * 
     */
    public static String ERROR_MBR_ISREADABLE(Object arg0) {
        return localizer.localize(localizableERROR_MBR_ISREADABLE(arg0));
    }

    public static Localizable localizableSSL_KMF_INIT_FAILED() {
        return messageFactory.getMessage("ssl.kmf.init.failed");
    }

    /**
     * Error initializing key manager factory (operation failed).
     * 
     */
    public static String SSL_KMF_INIT_FAILED() {
        return localizer.localize(localizableSSL_KMF_INIT_FAILED());
    }

    public static Localizable localizableOVERRIDING_METHOD_CANNOT_BE_FOUND(Object arg0, Object arg1) {
        return messageFactory.getMessage("overriding.method.cannot.be.found", arg0, arg1);
    }

    /**
     * Method that overrides {0} cannot be found on class {1}.
     * 
     */
    public static String OVERRIDING_METHOD_CANNOT_BE_FOUND(Object arg0, Object arg1) {
        return localizer.localize(localizableOVERRIDING_METHOD_CANNOT_BE_FOUND(arg0, arg1));
    }

    public static Localizable localizableERROR_INTERCEPTOR_READER_PROCEED() {
        return messageFactory.getMessage("error.interceptor.reader.proceed");
    }

    /**
     * Last reader interceptor in the chain called the method proceed.
     * 
     */
    public static String ERROR_INTERCEPTOR_READER_PROCEED() {
        return localizer.localize(localizableERROR_INTERCEPTOR_READER_PROCEED());
    }

    public static Localizable localizableMEDIA_TYPE_IS_NULL() {
        return messageFactory.getMessage("media.type.is.null");
    }

    /**
     * Media type is null.
     * 
     */
    public static String MEDIA_TYPE_IS_NULL() {
        return localizer.localize(localizableMEDIA_TYPE_IS_NULL());
    }

    public static Localizable localizableURI_COMPONENT_ENCODED_OCTET_MALFORMED(Object arg0) {
        return messageFactory.getMessage("uri.component.encoded.octet.malformed", arg0);
    }

    /**
     * Malformed percent-encoded octet at index {0}.
     * 
     */
    public static String URI_COMPONENT_ENCODED_OCTET_MALFORMED(Object arg0) {
        return localizer.localize(localizableURI_COMPONENT_ENCODED_OCTET_MALFORMED(arg0));
    }

    public static Localizable localizableSSL_KS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return messageFactory.getMessage("ssl.ks.integrity.algorithm.not.found");
    }

    /**
     * Error initializing key store (algorithm to check key store integrity not found).
     * 
     */
    public static String SSL_KS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return localizer.localize(localizableSSL_KS_INTEGRITY_ALGORITHM_NOT_FOUND());
    }

    public static Localizable localizableMESSAGE_CONTENT_BUFFER_RESET_FAILED() {
        return messageFactory.getMessage("message.content.buffer.reset.failed");
    }

    /**
     * Error resetting the buffered message content input stream.
     * 
     */
    public static String MESSAGE_CONTENT_BUFFER_RESET_FAILED() {
        return localizer.localize(localizableMESSAGE_CONTENT_BUFFER_RESET_FAILED());
    }

    public static Localizable localizableTEMPLATE_PARAM_NULL() {
        return messageFactory.getMessage("template.param.null");
    }

    /**
     * One or more of template keys or values are null.
     * 
     */
    public static String TEMPLATE_PARAM_NULL() {
        return localizer.localize(localizableTEMPLATE_PARAM_NULL());
    }

    public static Localizable localizableSSL_TMF_INIT_FAILED() {
        return messageFactory.getMessage("ssl.tmf.init.failed");
    }

    /**
     * Error initializing trust manager factory (operation failed).
     * 
     */
    public static String SSL_TMF_INIT_FAILED() {
        return localizer.localize(localizableSSL_TMF_INIT_FAILED());
    }

    public static Localizable localizableURI_BUILDER_CLASS_PATH_ANNOTATION_MISSING(Object arg0) {
        return messageFactory.getMessage("uri.builder.class.path.annotation.missing", arg0);
    }

    /**
     * The class, {0} is not annotated with @Path.
     * 
     */
    public static String URI_BUILDER_CLASS_PATH_ANNOTATION_MISSING(Object arg0) {
        return localizer.localize(localizableURI_BUILDER_CLASS_PATH_ANNOTATION_MISSING(arg0));
    }

    public static Localizable localizableUNHANDLED_EXCEPTION_DETECTED(Object arg0) {
        return messageFactory.getMessage("unhandled.exception.detected", arg0);
    }

    /**
     * Unhandled exception detected on thread {0}.
     * 
     */
    public static String UNHANDLED_EXCEPTION_DETECTED(Object arg0) {
        return localizer.localize(localizableUNHANDLED_EXCEPTION_DETECTED(arg0));
    }

    public static Localizable localizableNOT_SUPPORTED_ON_OUTBOUND_MESSAGE() {
        return messageFactory.getMessage("not.supported.on.outbound.message");
    }

    /**
     * Method not supported on an outbound message.
     * 
     */
    public static String NOT_SUPPORTED_ON_OUTBOUND_MESSAGE() {
        return localizer.localize(localizableNOT_SUPPORTED_ON_OUTBOUND_MESSAGE());
    }

    public static Localizable localizableUNABLE_TO_PARSE_HEADER_VALUE(Object arg0, Object arg1) {
        return messageFactory.getMessage("unable.to.parse.header.value", arg0, arg1);
    }

    /**
     * Unable to parse "{0}" header value: "{1}"
     * 
     */
    public static String UNABLE_TO_PARSE_HEADER_VALUE(Object arg0, Object arg1) {
        return localizer.localize(localizableUNABLE_TO_PARSE_HEADER_VALUE(arg0, arg1));
    }

    public static Localizable localizableERROR_PROVIDER_CONSTRAINED_TO_WRONG_RUNTIME(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.provider.constrainedTo.wrong.runtime", arg0, arg1, arg2);
    }

    /**
     * A provider {0} registered in {2} runtime is constrained (via @ConstrainedTo) to {1} runtime.
     * 
     */
    public static String ERROR_PROVIDER_CONSTRAINED_TO_WRONG_RUNTIME(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_PROVIDER_CONSTRAINED_TO_WRONG_RUNTIME(arg0, arg1, arg2));
    }

    public static Localizable localizableSSL_KMF_NO_PASSWORD_SET(Object arg0) {
        return messageFactory.getMessage("ssl.kmf.no.password.set", arg0);
    }

    /**
     * Neither key password nor key store password has been set for {0} key store. Ignoring the key store configuration and skipping the key manager factory initialization. Key manager factory will not be configured in the current SSL context.
     * 
     */
    public static String SSL_KMF_NO_PASSWORD_SET(Object arg0) {
        return localizer.localize(localizableSSL_KMF_NO_PASSWORD_SET(arg0));
    }

    public static Localizable localizablePARAM_NULL(Object arg0) {
        return messageFactory.getMessage("param.null", arg0);
    }

    /**
     * "{0}" parameter is null.
     * 
     */
    public static String PARAM_NULL(Object arg0) {
        return localizer.localize(localizablePARAM_NULL(arg0));
    }

    public static Localizable localizableHTTP_HEADER_UNBALANCED_QUOTED() {
        return messageFactory.getMessage("http.header.unbalanced.quoted");
    }

    /**
     * Unbalanced quoted string.
     * 
     */
    public static String HTTP_HEADER_UNBALANCED_QUOTED() {
        return localizer.localize(localizableHTTP_HEADER_UNBALANCED_QUOTED());
    }

    public static Localizable localizableLINK_IS_NULL() {
        return messageFactory.getMessage("link.is.null");
    }

    /**
     * Link is null.
     * 
     */
    public static String LINK_IS_NULL() {
        return localizer.localize(localizableLINK_IS_NULL());
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_PART_OF_NAME(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.template.parser.illegal.char.partOf.name", arg0, arg1, arg2);
    }

    /**
     * Illegal character "{0}" at position {1} is not allowed as a part of a name in a path template "{2}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_PART_OF_NAME(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_PART_OF_NAME(arg0, arg1, arg2));
    }

    public static Localizable localizablePROPERTIES_HELPER_DEPRECATED_PROPERTY_NAME(Object arg0, Object arg1) {
        return messageFactory.getMessage("properties.helper.deprecated.property.name", arg0, arg1);
    }

    /**
     * Deprecated property name "{0}" usage was found. Please use "{1}" instead.
     * 
     */
    public static String PROPERTIES_HELPER_DEPRECATED_PROPERTY_NAME(Object arg0, Object arg1) {
        return localizer.localize(localizablePROPERTIES_HELPER_DEPRECATED_PROPERTY_NAME(arg0, arg1));
    }

    public static Localizable localizableCOMPONENT_CANNOT_BE_NULL() {
        return messageFactory.getMessage("component.cannot.be.null");
    }

    /**
     * Registered component instance cannot be null.
     * 
     */
    public static String COMPONENT_CANNOT_BE_NULL() {
        return localizer.localize(localizableCOMPONENT_CANNOT_BE_NULL());
    }

    public static Localizable localizableURI_BUILDER_ANNOTATEDELEMENT_PATH_ANNOTATION_MISSING(Object arg0) {
        return messageFactory.getMessage("uri.builder.annotatedelement.path.annotation.missing", arg0);
    }

    /**
     * The annotated element "{0}" is not annotated with @Path.
     * 
     */
    public static String URI_BUILDER_ANNOTATEDELEMENT_PATH_ANNOTATION_MISSING(Object arg0) {
        return localizer.localize(localizableURI_BUILDER_ANNOTATEDELEMENT_PATH_ANNOTATION_MISSING(arg0));
    }

    public static Localizable localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_CONTEXT(Object arg0) {
        return messageFactory.getMessage("error.service.locator.provider.instance.feature.context", arg0);
    }

    /**
     * Incorrect type of feature context instance {0}. Parameter must be a default Jersey FeatureContext implementation.
     * 
     */
    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_CONTEXT(Object arg0) {
        return localizer.localize(localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_CONTEXT(arg0));
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_START_NAME(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.template.parser.illegal.char.start.name", arg0, arg1, arg2);
    }

    /**
     * Illegal character "{0}" at position {1} is not allowed as a start of a name in a path template "{2}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_START_NAME(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_START_NAME(arg0, arg1, arg2));
    }

    public static Localizable localizableCONFIGURATION_NOT_MODIFIABLE() {
        return messageFactory.getMessage("configuration.not.modifiable");
    }

    /**
     * The configuration is not modifiable in this context.
     * 
     */
    public static String CONFIGURATION_NOT_MODIFIABLE() {
        return localizer.localize(localizableCONFIGURATION_NOT_MODIFIABLE());
    }

    public static Localizable localizableSSL_TS_CERT_LOAD_ERROR() {
        return messageFactory.getMessage("ssl.ts.cert.load.error");
    }

    /**
     * Cannot load trust store certificates.
     * 
     */
    public static String SSL_TS_CERT_LOAD_ERROR() {
        return localizer.localize(localizableSSL_TS_CERT_LOAD_ERROR());
    }

    public static Localizable localizableERROR_FINDING_EXCEPTION_MAPPER_TYPE(Object arg0) {
        return messageFactory.getMessage("error.finding.exception.mapper.type", arg0);
    }

    /**
     * Could not find exception type for given ExceptionMapper class: {0}.
     * 
     */
    public static String ERROR_FINDING_EXCEPTION_MAPPER_TYPE(Object arg0) {
        return localizer.localize(localizableERROR_FINDING_EXCEPTION_MAPPER_TYPE(arg0));
    }

    public static Localizable localizableERROR_NEWCOOKIE_EXPIRES(Object arg0) {
        return messageFactory.getMessage("error.newcookie.expires", arg0);
    }

    /**
     * NewCookie Expires header value ({0}) cannot be read.
     * 
     */
    public static String ERROR_NEWCOOKIE_EXPIRES(Object arg0) {
        return localizer.localize(localizableERROR_NEWCOOKIE_EXPIRES(arg0));
    }

    public static Localizable localizableILLEGAL_INITIAL_CAPACITY(Object arg0) {
        return messageFactory.getMessage("illegal.initial.capacity", arg0);
    }

    /**
     * Illegal initial capacity: {0}.
     * 
     */
    public static String ILLEGAL_INITIAL_CAPACITY(Object arg0) {
        return localizer.localize(localizableILLEGAL_INITIAL_CAPACITY(arg0));
    }

    public static Localizable localizableSSL_KS_CERT_LOAD_ERROR() {
        return messageFactory.getMessage("ssl.ks.cert.load.error");
    }

    /**
     * Cannot load key store certificates.
     * 
     */
    public static String SSL_KS_CERT_LOAD_ERROR() {
        return localizer.localize(localizableSSL_KS_CERT_LOAD_ERROR());
    }

    public static Localizable localizableERROR_READING_ENTITY_FROM_INPUT_STREAM() {
        return messageFactory.getMessage("error.reading.entity.from.input.stream");
    }

    /**
     * Error reading entity from input stream.
     * 
     */
    public static String ERROR_READING_ENTITY_FROM_INPUT_STREAM() {
        return localizer.localize(localizableERROR_READING_ENTITY_FROM_INPUT_STREAM());
    }

    public static Localizable localizableERROR_PROVIDER_CONSTRAINED_TO_IGNORED(Object arg0) {
        return messageFactory.getMessage("error.provider.constrainedTo.ignored", arg0);
    }

    /**
     * Due to constraint configuration problems the provider {0} will be ignored.
     * 
     */
    public static String ERROR_PROVIDER_CONSTRAINED_TO_IGNORED(Object arg0) {
        return localizer.localize(localizableERROR_PROVIDER_CONSTRAINED_TO_IGNORED(arg0));
    }

    public static Localizable localizableHTTP_HEADER_WHITESPACE_NOT_ALLOWED() {
        return messageFactory.getMessage("http.header.whitespace.not.allowed");
    }

    /**
     * White space not allowed.
     * 
     */
    public static String HTTP_HEADER_WHITESPACE_NOT_ALLOWED() {
        return localizer.localize(localizableHTTP_HEADER_WHITESPACE_NOT_ALLOWED());
    }

    public static Localizable localizableILLEGAL_CONFIG_SYNTAX() {
        return messageFactory.getMessage("illegal.config.syntax");
    }

    /**
     * Illegal configuration-file syntax.
     * 
     */
    public static String ILLEGAL_CONFIG_SYNTAX() {
        return localizer.localize(localizableILLEGAL_CONFIG_SYNTAX());
    }

    public static Localizable localizableSSL_TS_FILE_NOT_FOUND(Object arg0) {
        return messageFactory.getMessage("ssl.ts.file.not.found", arg0);
    }

    /**
     * Cannot find trust store file "{0}".
     * 
     */
    public static String SSL_TS_FILE_NOT_FOUND(Object arg0) {
        return localizer.localize(localizableSSL_TS_FILE_NOT_FOUND(arg0));
    }

    public static Localizable localizableERROR_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return messageFactory.getMessage("error.caught.while.loading.spi.providers");
    }

    /**
     * Error caught while loading SPI providers.
     * 
     */
    public static String ERROR_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return localizer.localize(localizableERROR_CAUGHT_WHILE_LOADING_SPI_PROVIDERS());
    }

    public static Localizable localizableMULTIPLE_MATCHING_CONSTRUCTORS_FOUND(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("multiple.matching.constructors.found", arg0, arg1, arg2, arg3);
    }

    /**
     * Found {0} constructors with {1} parameters in {2} class. Selecting the first found constructor: {3}
     * 
     */
    public static String MULTIPLE_MATCHING_CONSTRUCTORS_FOUND(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableMULTIPLE_MATCHING_CONSTRUCTORS_FOUND(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableMETHOD_NOT_GETTER_NOR_SETTER() {
        return messageFactory.getMessage("method.not.getter.nor.setter");
    }

    /**
     * Method is neither getter nor setter.
     * 
     */
    public static String METHOD_NOT_GETTER_NOR_SETTER() {
        return localizer.localize(localizableMETHOD_NOT_GETTER_NOR_SETTER());
    }

    public static Localizable localizableERROR_PARSING_ENTITY_TAG(Object arg0) {
        return messageFactory.getMessage("error.parsing.entity.tag", arg0);
    }

    /**
     * Error parsing entity tag: {0}
     * 
     */
    public static String ERROR_PARSING_ENTITY_TAG(Object arg0) {
        return localizer.localize(localizableERROR_PARSING_ENTITY_TAG(arg0));
    }

    public static Localizable localizableSSL_CTX_ALGORITHM_NOT_SUPPORTED() {
        return messageFactory.getMessage("ssl.ctx.algorithm.not.supported");
    }

    /**
     * Error creating SSL context (algorithm not supported).
     * 
     */
    public static String SSL_CTX_ALGORITHM_NOT_SUPPORTED() {
        return localizer.localize(localizableSSL_CTX_ALGORITHM_NOT_SUPPORTED());
    }

    public static Localizable localizableHK_2_UNKNOWN_ERROR(Object arg0) {
        return messageFactory.getMessage("hk2.unknown.error", arg0);
    }

    /**
     * Unknown HK2 failure detected:
     * {0}
     * 
     */
    public static String HK_2_UNKNOWN_ERROR(Object arg0) {
        return localizer.localize(localizableHK_2_UNKNOWN_ERROR(arg0));
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_AFTER_NAME(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.template.parser.illegal.char.after.name", arg0, arg1, arg2);
    }

    /**
     * Illegal character "{0}" at position {1} is not allowed after a name in a path template "{2}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_AFTER_NAME(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_ILLEGAL_CHAR_AFTER_NAME(arg0, arg1, arg2));
    }

    public static Localizable localizableOUTPUT_STREAM_CLOSED() {
        return messageFactory.getMessage("output.stream.closed");
    }

    /**
     * The output stream has already been closed.
     * 
     */
    public static String OUTPUT_STREAM_CLOSED() {
        return localizer.localize(localizableOUTPUT_STREAM_CLOSED());
    }

    public static Localizable localizableENTITY_TAG_IS_NULL() {
        return messageFactory.getMessage("entity.tag.is.null");
    }

    /**
     * Entity tag is null.
     * 
     */
    public static String ENTITY_TAG_IS_NULL() {
        return localizer.localize(localizableENTITY_TAG_IS_NULL());
    }

    public static Localizable localizableINPUT_STREAM_CLOSED() {
        return messageFactory.getMessage("input.stream.closed");
    }

    /**
     * Input stream has been closed.
     * 
     */
    public static String INPUT_STREAM_CLOSED() {
        return localizer.localize(localizableINPUT_STREAM_CLOSED());
    }

    public static Localizable localizableCOOKIE_IS_NULL() {
        return messageFactory.getMessage("cookie.is.null");
    }

    /**
     * Cookie is null.
     * 
     */
    public static String COOKIE_IS_NULL() {
        return localizer.localize(localizableCOOKIE_IS_NULL());
    }

    public static Localizable localizableNEW_COOKIE_IS_NULL() {
        return messageFactory.getMessage("new.cookie.is.null");
    }

    /**
     * New cookie is null.
     * 
     */
    public static String NEW_COOKIE_IS_NULL() {
        return localizer.localize(localizableNEW_COOKIE_IS_NULL());
    }

    public static Localizable localizableINJECTION_ERROR_LOCAL_CLASS_NOT_SUPPORTED(Object arg0) {
        return messageFactory.getMessage("injection.error.local.class.not.supported", arg0);
    }

    /**
     * Cannot instantiate {0} class. Instantiation of local classes is not supported.
     * 
     */
    public static String INJECTION_ERROR_LOCAL_CLASS_NOT_SUPPORTED(Object arg0) {
        return localizer.localize(localizableINJECTION_ERROR_LOCAL_CLASS_NOT_SUPPORTED(arg0));
    }

    public static Localizable localizableSSL_TS_PROVIDERS_NOT_REGISTERED() {
        return messageFactory.getMessage("ssl.ts.providers.not.registered");
    }

    /**
     * Error initializing trust store (provider not registered).
     * 
     */
    public static String SSL_TS_PROVIDERS_NOT_REGISTERED() {
        return localizer.localize(localizableSSL_TS_PROVIDERS_NOT_REGISTERED());
    }

    public static Localizable localizableINJECTION_ERROR_NONSTATIC_MEMBER_CLASS_NOT_SUPPORTED(Object arg0) {
        return messageFactory.getMessage("injection.error.nonstatic.member.class.not.supported", arg0);
    }

    /**
     * Cannot instantiate {0} class. Instantiation of non-static member classes is not supported.
     * 
     */
    public static String INJECTION_ERROR_NONSTATIC_MEMBER_CLASS_NOT_SUPPORTED(Object arg0) {
        return localizer.localize(localizableINJECTION_ERROR_NONSTATIC_MEMBER_CLASS_NOT_SUPPORTED(arg0));
    }

    public static Localizable localizableURI_BUILDER_SCHEME_PART_NULL() {
        return messageFactory.getMessage("uri.builder.scheme.part.null");
    }

    /**
     * Supplied scheme-specific part parameter is null.
     * 
     */
    public static String URI_BUILDER_SCHEME_PART_NULL() {
        return localizer.localize(localizableURI_BUILDER_SCHEME_PART_NULL());
    }

    public static Localizable localizableMATRIX_PARAM_NULL() {
        return messageFactory.getMessage("matrix.param.null");
    }

    /**
     * One or more of matrix value parameters are null.
     * 
     */
    public static String MATRIX_PARAM_NULL() {
        return localizer.localize(localizableMATRIX_PARAM_NULL());
    }

    public static Localizable localizableWARNINGS_DETECTED(Object arg0) {
        return messageFactory.getMessage("warnings.detected", arg0);
    }

    /**
     * The following warnings have been detected: {0}
     * 
     */
    public static String WARNINGS_DETECTED(Object arg0) {
        return localizer.localize(localizableWARNINGS_DETECTED(arg0));
    }

    public static Localizable localizableHINT_MSG(Object arg0) {
        return messageFactory.getMessage("hint.msg", arg0);
    }

    /**
     * HINT: {0}
     * 
     */
    public static String HINT_MSG(Object arg0) {
        return localizer.localize(localizableHINT_MSG(arg0));
    }

    public static Localizable localizableSSL_TS_LOAD_ERROR(Object arg0) {
        return messageFactory.getMessage("ssl.ts.load.error", arg0);
    }

    /**
     * Error loading trust store from file "{0}".
     * 
     */
    public static String SSL_TS_LOAD_ERROR(Object arg0) {
        return localizer.localize(localizableSSL_TS_LOAD_ERROR(arg0));
    }

    public static Localizable localizableERROR_PROVIDER_REGISTERED_WRONG_RUNTIME(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.provider.registered.wrong.runtime", arg0, arg1);
    }

    /**
     * A provider {0} registered in {1} runtime does not implement any provider interfaces applicable in the {1} runtime.
     * 
     */
    public static String ERROR_PROVIDER_REGISTERED_WRONG_RUNTIME(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_PROVIDER_REGISTERED_WRONG_RUNTIME(arg0, arg1));
    }

    public static Localizable localizableSSL_KMF_NO_PASSWORD_FOR_PROVIDER_BASED_KS() {
        return messageFactory.getMessage("ssl.kmf.no.password.for.provider.based.ks");
    }

    /**
     * "provider based"
     * 
     */
    public static String SSL_KMF_NO_PASSWORD_FOR_PROVIDER_BASED_KS() {
        return localizer.localize(localizableSSL_KMF_NO_PASSWORD_FOR_PROVIDER_BASED_KS());
    }

    public static Localizable localizableURI_PARSER_SCHEME_EXPECTED(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.parser.scheme.expected", arg0, arg1);
    }

    /**
     * Expected scheme name at index {0}: ''{1}''.
     * 
     */
    public static String URI_PARSER_SCHEME_EXPECTED(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_PARSER_SCHEME_EXPECTED(arg0, arg1));
    }

    public static Localizable localizableTHREAD_POOL_EXECUTOR_PROVIDER_CLOSED() {
        return messageFactory.getMessage("thread.pool.executor.provider.closed");
    }

    /**
     * Thread pool executor provider has been closed.
     * 
     */
    public static String THREAD_POOL_EXECUTOR_PROVIDER_CLOSED() {
        return localizer.localize(localizableTHREAD_POOL_EXECUTOR_PROVIDER_CLOSED());
    }

    public static Localizable localizableMBW_TRYING_TO_CLOSE_STREAM(Object arg0) {
        return messageFactory.getMessage("mbw.trying.to.close.stream", arg0);
    }

    /**
     * Message body writer ({0}) is trying to close the entity output stream. Not closing.
     * 
     */
    public static String MBW_TRYING_TO_CLOSE_STREAM(Object arg0) {
        return localizer.localize(localizableMBW_TRYING_TO_CLOSE_STREAM(arg0));
    }

    public static Localizable localizableCOMPONENT_CONTRACTS_EMPTY_OR_NULL(Object arg0) {
        return messageFactory.getMessage("component.contracts.empty.or.null", arg0);
    }

    /**
     * Attempt to register component of type {0} to null or empty array of contracts  is ignored.
     * 
     */
    public static String COMPONENT_CONTRACTS_EMPTY_OR_NULL(Object arg0) {
        return localizer.localize(localizableCOMPONENT_CONTRACTS_EMPTY_OR_NULL(arg0));
    }

    public static Localizable localizablePROVIDER_NOT_FOUND(Object arg0, Object arg1) {
        return messageFactory.getMessage("provider.not.found", arg0, arg1);
    }

    /**
     * The class {0} implementing the provider {1} is not found. The provider implementation is ignored.
     * 
     */
    public static String PROVIDER_NOT_FOUND(Object arg0, Object arg1) {
        return localizer.localize(localizablePROVIDER_NOT_FOUND(arg0, arg1));
    }

    public static Localizable localizableTOO_MANY_HEADER_VALUES(Object arg0, Object arg1) {
        return messageFactory.getMessage("too.many.header.values", arg0, arg1);
    }

    /**
     * Too many "{0}" header values: "{1}"
     * 
     */
    public static String TOO_MANY_HEADER_VALUES(Object arg0, Object arg1) {
        return localizer.localize(localizableTOO_MANY_HEADER_VALUES(arg0, arg1));
    }

    public static Localizable localizableCACHE_CONTROL_IS_NULL() {
        return messageFactory.getMessage("cache.control.is.null");
    }

    /**
     * Cache control is null.
     * 
     */
    public static String CACHE_CONTROL_IS_NULL() {
        return localizer.localize(localizableCACHE_CONTROL_IS_NULL());
    }

    public static Localizable localizableHTTP_HEADER_END_OF_HEADER() {
        return messageFactory.getMessage("http.header.end.of.header");
    }

    /**
     * End of header.
     * 
     */
    public static String HTTP_HEADER_END_OF_HEADER() {
        return localizer.localize(localizableHTTP_HEADER_END_OF_HEADER());
    }

    public static Localizable localizableUSING_SCHEDULER_PROVIDER(Object arg0, Object arg1) {
        return messageFactory.getMessage("using.scheduler.provider", arg0, arg1);
    }

    /**
     * Selected ScheduledExecutorServiceProvider implementation [{0}] to be used for injection of scheduler qualified by [{1}] annotation.
     * 
     */
    public static String USING_SCHEDULER_PROVIDER(Object arg0, Object arg1) {
        return localizer.localize(localizableUSING_SCHEDULER_PROVIDER(arg0, arg1));
    }

    public static Localizable localizableHTTP_HEADER_COMMENTS_NOT_ALLOWED() {
        return messageFactory.getMessage("http.header.comments.not.allowed");
    }

    /**
     * Comments are not allowed.
     * 
     */
    public static String HTTP_HEADER_COMMENTS_NOT_ALLOWED() {
        return localizer.localize(localizableHTTP_HEADER_COMMENTS_NOT_ALLOWED());
    }

    public static Localizable localizableCOMPONENT_CLASS_CANNOT_BE_NULL() {
        return messageFactory.getMessage("component.class.cannot.be.null");
    }

    /**
     * Registered component class cannot be null.
     * 
     */
    public static String COMPONENT_CLASS_CANNOT_BE_NULL() {
        return localizer.localize(localizableCOMPONENT_CLASS_CANNOT_BE_NULL());
    }

    public static Localizable localizableURI_BUILDER_SCHEMA_PART_OPAQUE() {
        return messageFactory.getMessage("uri.builder.schema.part.opaque");
    }

    /**
     * Schema specific part is opaque.
     * 
     */
    public static String URI_BUILDER_SCHEMA_PART_OPAQUE() {
        return localizer.localize(localizableURI_BUILDER_SCHEMA_PART_OPAQUE());
    }

    public static Localizable localizableNO_ERROR_PROCESSING_IN_SCOPE() {
        return messageFactory.getMessage("no.error.processing.in.scope");
    }

    /**
     * There is no error processing in scope.
     * 
     */
    public static String NO_ERROR_PROCESSING_IN_SCOPE() {
        return localizer.localize(localizableNO_ERROR_PROCESSING_IN_SCOPE());
    }

    public static Localizable localizableCONTRACT_NOT_SUPPORTED(Object arg0, Object arg1) {
        return messageFactory.getMessage("contract.not.supported", arg0, arg1);
    }

    /**
     * Contract {0} can not be registered for component {1}: Contract type not supported.
     * 
     */
    public static String CONTRACT_NOT_SUPPORTED(Object arg0, Object arg1) {
        return localizer.localize(localizableCONTRACT_NOT_SUPPORTED(arg0, arg1));
    }

    public static Localizable localizableINVALID_SPI_CLASSES(Object arg0, Object arg1) {
        return messageFactory.getMessage("invalid.spi.classes", arg0, arg1);
    }

    /**
     * Supplied provider class(es) do not implement the expected {0} SPI: [{1}]
     * 
     */
    public static String INVALID_SPI_CLASSES(Object arg0, Object arg1) {
        return localizer.localize(localizableINVALID_SPI_CLASSES(arg0, arg1));
    }

    public static Localizable localizablePROVIDER_COULD_NOT_BE_CREATED(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("provider.could.not.be.created", arg0, arg1, arg2);
    }

    /**
     * The class {0} implementing provider {1} could not be instantiated: {2}
     * 
     */
    public static String PROVIDER_COULD_NOT_BE_CREATED(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizablePROVIDER_COULD_NOT_BE_CREATED(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_NOTFOUND_MESSAGEBODYREADER(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.notfound.messagebodyreader", arg0, arg1, arg2);
    }

    /**
     * MessageBodyReader not found for media type={0}, type={1}, genericType={2}.
     * 
     */
    public static String ERROR_NOTFOUND_MESSAGEBODYREADER(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_NOTFOUND_MESSAGEBODYREADER(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_READER_INTERCEPTOR_CONTEXT(Object arg0) {
        return messageFactory.getMessage("error.service.locator.provider.instance.feature.reader.interceptor.context", arg0);
    }

    /**
     * Incorrect type of reader interceptor context instance {0}. Parameter must be a default Jersey ReaderInterceptorContext implementation.
     * 
     */
    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_READER_INTERCEPTOR_CONTEXT(Object arg0) {
        return localizer.localize(localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_READER_INTERCEPTOR_CONTEXT(arg0));
    }

    public static Localizable localizableUSING_EXECUTOR_PROVIDER(Object arg0, Object arg1) {
        return messageFactory.getMessage("using.executor.provider", arg0, arg1);
    }

    /**
     * Selected ExecutorServiceProvider implementation [{0}] to be used for injection of executor qualified by [{1}] annotation.
     * 
     */
    public static String USING_EXECUTOR_PROVIDER(Object arg0, Object arg1) {
        return localizer.localize(localizableUSING_EXECUTOR_PROVIDER(arg0, arg1));
    }

    public static Localizable localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_WRITER_INTERCEPTOR_CONTEXT(Object arg0) {
        return messageFactory.getMessage("error.service.locator.provider.instance.feature.writer.interceptor.context", arg0);
    }

    /**
     * Incorrect type of writer interceptor context instance {0}. Parameter must be a default Jersey WriterInterceptorContext implementation.
     * 
     */
    public static String ERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_WRITER_INTERCEPTOR_CONTEXT(Object arg0) {
        return localizer.localize(localizableERROR_SERVICE_LOCATOR_PROVIDER_INSTANCE_FEATURE_WRITER_INTERCEPTOR_CONTEXT(arg0));
    }

    public static Localizable localizableIGNORED_SCHEDULER_PROVIDERS(Object arg0, Object arg1) {
        return messageFactory.getMessage("ignored.scheduler.providers", arg0, arg1);
    }

    /**
     * Multiple ScheduledExecutorServiceProvider registrations found for qualifier annotation [{1}]. The following provider registrations will be ignored: [{0}]
     * 
     */
    public static String IGNORED_SCHEDULER_PROVIDERS(Object arg0, Object arg1) {
        return localizer.localize(localizableIGNORED_SCHEDULER_PROVIDERS(arg0, arg1));
    }

    public static Localizable localizableDEPENDENT_CLASS_OF_PROVIDER_NOT_FOUND(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("dependent.class.of.provider.not.found", arg0, arg1, arg2);
    }

    /**
     * A dependent class, {0}, of the class {1} implementing the provider {2} is not found. The provider implementation is ignored.
     * 
     */
    public static String DEPENDENT_CLASS_OF_PROVIDER_NOT_FOUND(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableDEPENDENT_CLASS_OF_PROVIDER_NOT_FOUND(arg0, arg1, arg2));
    }

    public static Localizable localizableHTTP_HEADER_NO_END_SEPARATOR(Object arg0) {
        return messageFactory.getMessage("http.header.no.end.separator", arg0);
    }

    /**
     * No end separator "{0}".
     * 
     */
    public static String HTTP_HEADER_NO_END_SEPARATOR(Object arg0) {
        return localizer.localize(localizableHTTP_HEADER_NO_END_SEPARATOR(arg0));
    }

    public static Localizable localizableSSL_KS_IMPL_NOT_FOUND() {
        return messageFactory.getMessage("ssl.ks.impl.not.found");
    }

    /**
     * Error initializing key store (implementation not available).
     * 
     */
    public static String SSL_KS_IMPL_NOT_FOUND() {
        return localizer.localize(localizableSSL_KS_IMPL_NOT_FOUND());
    }

    public static Localizable localizableERROR_PROVIDER_AND_RESOURCE_CONSTRAINED_TO_IGNORED(Object arg0) {
        return messageFactory.getMessage("error.provider.and.resource.constrainedTo.ignored", arg0);
    }

    /**
     * Due to constraint configuration problems the implementations of providers in the resource class {0} will be ignored. However, the resource class will be loaded and used as an resource.
     * 
     */
    public static String ERROR_PROVIDER_AND_RESOURCE_CONSTRAINED_TO_IGNORED(Object arg0) {
        return localizer.localize(localizableERROR_PROVIDER_AND_RESOURCE_CONSTRAINED_TO_IGNORED(arg0));
    }

    public static Localizable localizableINVALID_PORT() {
        return messageFactory.getMessage("invalid.port");
    }

    /**
     * Invalid port value.
     * 
     */
    public static String INVALID_PORT() {
        return localizer.localize(localizableINVALID_PORT());
    }

    public static Localizable localizableERROR_INTERCEPTOR_WRITER_PROCEED() {
        return messageFactory.getMessage("error.interceptor.writer.proceed");
    }

    /**
     * Last writer interceptor in the chain called the method proceed.
     * 
     */
    public static String ERROR_INTERCEPTOR_WRITER_PROCEED() {
        return localizer.localize(localizableERROR_INTERCEPTOR_WRITER_PROCEED());
    }

    public static Localizable localizableHTTP_HEADER_NO_CHARS_BETWEEN_SEPARATORS(Object arg0, Object arg1) {
        return messageFactory.getMessage("http.header.no.chars.between.separators", arg0, arg1);
    }

    /**
     * No characters between the separators "{0}" and "{1}".
     * 
     */
    public static String HTTP_HEADER_NO_CHARS_BETWEEN_SEPARATORS(Object arg0, Object arg1) {
        return localizer.localize(localizableHTTP_HEADER_NO_CHARS_BETWEEN_SEPARATORS(arg0, arg1));
    }

    public static Localizable localizableILLEGAL_LOAD_FACTOR(Object arg0) {
        return messageFactory.getMessage("illegal.load.factor", arg0);
    }

    /**
     * Illegal load factor: {0}.
     * 
     */
    public static String ILLEGAL_LOAD_FACTOR(Object arg0) {
        return localizer.localize(localizableILLEGAL_LOAD_FACTOR(arg0));
    }

    public static Localizable localizableSOME_HEADERS_NOT_SENT(Object arg0, Object arg1) {
        return messageFactory.getMessage("some.headers.not.sent", arg0, arg1);
    }

    /**
     * There are some request headers that have not been sent by connector [{0}]. Probably you added those headers in WriterInterceptor or MessageBodyWriter. That feature is not supported by the connector. Please, do not modify headers in WriterInterceptor or MessageBodyWriter or use default HttpUrlConnector instead.
     * Unsent header changes: {1}
     * 
     */
    public static String SOME_HEADERS_NOT_SENT(Object arg0, Object arg1) {
        return localizer.localize(localizableSOME_HEADERS_NOT_SENT(arg0, arg1));
    }

    public static Localizable localizableQUERY_PARAM_NULL() {
        return messageFactory.getMessage("query.param.null");
    }

    /**
     * One or more of query value parameters are null.
     * 
     */
    public static String QUERY_PARAM_NULL() {
        return localizer.localize(localizableQUERY_PARAM_NULL());
    }

    public static Localizable localizableILLEGAL_PROVIDER_CLASS_NAME(Object arg0) {
        return messageFactory.getMessage("illegal.provider.class.name", arg0);
    }

    /**
     * Illegal provider-class name: {0}.
     * 
     */
    public static String ILLEGAL_PROVIDER_CLASS_NAME(Object arg0) {
        return localizer.localize(localizableILLEGAL_PROVIDER_CLASS_NAME(arg0));
    }

    public static Localizable localizableSTREAM_PROVIDER_NULL() {
        return messageFactory.getMessage("stream.provider.null");
    }

    /**
     * Stream provider is not defined. It must be set before writing first bytes to the entity output stream.
     * 
     */
    public static String STREAM_PROVIDER_NULL() {
        return localizer.localize(localizableSTREAM_PROVIDER_NULL());
    }

    public static Localizable localizableSSL_TMF_PROVIDER_NOT_REGISTERED() {
        return messageFactory.getMessage("ssl.tmf.provider.not.registered");
    }

    /**
     * Error initializing trust manager factory (provider not registered).
     * 
     */
    public static String SSL_TMF_PROVIDER_NOT_REGISTERED() {
        return localizer.localize(localizableSSL_TMF_PROVIDER_NOT_REGISTERED());
    }

    public static Localizable localizableNO_CONTAINER_AVAILABLE() {
        return messageFactory.getMessage("no.container.available");
    }

    /**
     * No container available.
     * 
     */
    public static String NO_CONTAINER_AVAILABLE() {
        return localizer.localize(localizableNO_CONTAINER_AVAILABLE());
    }

    public static Localizable localizableHK_2_FAILURE_OUTSIDE_ERROR_SCOPE() {
        return messageFactory.getMessage("hk2.failure.outside.error.scope");
    }

    /**
     * HK2 failure has been detected in a code that does not run in an active Jersey Error scope.
     * 
     */
    public static String HK_2_FAILURE_OUTSIDE_ERROR_SCOPE() {
        return localizer.localize(localizableHK_2_FAILURE_OUTSIDE_ERROR_SCOPE());
    }

    public static Localizable localizableERROR_ENTITY_PROVIDER_BASICTYPES_CONSTRUCTOR(Object arg0) {
        return messageFactory.getMessage("error.entity.provider.basictypes.constructor", arg0);
    }

    /**
     * Error converting entity to {0} type by single String constructor.
     * 
     */
    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_CONSTRUCTOR(Object arg0) {
        return localizer.localize(localizableERROR_ENTITY_PROVIDER_BASICTYPES_CONSTRUCTOR(arg0));
    }

    public static Localizable localizableERROR_NOTFOUND_MESSAGEBODYWRITER(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.notfound.messagebodywriter", arg0, arg1, arg2);
    }

    /**
     * MessageBodyWriter not found for media type={0}, type={1}, genericType={2}.
     * 
     */
    public static String ERROR_NOTFOUND_MESSAGEBODYWRITER(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_NOTFOUND_MESSAGEBODYWRITER(arg0, arg1, arg2));
    }

    public static Localizable localizableCONTRACT_NOT_ASSIGNABLE(Object arg0, Object arg1) {
        return messageFactory.getMessage("contract.not.assignable", arg0, arg1);
    }

    /**
     * Contract {0} can not be registered for component {1}: Contract not assignable to component.
     * 
     */
    public static String CONTRACT_NOT_ASSIGNABLE(Object arg0, Object arg1) {
        return localizer.localize(localizableCONTRACT_NOT_ASSIGNABLE(arg0, arg1));
    }

    public static Localizable localizableSSL_TMF_ALGORITHM_NOT_SUPPORTED() {
        return messageFactory.getMessage("ssl.tmf.algorithm.not.supported");
    }

    /**
     * Error initializing trust manager factory (algorithm not supported).
     * 
     */
    public static String SSL_TMF_ALGORITHM_NOT_SUPPORTED() {
        return localizer.localize(localizableSSL_TMF_ALGORITHM_NOT_SUPPORTED());
    }

    public static Localizable localizableOSGI_REGISTRY_ERROR_OPENING_RESOURCE_STREAM(Object arg0) {
        return messageFactory.getMessage("osgi.registry.error.opening.resource.stream", arg0);
    }

    /**
     * Unable to open an input stream for resource {0}.
     * 
     */
    public static String OSGI_REGISTRY_ERROR_OPENING_RESOURCE_STREAM(Object arg0) {
        return localizer.localize(localizableOSGI_REGISTRY_ERROR_OPENING_RESOURCE_STREAM(arg0));
    }

    public static Localizable localizableMBR_TRYING_TO_CLOSE_STREAM(Object arg0) {
        return messageFactory.getMessage("mbr.trying.to.close.stream", arg0);
    }

    /**
     * Message body reader ({0}) is trying to close the entity input stream. Not closing.
     * 
     */
    public static String MBR_TRYING_TO_CLOSE_STREAM(Object arg0) {
        return localizer.localize(localizableMBR_TRYING_TO_CLOSE_STREAM(arg0));
    }

    public static Localizable localizableIGNORED_EXECUTOR_PROVIDERS(Object arg0, Object arg1) {
        return messageFactory.getMessage("ignored.executor.providers", arg0, arg1);
    }

    /**
     * Multiple ExecutorServiceProvider registrations found for qualifier annotation [{1}]. The following provider registrations will be ignored: [{0}]
     * 
     */
    public static String IGNORED_EXECUTOR_PROVIDERS(Object arg0, Object arg1) {
        return localizer.localize(localizableIGNORED_EXECUTOR_PROVIDERS(arg0, arg1));
    }

    public static Localizable localizableURI_PARSER_NOT_EXECUTED() {
        return messageFactory.getMessage("uri.parser.not.executed");
    }

    /**
     * The parser was not executed yet. Call the parse() method first.
     * 
     */
    public static String URI_PARSER_NOT_EXECUTED() {
        return localizer.localize(localizableURI_PARSER_NOT_EXECUTED());
    }

    public static Localizable localizableMESSAGE_CONTENT_BUFFERING_FAILED() {
        return messageFactory.getMessage("message.content.buffering.failed");
    }

    /**
     * Failed to buffer the message content input stream.
     * 
     */
    public static String MESSAGE_CONTENT_BUFFERING_FAILED() {
        return localizer.localize(localizableMESSAGE_CONTENT_BUFFERING_FAILED());
    }

    public static Localizable localizableRESPONSE_CLOSED() {
        return messageFactory.getMessage("response.closed");
    }

    /**
     * Response is closed.
     * 
     */
    public static String RESPONSE_CLOSED() {
        return localizer.localize(localizableRESPONSE_CLOSED());
    }

    public static Localizable localizableSSL_KS_LOAD_ERROR(Object arg0) {
        return messageFactory.getMessage("ssl.ks.load.error", arg0);
    }

    /**
     * Error loading key store from file "{0}".
     * 
     */
    public static String SSL_KS_LOAD_ERROR(Object arg0) {
        return localizer.localize(localizableSSL_KS_LOAD_ERROR(arg0));
    }

    public static Localizable localizableCOMMITTING_STREAM_ALREADY_INITIALIZED() {
        return messageFactory.getMessage("committing.stream.already.initialized");
    }

    /**
     * Stream provider has already been initialized.
     * 
     */
    public static String COMMITTING_STREAM_ALREADY_INITIALIZED() {
        return localizer.localize(localizableCOMMITTING_STREAM_ALREADY_INITIALIZED());
    }

    public static Localizable localizableERROR_ENTITY_PROVIDER_BASICTYPES_CHARACTER_MORECHARS() {
        return messageFactory.getMessage("error.entity.provider.basictypes.character.morechars");
    }

    /**
     * A single character expected in the entity input stream.
     * 
     */
    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_CHARACTER_MORECHARS() {
        return localizer.localize(localizableERROR_ENTITY_PROVIDER_BASICTYPES_CHARACTER_MORECHARS());
    }

    public static Localizable localizableERROR_ENTITY_STREAM_CLOSED() {
        return messageFactory.getMessage("error.entity.stream.closed");
    }

    /**
     * Entity input stream has already been closed.
     * 
     */
    public static String ERROR_ENTITY_STREAM_CLOSED() {
        return localizer.localize(localizableERROR_ENTITY_STREAM_CLOSED());
    }

    public static Localizable localizableMESSAGE_CONTENT_INPUT_STREAM_CLOSE_FAILED() {
        return messageFactory.getMessage("message.content.input.stream.close.failed");
    }

    /**
     * Error closing message content input stream.
     * 
     */
    public static String MESSAGE_CONTENT_INPUT_STREAM_CLOSE_FAILED() {
        return localizer.localize(localizableMESSAGE_CONTENT_INPUT_STREAM_CLOSE_FAILED());
    }

    public static Localizable localizableERROR_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.provider.constrainedTo.wrong.package", arg0, arg1);
    }

    /**
     * A registered provider {0} is constrained (via @ConstrainedTo) to {1} runtime but does not implement any provider interface usable in the runtime.
     * 
     */
    public static String ERROR_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(arg0, arg1));
    }

    public static Localizable localizableSSL_KS_PROVIDERS_NOT_REGISTERED() {
        return messageFactory.getMessage("ssl.ks.providers.not.registered");
    }

    /**
     * Error initializing key store (provider not registered).
     * 
     */
    public static String SSL_KS_PROVIDERS_NOT_REGISTERED() {
        return localizer.localize(localizableSSL_KS_PROVIDERS_NOT_REGISTERED());
    }

    public static Localizable localizablePROPERTIES_HELPER_GET_VALUE_NO_TRANSFORM(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("properties.helper.get.value.no.transform", arg0, arg1, arg2);
    }

    /**
     * There is no way how to transform value "{0}" [{1}] to type [{2}].
     * 
     */
    public static String PROPERTIES_HELPER_GET_VALUE_NO_TRANSFORM(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizablePROPERTIES_HELPER_GET_VALUE_NO_TRANSFORM(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_INVALID_SYNTAX_TERMINATED(Object arg0) {
        return messageFactory.getMessage("error.template.parser.invalid.syntax.terminated", arg0);
    }

    /**
     * Invalid syntax in the template "{0}". Check if a path parameter is terminated with a "}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_INVALID_SYNTAX_TERMINATED(Object arg0) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_INVALID_SYNTAX_TERMINATED(arg0));
    }

    public static Localizable localizableURI_BUILDER_URI_PART_FRAGMENT(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.builder.uri.part.fragment", arg0, arg1);
    }

    /**
     * Supplied scheme-specific URI part "{0}" contains URI Fragment component: {1}.
     * 
     */
    public static String URI_BUILDER_URI_PART_FRAGMENT(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_BUILDER_URI_PART_FRAGMENT(arg0, arg1));
    }

    public static Localizable localizableERROR_MBW_ISWRITABLE(Object arg0) {
        return messageFactory.getMessage("error.mbw.iswritable", arg0);
    }

    /**
     * MesssageBodyWriter {0} threw exception in isWritable - skipping.
     * 
     */
    public static String ERROR_MBW_ISWRITABLE(Object arg0) {
        return localizer.localize(localizableERROR_MBW_ISWRITABLE(arg0));
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

    public static Localizable localizableINVALID_HOST() {
        return messageFactory.getMessage("invalid.host");
    }

    /**
     * Invalid host name.
     * 
     */
    public static String INVALID_HOST() {
        return localizer.localize(localizableINVALID_HOST());
    }

    public static Localizable localizableDEPENDENT_CLASS_OF_PROVIDER_FORMAT_ERROR(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("dependent.class.of.provider.format.error", arg0, arg1, arg2);
    }

    /**
     * {0}. A dependent class of the class {1} implementing the provider {2} is malformed. The provider implementation is ignored. Check if the malformed class is part of a stubbed jar that used for compiling only.
     * 
     */
    public static String DEPENDENT_CLASS_OF_PROVIDER_FORMAT_ERROR(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableDEPENDENT_CLASS_OF_PROVIDER_FORMAT_ERROR(arg0, arg1, arg2));
    }

    public static Localizable localizableEXCEPTION_MAPPER_SUPPORTED_TYPE_UNKNOWN(Object arg0) {
        return messageFactory.getMessage("exception.mapper.supported.type.unknown", arg0);
    }

    /**
     * Unable to retrieve the supported exception type for a registered exception mapper service class "{0}".
     * 
     */
    public static String EXCEPTION_MAPPER_SUPPORTED_TYPE_UNKNOWN(Object arg0) {
        return localizer.localize(localizableEXCEPTION_MAPPER_SUPPORTED_TYPE_UNKNOWN(arg0));
    }

    public static Localizable localizableSSL_KMF_NO_PASSWORD_FOR_BYTE_BASED_KS() {
        return messageFactory.getMessage("ssl.kmf.no.password.for.byte.based.ks");
    }

    /**
     * "byte array based"
     * 
     */
    public static String SSL_KMF_NO_PASSWORD_FOR_BYTE_BASED_KS() {
        return localizer.localize(localizableSSL_KMF_NO_PASSWORD_FOR_BYTE_BASED_KS());
    }

    public static Localizable localizableTYPE_TO_CLASS_CONVERSION_NOT_SUPPORTED(Object arg0) {
        return messageFactory.getMessage("type.to.class.conversion.not.supported", arg0);
    }

    /**
     * Type-to-class conversion not supported for: {0}.
     * 
     */
    public static String TYPE_TO_CLASS_CONVERSION_NOT_SUPPORTED(Object arg0) {
        return localizer.localize(localizableTYPE_TO_CLASS_CONVERSION_NOT_SUPPORTED(arg0));
    }

    public static Localizable localizableFEATURE_HAS_ALREADY_BEEN_PROCESSED(Object arg0) {
        return messageFactory.getMessage("feature.has.already.been.processed", arg0);
    }

    /**
     * Feature [{0}] has already been processed.
     * 
     */
    public static String FEATURE_HAS_ALREADY_BEEN_PROCESSED(Object arg0) {
        return localizer.localize(localizableFEATURE_HAS_ALREADY_BEEN_PROCESSED(arg0));
    }

    public static Localizable localizableSSL_CTX_INIT_FAILED() {
        return messageFactory.getMessage("ssl.ctx.init.failed");
    }

    /**
     * Error initializing SSL context (operation failed).
     * 
     */
    public static String SSL_CTX_INIT_FAILED() {
        return localizer.localize(localizableSSL_CTX_INIT_FAILED());
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_INVALID_SYNTAX(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.template.parser.invalid.syntax", arg0, arg1, arg2);
    }

    /**
     * Invalid syntax for the expression "{0}" associated with the name "{1}" in a path template "{2}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_INVALID_SYNTAX(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_INVALID_SYNTAX(arg0, arg1, arg2));
    }

    public static Localizable localizableURI_BUILDER_SCHEME_PART_UNEXPECTED_COMPONENT(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.builder.scheme.part.unexpected.component", arg0, arg1);
    }

    /**
     * Supplied scheme-specific URI part "{0}" contains unexpected URI Scheme component: {1}.
     * 
     */
    public static String URI_BUILDER_SCHEME_PART_UNEXPECTED_COMPONENT(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_BUILDER_SCHEME_PART_UNEXPECTED_COMPONENT(arg0, arg1));
    }

    public static Localizable localizableSSL_TS_IMPL_NOT_FOUND() {
        return messageFactory.getMessage("ssl.ts.impl.not.found");
    }

    /**
     * Error initializing trust store (implementation not available).
     * 
     */
    public static String SSL_TS_IMPL_NOT_FOUND() {
        return localizer.localize(localizableSSL_TS_IMPL_NOT_FOUND());
    }

    public static Localizable localizableWARNING_MSG(Object arg0) {
        return messageFactory.getMessage("warning.msg", arg0);
    }

    /**
     * WARNING: {0}
     * 
     */
    public static String WARNING_MSG(Object arg0) {
        return localizer.localize(localizableWARNING_MSG(arg0));
    }

    public static Localizable localizableWARNING_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("warning.provider.constrainedTo.wrong.package", arg0, arg1, arg2, arg3);
    }

    /**
     * A registered provider {0} constrained (via @ConstrainedTo) to {1} runtime implements interface {2} which is only usable in a {3} runtime context.
     * 
     */
    public static String WARNING_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableWARNING_PROVIDER_CONSTRAINED_TO_WRONG_PACKAGE(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableHINTS_DETECTED(Object arg0) {
        return messageFactory.getMessage("hints.detected", arg0);
    }

    /**
     * The following hints have been detected: {0}
     * 
     */
    public static String HINTS_DETECTED(Object arg0) {
        return localizer.localize(localizableHINTS_DETECTED(arg0));
    }

    public static Localizable localizableHTTP_HEADER_UNBALANCED_COMMENTS() {
        return messageFactory.getMessage("http.header.unbalanced.comments");
    }

    /**
     * Unbalanced comments.
     * 
     */
    public static String HTTP_HEADER_UNBALANCED_COMMENTS() {
        return localizer.localize(localizableHTTP_HEADER_UNBALANCED_COMMENTS());
    }

    public static Localizable localizableURI_BUILDER_METHODNAME_NOT_SPECIFIED(Object arg0, Object arg1) {
        return messageFactory.getMessage("uri.builder.methodname.not.specified", arg0, arg1);
    }

    /**
     * The method named "{0}" is not specified by {1}.
     * 
     */
    public static String URI_BUILDER_METHODNAME_NOT_SPECIFIED(Object arg0, Object arg1) {
        return localizer.localize(localizableURI_BUILDER_METHODNAME_NOT_SPECIFIED(arg0, arg1));
    }

    public static Localizable localizableSSL_KMF_UNRECOVERABLE_KEY() {
        return messageFactory.getMessage("ssl.kmf.unrecoverable.key");
    }

    /**
     * Error initializing key manager factory (unrecoverable key).
     * 
     */
    public static String SSL_KMF_UNRECOVERABLE_KEY() {
        return localizer.localize(localizableSSL_KMF_UNRECOVERABLE_KEY());
    }

    public static Localizable localizableINJECTION_ERROR_SUITABLE_CONSTRUCTOR_NOT_FOUND(Object arg0) {
        return messageFactory.getMessage("injection.error.suitable.constructor.not.found", arg0);
    }

    /**
     * Could not find a suitable constructor in {0} class.
     * 
     */
    public static String INJECTION_ERROR_SUITABLE_CONSTRUCTOR_NOT_FOUND(Object arg0) {
        return localizer.localize(localizableINJECTION_ERROR_SUITABLE_CONSTRUCTOR_NOT_FOUND(arg0));
    }

    public static Localizable localizableAUTODISCOVERABLE_CONFIGURATION_FAILED(Object arg0) {
        return messageFactory.getMessage("autodiscoverable.configuration.failed", arg0);
    }

    /**
     * Configuration of {0} auto-discoverable failed.
     * 
     */
    public static String AUTODISCOVERABLE_CONFIGURATION_FAILED(Object arg0) {
        return localizer.localize(localizableAUTODISCOVERABLE_CONFIGURATION_FAILED(arg0));
    }

    public static Localizable localizableURI_COMPONENT_INVALID_CHARACTER(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("uri.component.invalid.character", arg0, arg1, arg2, arg3);
    }

    /**
     * The string "{0}" for the URI component {1} contains an invalid character, ''{2}'', at index {3}.
     * 
     */
    public static String URI_COMPONENT_INVALID_CHARACTER(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableURI_COMPONENT_INVALID_CHARACTER(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableSSL_KS_FILE_NOT_FOUND(Object arg0) {
        return messageFactory.getMessage("ssl.ks.file.not.found", arg0);
    }

    /**
     * Cannot find key store file "{0}".
     * 
     */
    public static String SSL_KS_FILE_NOT_FOUND(Object arg0) {
        return localizer.localize(localizableSSL_KS_FILE_NOT_FOUND(arg0));
    }

    public static Localizable localizableEXCEPTION_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return messageFactory.getMessage("exception.caught.while.loading.spi.providers");
    }

    /**
     * Exception caught while loading SPI providers.
     * 
     */
    public static String EXCEPTION_CAUGHT_WHILE_LOADING_SPI_PROVIDERS() {
        return localizer.localize(localizableEXCEPTION_CAUGHT_WHILE_LOADING_SPI_PROVIDERS());
    }

    public static Localizable localizableERROR_MSG(Object arg0) {
        return messageFactory.getMessage("error.msg", arg0);
    }

    /**
     * WARNING: {0}
     * 
     */
    public static String ERROR_MSG(Object arg0) {
        return localizer.localize(localizableERROR_MSG(arg0));
    }

    public static Localizable localizableURI_IS_NULL() {
        return messageFactory.getMessage("uri.is.null");
    }

    /**
     * Uri is null.
     * 
     */
    public static String URI_IS_NULL() {
        return localizer.localize(localizableURI_IS_NULL());
    }

    public static Localizable localizableOSGI_REGISTRY_ERROR_PROCESSING_RESOURCE_STREAM(Object arg0) {
        return messageFactory.getMessage("osgi.registry.error.processing.resource.stream", arg0);
    }

    /**
     * Unexpected error occurred while processing resource stream {0}.
     * 
     */
    public static String OSGI_REGISTRY_ERROR_PROCESSING_RESOURCE_STREAM(Object arg0) {
        return localizer.localize(localizableOSGI_REGISTRY_ERROR_PROCESSING_RESOURCE_STREAM(arg0));
    }

    public static Localizable localizablePROVIDER_CLASS_COULD_NOT_BE_LOADED(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("provider.class.could.not.be.loaded", arg0, arg1, arg2);
    }

    /**
     * The class {0} implementing provider {1} could not be loaded: {2}
     * 
     */
    public static String PROVIDER_CLASS_COULD_NOT_BE_LOADED(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizablePROVIDER_CLASS_COULD_NOT_BE_LOADED(arg0, arg1, arg2));
    }

    public static Localizable localizableCOMPONENT_TYPE_ALREADY_REGISTERED(Object arg0) {
        return messageFactory.getMessage("component.type.already.registered", arg0);
    }

    /**
     * Cannot create new registration for component type {0}: Existing previous registration found for the type.
     * 
     */
    public static String COMPONENT_TYPE_ALREADY_REGISTERED(Object arg0) {
        return localizer.localize(localizableCOMPONENT_TYPE_ALREADY_REGISTERED(arg0));
    }

    public static Localizable localizableERROR_ENTITY_PROVIDER_BASICTYPES_UNKWNOWN(Object arg0) {
        return messageFactory.getMessage("error.entity.provider.basictypes.unkwnown", arg0);
    }

    /**
     * Unsupported basic type {0}.
     * 
     */
    public static String ERROR_ENTITY_PROVIDER_BASICTYPES_UNKWNOWN(Object arg0) {
        return localizer.localize(localizableERROR_ENTITY_PROVIDER_BASICTYPES_UNKWNOWN(arg0));
    }

    public static Localizable localizableSTRING_IS_NULL() {
        return messageFactory.getMessage("string.is.null");
    }

    /**
     * String is null.
     * 
     */
    public static String STRING_IS_NULL() {
        return localizer.localize(localizableSTRING_IS_NULL());
    }

    public static Localizable localizableDATE_IS_NULL() {
        return messageFactory.getMessage("date.is.null");
    }

    /**
     * Date is null.
     * 
     */
    public static String DATE_IS_NULL() {
        return localizer.localize(localizableDATE_IS_NULL());
    }

    public static Localizable localizableERROR_RESOLVING_GENERIC_TYPE_VALUE(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.resolving.generic.type.value", arg0, arg1);
    }

    /**
     * Unable to resolve generic type value of {0} for an instance of {1}.
     * 
     */
    public static String ERROR_RESOLVING_GENERIC_TYPE_VALUE(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_RESOLVING_GENERIC_TYPE_VALUE(arg0, arg1));
    }

    public static Localizable localizableERROR_TEMPLATE_PARSER_NAME_MORE_THAN_ONCE(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.template.parser.name.more.than.once", arg0, arg1);
    }

    /**
     * The name "{0}" is declared more than once with different regular expressions in a path template "{1}".
     * 
     */
    public static String ERROR_TEMPLATE_PARSER_NAME_MORE_THAN_ONCE(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_TEMPLATE_PARSER_NAME_MORE_THAN_ONCE(arg0, arg1));
    }

    public static Localizable localizableSSL_TS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return messageFactory.getMessage("ssl.ts.integrity.algorithm.not.found");
    }

    /**
     * Error initializing trust store (algorithm to check trust store integrity not found).
     * 
     */
    public static String SSL_TS_INTEGRITY_ALGORITHM_NOT_FOUND() {
        return localizer.localize(localizableSSL_TS_INTEGRITY_ALGORITHM_NOT_FOUND());
    }

}
