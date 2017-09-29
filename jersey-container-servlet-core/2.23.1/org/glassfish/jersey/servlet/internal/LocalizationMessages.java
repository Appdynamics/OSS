
package org.glassfish.jersey.servlet.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.servlet.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizableHEADER_VALUE_READ_FAILED() {
        return messageFactory.getMessage("header.value.read.failed");
    }

    /**
     * Attempt to read the header value failed.
     * 
     */
    public static String HEADER_VALUE_READ_FAILED() {
        return localizer.localize(localizableHEADER_VALUE_READ_FAILED());
    }

    public static Localizable localizableFORM_PARAM_CONSUMED(Object arg0) {
        return messageFactory.getMessage("form.param.consumed", arg0);
    }

    /**
     * A servlet request to the URI {0} contains form parameters in the request body but the request body has been consumed by the servlet or a servlet filter accessing the request parameters. Only resource methods using @FormParam will work as expected. Resource methods consuming the request body by other means will not work as expected.
     * 
     */
    public static String FORM_PARAM_CONSUMED(Object arg0) {
        return localizer.localize(localizableFORM_PARAM_CONSUMED(arg0));
    }

    public static Localizable localizableFILTER_CONTEXT_PATH_MISSING() {
        return messageFactory.getMessage("filter.context.path.missing");
    }

    /**
     * The root of the app was not properly defined. Either use a Servlet 3.x container or add an init-param 'jersey.config.servlet.filter.contextPath' to the filter configuration. Due to Servlet 2.x API, Jersey cannot determine the request base URI solely from the ServletContext. The application will most likely not work.
     * 
     */
    public static String FILTER_CONTEXT_PATH_MISSING() {
        return localizer.localize(localizableFILTER_CONTEXT_PATH_MISSING());
    }

    public static Localizable localizableSERVLET_PATH_MISMATCH(Object arg0, Object arg1) {
        return messageFactory.getMessage("servlet.path.mismatch", arg0, arg1);
    }

    /**
     * The servlet path {0} does not start with the filter context path {1}.
     * 
     */
    public static String SERVLET_PATH_MISMATCH(Object arg0, Object arg1) {
        return localizer.localize(localizableSERVLET_PATH_MISMATCH(arg0, arg1));
    }

    public static Localizable localizableASYNC_PROCESSING_NOT_SUPPORTED() {
        return messageFactory.getMessage("async.processing.not.supported");
    }

    /**
     * Asynchronous processing not supported on Servlet 2.x container.
     * 
     */
    public static String ASYNC_PROCESSING_NOT_SUPPORTED() {
        return localizer.localize(localizableASYNC_PROCESSING_NOT_SUPPORTED());
    }

    public static Localizable localizableSERVLET_REQUEST_SUSPEND_FAILED() {
        return messageFactory.getMessage("servlet.request.suspend.failed");
    }

    /**
     * Attempt to put servlet request into asynchronous mode has failed. Please check your servlet configuration - all Servlet instances and Servlet filters involved in the request processing must explicitly declare support for asynchronous request processing.
     * 
     */
    public static String SERVLET_REQUEST_SUSPEND_FAILED() {
        return localizer.localize(localizableSERVLET_REQUEST_SUSPEND_FAILED());
    }

    public static Localizable localizableEXCEPTION_SENDING_ERROR_RESPONSE(Object arg0, Object arg1) {
        return messageFactory.getMessage("exception.sending.error.response", arg0, arg1);
    }

    /**
     * I/O exception occurred while sending "{0}/{1}" error response.
     * 
     */
    public static String EXCEPTION_SENDING_ERROR_RESPONSE(Object arg0, Object arg1) {
        return localizer.localize(localizableEXCEPTION_SENDING_ERROR_RESPONSE(arg0, arg1));
    }

    public static Localizable localizableNO_THREAD_LOCAL_VALUE(Object arg0) {
        return messageFactory.getMessage("no.thread.local.value", arg0);
    }

    /**
     * No thread local value in scope for proxy of {0}.
     * 
     */
    public static String NO_THREAD_LOCAL_VALUE(Object arg0) {
        return localizer.localize(localizableNO_THREAD_LOCAL_VALUE(arg0));
    }

    public static Localizable localizableINIT_PARAM_REGEX_SYNTAX_INVALID(Object arg0, Object arg1) {
        return messageFactory.getMessage("init.param.regex.syntax.invalid", arg0, arg1);
    }

    /**
     * The syntax is invalid for the regular expression "{0}" associated with the initialization parameter "{1}".
     * 
     */
    public static String INIT_PARAM_REGEX_SYNTAX_INVALID(Object arg0, Object arg1) {
        return localizer.localize(localizableINIT_PARAM_REGEX_SYNTAX_INVALID(arg0, arg1));
    }

    public static Localizable localizableRESOURCE_CONFIG_UNABLE_TO_LOAD(Object arg0) {
        return messageFactory.getMessage("resource.config.unable.to.load", arg0);
    }

    /**
     * Resource configuration class {0} could not be loaded.
     * 
     */
    public static String RESOURCE_CONFIG_UNABLE_TO_LOAD(Object arg0) {
        return localizer.localize(localizableRESOURCE_CONFIG_UNABLE_TO_LOAD(arg0));
    }

    public static Localizable localizablePERSISTENCE_UNIT_NOT_CONFIGURED(Object arg0) {
        return messageFactory.getMessage("persistence.unit.not.configured", arg0);
    }

    /**
     * Persistence unit "{0}" is not configured as a servlet parameter in web.xml.
     * 
     */
    public static String PERSISTENCE_UNIT_NOT_CONFIGURED(Object arg0) {
        return localizer.localize(localizablePERSISTENCE_UNIT_NOT_CONFIGURED(arg0));
    }

    public static Localizable localizableRESOURCE_CONFIG_PARENT_CLASS_INVALID(Object arg0, Object arg1) {
        return messageFactory.getMessage("resource.config.parent.class.invalid", arg0, arg1);
    }

    /**
     * Resource configuration class {0} is not a subclass of {1}.
     * 
     */
    public static String RESOURCE_CONFIG_PARENT_CLASS_INVALID(Object arg0, Object arg1) {
        return localizer.localize(localizableRESOURCE_CONFIG_PARENT_CLASS_INVALID(arg0, arg1));
    }

}
