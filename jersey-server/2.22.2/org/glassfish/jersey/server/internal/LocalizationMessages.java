
package org.glassfish.jersey.server.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.server.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizableSUBRES_LOC_CACHE_INVALID_SIZE(Object arg0, Object arg1) {
        return messageFactory.getMessage("subres.loc.cache.invalid.size", arg0, arg1);
    }

    /**
     * Invalid value for sub-resource locator cache size: {0}. Using default value: {1}.
     * 
     */
    public static String SUBRES_LOC_CACHE_INVALID_SIZE(Object arg0, Object arg1) {
        return localizer.localize(localizableSUBRES_LOC_CACHE_INVALID_SIZE(arg0, arg1));
    }

    public static Localizable localizableLOGGING_GLOBAL_REQUEST_FILTERS() {
        return messageFactory.getMessage("logging.global.request.filters");
    }

    /**
     * Global Request Filters
     * 
     */
    public static String LOGGING_GLOBAL_REQUEST_FILTERS() {
        return localizer.localize(localizableLOGGING_GLOBAL_REQUEST_FILTERS());
    }

    public static Localizable localizableERROR_PROCESSING_METHOD(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.processing.method", arg0, arg1);
    }

    /**
     * Error processing resource method, {0}, for ResourceMethodDispatchProvider, {1}.
     * 
     */
    public static String ERROR_PROCESSING_METHOD(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_PROCESSING_METHOD(arg0, arg1));
    }

    public static Localizable localizableCOLLECTION_EXTRACTOR_TYPE_UNSUPPORTED() {
        return messageFactory.getMessage("collection.extractor.type.unsupported");
    }

    /**
     * Unsupported collection type.
     * 
     */
    public static String COLLECTION_EXTRACTOR_TYPE_UNSUPPORTED() {
        return localizer.localize(localizableCOLLECTION_EXTRACTOR_TYPE_UNSUPPORTED());
    }

    public static Localizable localizableLOGGING_ROOT_RESOURCE_CLASSES() {
        return messageFactory.getMessage("logging.root.resource.classes");
    }

    /**
     * Root Resource Classes
     * 
     */
    public static String LOGGING_ROOT_RESOURCE_CLASSES() {
        return localizer.localize(localizableLOGGING_ROOT_RESOURCE_CLASSES());
    }

    public static Localizable localizableERROR_UNMARSHALLING_JAXB(Object arg0) {
        return messageFactory.getMessage("error.unmarshalling.jaxb", arg0);
    }

    /**
     * Error unmarshalling JAXB object of type "{0}".
     * 
     */
    public static String ERROR_UNMARSHALLING_JAXB(Object arg0) {
        return localizer.localize(localizableERROR_UNMARSHALLING_JAXB(arg0));
    }

    public static Localizable localizableJAR_SCANNER_UNABLE_TO_CLOSE_FILE() {
        return messageFactory.getMessage("jar.scanner.unable.to.close.file");
    }

    /**
     * Unable to close Jar file.
     * 
     */
    public static String JAR_SCANNER_UNABLE_TO_CLOSE_FILE() {
        return localizer.localize(localizableJAR_SCANNER_UNABLE_TO_CLOSE_FILE());
    }

    public static Localizable localizableERROR_WADL_JAXB_CONTEXT() {
        return messageFactory.getMessage("error.wadl.jaxb.context");
    }

    /**
     * Error creating a JAXBContext for wadl processing.
     * 
     */
    public static String ERROR_WADL_JAXB_CONTEXT() {
        return localizer.localize(localizableERROR_WADL_JAXB_CONTEXT());
    }

    public static Localizable localizableERROR_WRITING_RESPONSE_ENTITY_CHUNK() {
        return messageFactory.getMessage("error.writing.response.entity.chunk");
    }

    /**
     * An I/O error has occurred while writing a response message entity chunk to the container output stream.
     * 
     */
    public static String ERROR_WRITING_RESPONSE_ENTITY_CHUNK() {
        return localizer.localize(localizableERROR_WRITING_RESPONSE_ENTITY_CHUNK());
    }

    public static Localizable localizableSUBRES_LOC_RETURNS_VOID(Object arg0) {
        return messageFactory.getMessage("subres.loc.returns.void", arg0);
    }

    /**
     * A sub-resource model, {0}, MUST return a non-void type.
     * 
     */
    public static String SUBRES_LOC_RETURNS_VOID(Object arg0) {
        return localizer.localize(localizableSUBRES_LOC_RETURNS_VOID(arg0));
    }

    public static Localizable localizableERROR_WADL_GENERATOR_CONFIGURE() {
        return messageFactory.getMessage("error.wadl.generator.configure");
    }

    /**
     * Error configuring the given user WadlGenerator by configure() method.
     * 
     */
    public static String ERROR_WADL_GENERATOR_CONFIGURE() {
        return localizer.localize(localizableERROR_WADL_GENERATOR_CONFIGURE());
    }

    public static Localizable localizableNON_INSTANTIABLE_COMPONENT(Object arg0) {
        return messageFactory.getMessage("non.instantiable.component", arg0);
    }

    /**
     * Component of class {0} cannot be instantiated and will be ignored.
     * 
     */
    public static String NON_INSTANTIABLE_COMPONENT(Object arg0) {
        return localizer.localize(localizableNON_INSTANTIABLE_COMPONENT(arg0));
    }

    public static Localizable localizableCHUNKED_OUTPUT_CLOSED() {
        return messageFactory.getMessage("chunked.output.closed");
    }

    /**
     * This chunked output has been closed.
     * 
     */
    public static String CHUNKED_OUTPUT_CLOSED() {
        return localizer.localize(localizableCHUNKED_OUTPUT_CLOSED());
    }

    public static Localizable localizableERROR_ASYNC_CALLBACK_FAILED(Object arg0) {
        return messageFactory.getMessage("error.async.callback.failed", arg0);
    }

    /**
     * Callback {0} invocation failed.
     * 
     */
    public static String ERROR_ASYNC_CALLBACK_FAILED(Object arg0) {
        return localizer.localize(localizableERROR_ASYNC_CALLBACK_FAILED(arg0));
    }

    public static Localizable localizableWARNING_MONITORING_MBEANS_BEAN_ALREADY_REGISTERED(Object arg0) {
        return messageFactory.getMessage("warning.monitoring.mbeans.bean.already.registered", arg0);
    }

    /**
     * Monitoring MBeans "{0}" is already registered. Un-registering the current mbean and registering a new one instead.
     * 
     */
    public static String WARNING_MONITORING_MBEANS_BEAN_ALREADY_REGISTERED(Object arg0) {
        return localizer.localize(localizableWARNING_MONITORING_MBEANS_BEAN_ALREADY_REGISTERED(arg0));
    }

    public static Localizable localizableERROR_EXCEPTION_MAPPING_PROCESSED_RESPONSE_ERROR() {
        return messageFactory.getMessage("error.exception.mapping.processed.response.error");
    }

    /**
     * A response error mapping did not successfully produce and processed a response.
     * 
     */
    public static String ERROR_EXCEPTION_MAPPING_PROCESSED_RESPONSE_ERROR() {
        return localizer.localize(localizableERROR_EXCEPTION_MAPPING_PROCESSED_RESPONSE_ERROR());
    }

    public static Localizable localizableCLEARING_HK_2_CACHE(Object arg0, Object arg1) {
        return messageFactory.getMessage("clearing.hk2.cache", arg0, arg1);
    }

    /**
     * Clearing Jersey HK2 caches. Service cache size: {0}, reflection cache size: {1}.
     * 
     */
    public static String CLEARING_HK_2_CACHE(Object arg0, Object arg1) {
        return localizer.localize(localizableCLEARING_HK_2_CACHE(arg0, arg1));
    }

    public static Localizable localizableINVALID_MAPPING_TYPE(Object arg0) {
        return messageFactory.getMessage("invalid.mapping.type", arg0);
    }

    /**
     * Provided {0} property value type is invalid. Acceptable types are String and String[].
     * 
     */
    public static String INVALID_MAPPING_TYPE(Object arg0) {
        return localizer.localize(localizableINVALID_MAPPING_TYPE(arg0));
    }

    public static Localizable localizablePREMATCHING_ALSO_NAME_BOUND(Object arg0) {
        return messageFactory.getMessage("prematching.also.name.bound", arg0);
    }

    /**
     * @PreMatching provider, {0}, also annotated with a name binding annotation. Name binding will be ignored.
     * 
     */
    public static String PREMATCHING_ALSO_NAME_BOUND(Object arg0) {
        return localizer.localize(localizablePREMATCHING_ALSO_NAME_BOUND(arg0));
    }

    public static Localizable localizableERROR_WADL_RESOURCE_EXTERNAL_GRAMMAR() {
        return messageFactory.getMessage("error.wadl.resource.external.grammar");
    }

    /**
     * Error generating external wadl grammar on /application.wadl/[path].
     * 
     */
    public static String ERROR_WADL_RESOURCE_EXTERNAL_GRAMMAR() {
        return localizer.localize(localizableERROR_WADL_RESOURCE_EXTERNAL_GRAMMAR());
    }

    public static Localizable localizableERROR_WADL_EXTERNAL_GRAMMAR() {
        return messageFactory.getMessage("error.wadl.external.grammar");
    }

    /**
     * Error attaching an external grammar to the wadl.
     * 
     */
    public static String ERROR_WADL_EXTERNAL_GRAMMAR() {
        return localizer.localize(localizableERROR_WADL_EXTERNAL_GRAMMAR());
    }

    public static Localizable localizableRESOURCE_ADD_CHILD_ALREADY_CHILD() {
        return messageFactory.getMessage("resource.add.child.already.child");
    }

    /**
     * The resource is already a child resource and cannot contain another child resource.
     * 
     */
    public static String RESOURCE_ADD_CHILD_ALREADY_CHILD() {
        return localizer.localize(localizableRESOURCE_ADD_CHILD_ALREADY_CHILD());
    }

    public static Localizable localizableRESOURCE_CONFIG_ERROR_NULL_APPLICATIONCLASS() {
        return messageFactory.getMessage("resource.config.error.null.applicationclass");
    }

    /**
     * Both application and applicationClass can't be null.
     * 
     */
    public static String RESOURCE_CONFIG_ERROR_NULL_APPLICATIONCLASS() {
        return localizer.localize(localizableRESOURCE_CONFIG_ERROR_NULL_APPLICATIONCLASS());
    }

    public static Localizable localizableRESOURCE_MULTIPLE_SCOPE_ANNOTATIONS(Object arg0) {
        return messageFactory.getMessage("resource.multiple.scope.annotations", arg0);
    }

    /**
     * A resource, {0}, is annotated with multiple scope annotations. Only one scope annotation is allowed for the resource.
     * 
     */
    public static String RESOURCE_MULTIPLE_SCOPE_ANNOTATIONS(Object arg0) {
        return localizer.localize(localizableRESOURCE_MULTIPLE_SCOPE_ANNOTATIONS(arg0));
    }

    public static Localizable localizableEXCEPTION_MAPPING_START() {
        return messageFactory.getMessage("exception.mapping.start");
    }

    /**
     * Starting mapping of the exception.
     * 
     */
    public static String EXCEPTION_MAPPING_START() {
        return localizer.localize(localizableEXCEPTION_MAPPING_START());
    }

    public static Localizable localizableLOGGING_GLOBAL_READER_INTERCEPTORS() {
        return messageFactory.getMessage("logging.global.reader.interceptors");
    }

    /**
     * Global Reader Interceptors
     * 
     */
    public static String LOGGING_GLOBAL_READER_INTERCEPTORS() {
        return localizer.localize(localizableLOGGING_GLOBAL_READER_INTERCEPTORS());
    }

    public static Localizable localizableRESOURCE_LOOKUP_FAILED(Object arg0) {
        return messageFactory.getMessage("resource.lookup.failed", arg0);
    }

    /**
     * Lookup and initialization failed for a resource class: {0}.
     * 
     */
    public static String RESOURCE_LOOKUP_FAILED(Object arg0) {
        return localizer.localize(localizableRESOURCE_LOOKUP_FAILED(arg0));
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_RESOURCE_LOCATOR(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.builder.generation.resource.locator", arg0, arg1);
    }

    /**
     * Error generating wadl for sub resource locator {0} of resource {1}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_RESOURCE_LOCATOR(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_RESOURCE_LOCATOR(arg0, arg1));
    }

    public static Localizable localizableERROR_REQUEST_SET_SECURITY_CONTEXT_IN_RESPONSE_PHASE() {
        return messageFactory.getMessage("error.request.set.security.context.in.response.phase");
    }

    /**
     * The security context cannot be set in the request as it is already in the response processing phase.
     * 
     */
    public static String ERROR_REQUEST_SET_SECURITY_CONTEXT_IN_RESPONSE_PHASE() {
        return localizer.localize(localizableERROR_REQUEST_SET_SECURITY_CONTEXT_IN_RESPONSE_PHASE());
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

    public static Localizable localizableEXCEPTION_MAPPING_WAE_ENTITY(Object arg0) {
        return messageFactory.getMessage("exception.mapping.wae.entity", arg0);
    }

    /**
     * WebApplicationException (WAE) with non-null entity thrown. Response with status {0} is directly generated from the WAE.
     * 
     */
    public static String EXCEPTION_MAPPING_WAE_ENTITY(Object arg0) {
        return localizer.localize(localizableEXCEPTION_MAPPING_WAE_ENTITY(arg0));
    }

    public static Localizable localizableILLEGAL_CLIENT_CONFIG_CLASS_PROPERTY_VALUE(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("illegal.client.config.class.property.value", arg0, arg1, arg2);
    }

    /**
     * "{0}" property value ({1}) does not represent a valid client configuration class. Falling back to "{2}".
     * 
     */
    public static String ILLEGAL_CLIENT_CONFIG_CLASS_PROPERTY_VALUE(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableILLEGAL_CLIENT_CONFIG_CLASS_PROPERTY_VALUE(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_PROCESSING_RESPONSE_FROM_ALREADY_MAPPED_EXCEPTION() {
        return messageFactory.getMessage("error.processing.response.from.already.mapped.exception");
    }

    /**
     * Error occurred when processing a response created from an already mapped exception.
     * 
     */
    public static String ERROR_PROCESSING_RESPONSE_FROM_ALREADY_MAPPED_EXCEPTION() {
        return localizer.localize(localizableERROR_PROCESSING_RESPONSE_FROM_ALREADY_MAPPED_EXCEPTION());
    }

    public static Localizable localizableLOGGING_MESSAGE_BODY_READERS() {
        return messageFactory.getMessage("logging.message.body.readers");
    }

    /**
     * Message Body Readers
     * 
     */
    public static String LOGGING_MESSAGE_BODY_READERS() {
        return localizer.localize(localizableLOGGING_MESSAGE_BODY_READERS());
    }

    public static Localizable localizableERROR_SUSPENDING_ASYNC_REQUEST() {
        return messageFactory.getMessage("error.suspending.async.request");
    }

    /**
     * Attempt to suspend a connection of an asynchronous request failed in the underlying container.
     * 
     */
    public static String ERROR_SUSPENDING_ASYNC_REQUEST() {
        return localizer.localize(localizableERROR_SUSPENDING_ASYNC_REQUEST());
    }

    public static Localizable localizableERROR_SUSPENDING_CHUNKED_OUTPUT_RESPONSE() {
        return messageFactory.getMessage("error.suspending.chunked.output.response");
    }

    /**
     * Attempt to suspend a client connection associated with a chunked output has failed in the underlying container.
     * 
     */
    public static String ERROR_SUSPENDING_CHUNKED_OUTPUT_RESPONSE() {
        return localizer.localize(localizableERROR_SUSPENDING_CHUNKED_OUTPUT_RESPONSE());
    }

    public static Localizable localizableAMBIGUOUS_PARAMETER(Object arg0, Object arg1) {
        return messageFactory.getMessage("ambiguous.parameter", arg0, arg1);
    }

    /**
     * Parameter {1} of {0} MUST be only one of a path, query, matrix or header parameter.
     * 
     */
    public static String AMBIGUOUS_PARAMETER(Object arg0, Object arg1) {
        return localizer.localize(localizableAMBIGUOUS_PARAMETER(arg0, arg1));
    }

    public static Localizable localizableERROR_VALIDATION_SUBRESOURCE() {
        return messageFactory.getMessage("error.validation.subresource");
    }

    /**
     * Model validation error(s) found in sub resource returned by sub resource locator.
     * 
     */
    public static String ERROR_VALIDATION_SUBRESOURCE() {
        return localizer.localize(localizableERROR_VALIDATION_SUBRESOURCE());
    }

    public static Localizable localizableAMBIGUOUS_RMS_OUT(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("ambiguous.rms.out", arg0, arg1, arg2, arg3);
    }

    /**
     * A resource model has ambiguous (sub-)resource method for HTTP method {0} and output mime-types as defined by @Produces annotation at Java methods {1} and {2} at matching path pattern {3}.
     * 
     */
    public static String AMBIGUOUS_RMS_OUT(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableAMBIGUOUS_RMS_OUT(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableERROR_SUB_RESOURCE_LOCATOR_MORE_RESOURCES(Object arg0) {
        return messageFactory.getMessage("error.sub.resource.locator.more.resources", arg0);
    }

    /**
     * Sub resource locator returned {0} in the resource model. Exactly one resource must be returned.
     * 
     */
    public static String ERROR_SUB_RESOURCE_LOCATOR_MORE_RESOURCES(Object arg0) {
        return localizer.localize(localizableERROR_SUB_RESOURCE_LOCATOR_MORE_RESOURCES(arg0));
    }

    public static Localizable localizableAMBIGUOUS_RMS_IN(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("ambiguous.rms.in", arg0, arg1, arg2, arg3);
    }

    /**
     * A resource model has ambiguous (sub-)resource method for HTTP method {0} and input mime-types as defined by "@Consumes" annotation at Java methods {1} and {2} at matching path pattern {3}. This could cause an error for conflicting output types!
     * 
     */
    public static String AMBIGUOUS_RMS_IN(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableAMBIGUOUS_RMS_IN(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableERROR_MONITORING_QUEUE_RESPONSE() {
        return messageFactory.getMessage("error.monitoring.queue.response");
    }

    /**
     * Failed to add the monitoring event into the Response Statuses Queue - queue is full. One of the registered MonitoringStatisticsListeners might be blocking the event processing.
     * 
     */
    public static String ERROR_MONITORING_QUEUE_RESPONSE() {
        return localizer.localize(localizableERROR_MONITORING_QUEUE_RESPONSE());
    }

    public static Localizable localizableINVALID_MAPPING_VALUE_EMPTY(Object arg0, Object arg1) {
        return messageFactory.getMessage("invalid.mapping.value.empty", arg0, arg1);
    }

    /**
     * The value in {0} mappings record "{1}" is empty.
     * 
     */
    public static String INVALID_MAPPING_VALUE_EMPTY(Object arg0, Object arg1) {
        return localizer.localize(localizableINVALID_MAPPING_VALUE_EMPTY(arg0, arg1));
    }

    public static Localizable localizableERROR_MONITORING_SCHEDULER_DESTROY_TIMEOUT() {
        return messageFactory.getMessage("error.monitoring.scheduler.destroy.timeout");
    }

    /**
     * Waiting for shutdown of MonitoringStatisticsProcessor has timed out! Possible cause can be that any registered MonitoringStatisticsListener takes too long to execute.
     * 
     */
    public static String ERROR_MONITORING_SCHEDULER_DESTROY_TIMEOUT() {
        return localizer.localize(localizableERROR_MONITORING_SCHEDULER_DESTROY_TIMEOUT());
    }

    public static Localizable localizableNON_PUB_RES_METHOD(Object arg0) {
        return messageFactory.getMessage("non.pub.res.method", arg0);
    }

    /**
     * A resource method, {0}, MUST be public scoped otherwise the method is ignored
     * 
     */
    public static String NON_PUB_RES_METHOD(Object arg0) {
        return localizer.localize(localizableNON_PUB_RES_METHOD(arg0));
    }

    public static Localizable localizableERROR_MONITORING_STATISTICS_LISTENER(Object arg0) {
        return messageFactory.getMessage("error.monitoring.statistics.listener", arg0);
    }

    /**
     * Exception thrown when provider {0} was processing MonitoringStatistics. Removing provider from further processing.
     * 
     */
    public static String ERROR_MONITORING_STATISTICS_LISTENER(Object arg0) {
        return localizer.localize(localizableERROR_MONITORING_STATISTICS_LISTENER(arg0));
    }

    public static Localizable localizableAMBIGUOUS_RESOURCE_METHOD(Object arg0) {
        return messageFactory.getMessage("ambiguous.resource.method", arg0);
    }

    /**
     * Multiple methods found to suit client request with accept header {0}. Selected the first method from the following list.
     * 
     */
    public static String AMBIGUOUS_RESOURCE_METHOD(Object arg0) {
        return localizer.localize(localizableAMBIGUOUS_RESOURCE_METHOD(arg0));
    }

    public static Localizable localizableCLOSEABLE_INJECTED_REQUEST_CONTEXT_NULL(Object arg0) {
        return messageFactory.getMessage("closeable.injected.request.context.null", arg0);
    }

    /**
     * Injected request context is 'null' on thread {0}.
     * 
     */
    public static String CLOSEABLE_INJECTED_REQUEST_CONTEXT_NULL(Object arg0) {
        return localizer.localize(localizableCLOSEABLE_INJECTED_REQUEST_CONTEXT_NULL(arg0));
    }

    public static Localizable localizableSUSPEND_SCHEDULING_ERROR() {
        return messageFactory.getMessage("suspend.scheduling.error");
    }

    /**
     * Error while scheduling a timeout task.
     * 
     */
    public static String SUSPEND_SCHEDULING_ERROR() {
        return localizer.localize(localizableSUSPEND_SCHEDULING_ERROR());
    }

    public static Localizable localizableERROR_WADL_RESOURCE_APPLICATION_WADL() {
        return messageFactory.getMessage("error.wadl.resource.application.wadl");
    }

    /**
     * Error generating /application.wadl.
     * 
     */
    public static String ERROR_WADL_RESOURCE_APPLICATION_WADL() {
        return localizer.localize(localizableERROR_WADL_RESOURCE_APPLICATION_WADL());
    }

    public static Localizable localizableWADL_DOC_EXTENDED_WADL(Object arg0, Object arg1) {
        return messageFactory.getMessage("wadl.doc.extended.wadl", arg0, arg1);
    }

    /**
     * This is full WADL including extended resources. To get simplified WADL with users resources only do not use the query parameter {0}. Link: {1}
     * 
     */
    public static String WADL_DOC_EXTENDED_WADL(Object arg0, Object arg1) {
        return localizer.localize(localizableWADL_DOC_EXTENDED_WADL(arg0, arg1));
    }

    public static Localizable localizableJAR_SCANNER_UNABLE_TO_READ_ENTRY() {
        return messageFactory.getMessage("jar.scanner.unable.to.read.entry");
    }

    /**
     * Unable to read the next Jar entry.
     * 
     */
    public static String JAR_SCANNER_UNABLE_TO_READ_ENTRY() {
        return localizer.localize(localizableJAR_SCANNER_UNABLE_TO_READ_ENTRY());
    }

    public static Localizable localizableINIT_MSG(Object arg0) {
        return messageFactory.getMessage("init.msg", arg0);
    }

    /**
     * Initiating Jersey application, version {0}...
     * 
     */
    public static String INIT_MSG(Object arg0) {
        return localizer.localize(localizableINIT_MSG(arg0));
    }

    public static Localizable localizableRESOURCE_CONFIG_UNABLE_TO_PROCESS(Object arg0) {
        return messageFactory.getMessage("resource.config.unable.to.process", arg0);
    }

    /**
     * Unable to process {0}
     * 
     */
    public static String RESOURCE_CONFIG_UNABLE_TO_PROCESS(Object arg0) {
        return localizer.localize(localizableRESOURCE_CONFIG_UNABLE_TO_PROCESS(arg0));
    }

    public static Localizable localizableSUBRES_LOC_HAS_ENTITY_PARAM(Object arg0) {
        return messageFactory.getMessage("subres.loc.has.entity.param", arg0);
    }

    /**
     * A sub-resource model, {0}, can not have an entity parameter. Try to move the parameter to the corresponding resource method.
     * 
     */
    public static String SUBRES_LOC_HAS_ENTITY_PARAM(Object arg0) {
        return localizer.localize(localizableSUBRES_LOC_HAS_ENTITY_PARAM(arg0));
    }

    public static Localizable localizableWADL_RESOURCEDOC_AMBIGUOUS_METHOD_ENTRIES(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("wadl.resourcedoc.ambiguous.method.entries", arg0, arg1, arg2, arg3);
    }

    /**
     * Ambiguous resource documentation detected: Unique resource method documentation cannot be found for method %{0}.%{1}%{2}. Selecting the first available method documentation element from the list of {3} possible candidates."
     * 
     */
    public static String WADL_RESOURCEDOC_AMBIGUOUS_METHOD_ENTRIES(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableWADL_RESOURCEDOC_AMBIGUOUS_METHOD_ENTRIES(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableSUSPEND_HANDLER_EXECUTION_FAILED() {
        return messageFactory.getMessage("suspend.handler.execution.failed");
    }

    /**
     * Time-out handler execution failed.
     * 
     */
    public static String SUSPEND_HANDLER_EXECUTION_FAILED() {
        return localizer.localize(localizableSUSPEND_HANDLER_EXECUTION_FAILED());
    }

    public static Localizable localizableERROR_UNSUPPORTED_ENCODING(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.unsupported.encoding", arg0, arg1);
    }

    /**
     * The encoding {0} is not supported for parameter {1}.
     * 
     */
    public static String ERROR_UNSUPPORTED_ENCODING(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_UNSUPPORTED_ENCODING(arg0, arg1));
    }

    public static Localizable localizableEXCEPTION_MAPPER_FAILED_FOR_EXCEPTION() {
        return messageFactory.getMessage("exception.mapper.failed.for.exception");
    }

    /**
     * An exception was not mapped due to exception mapper failure. The HTTP 500 response will be returned.
     * 
     */
    public static String EXCEPTION_MAPPER_FAILED_FOR_EXCEPTION() {
        return localizer.localize(localizableEXCEPTION_MAPPER_FAILED_FOR_EXCEPTION());
    }

    public static Localizable localizableERROR_MONITORING_QUEUE_APP() {
        return messageFactory.getMessage("error.monitoring.queue.app");
    }

    /**
     * Failed to add the monitoring event into the Application Event Queue - queue is full. One of the registered MonitoringStatisticsListeners might be blocking the event processing.
     * 
     */
    public static String ERROR_MONITORING_QUEUE_APP() {
        return localizer.localize(localizableERROR_MONITORING_QUEUE_APP());
    }

    public static Localizable localizableSINGLETON_INJECTS_PARAMETER(Object arg0, Object arg1) {
        return messageFactory.getMessage("singleton.injects.parameter", arg0, arg1);
    }

    /**
     * Parameter {1} of {0} cannot be injected into singleton resource.
     * 
     */
    public static String SINGLETON_INJECTS_PARAMETER(Object arg0, Object arg1) {
        return localizer.localize(localizableSINGLETON_INJECTS_PARAMETER(arg0, arg1));
    }

    public static Localizable localizableRESOURCE_REPLACED_CHILD_DOES_NOT_EXIST(Object arg0) {
        return messageFactory.getMessage("resource.replaced.child.does.not.exist", arg0);
    }

    /**
     * Replaced child resource does not exist in model: {0}.
     * 
     */
    public static String RESOURCE_REPLACED_CHILD_DOES_NOT_EXIST(Object arg0) {
        return localizer.localize(localizableRESOURCE_REPLACED_CHILD_DOES_NOT_EXIST(arg0));
    }

    public static Localizable localizableCONTRACT_CANNOT_BE_BOUND_TO_RESOURCE_METHOD(Object arg0, Object arg1) {
        return messageFactory.getMessage("contract.cannot.be.bound.to.resource.method", arg0, arg1);
    }

    /**
     * The given contract ({0}) of {1} provider cannot be bound to a resource method.
     * 
     */
    public static String CONTRACT_CANNOT_BE_BOUND_TO_RESOURCE_METHOD(Object arg0, Object arg1) {
        return localizer.localize(localizableCONTRACT_CANNOT_BE_BOUND_TO_RESOURCE_METHOD(arg0, arg1));
    }

    public static Localizable localizableERROR_MONITORING_MBEANS_UNREGISTRATION_DESTROY() {
        return messageFactory.getMessage("error.monitoring.mbeans.unregistration.destroy");
    }

    /**
     * Error un-registering Jersey monitoring MBeans on application destroy.
     * 
     */
    public static String ERROR_MONITORING_MBEANS_UNREGISTRATION_DESTROY() {
        return localizer.localize(localizableERROR_MONITORING_MBEANS_UNREGISTRATION_DESTROY());
    }

    public static Localizable localizableUNSUPPORTED_URI_INJECTION_TYPE(Object arg0) {
        return messageFactory.getMessage("unsupported.uri.injection.type", arg0);
    }

    /**
     * "@Uri"-based injection of "{0}" type is not supported.
     * 
     */
    public static String UNSUPPORTED_URI_INJECTION_TYPE(Object arg0) {
        return localizer.localize(localizableUNSUPPORTED_URI_INJECTION_TYPE(arg0));
    }

    public static Localizable localizableERROR_MONITORING_STATISTICS_GENERATION() {
        return messageFactory.getMessage("error.monitoring.statistics.generation");
    }

    /**
     * Error generating monitoring statistics.
     * 
     */
    public static String ERROR_MONITORING_STATISTICS_GENERATION() {
        return localizer.localize(localizableERROR_MONITORING_STATISTICS_GENERATION());
    }

    public static Localizable localizableINVALID_MAPPING_KEY_EMPTY(Object arg0, Object arg1) {
        return messageFactory.getMessage("invalid.mapping.key.empty", arg0, arg1);
    }

    /**
     * The key in {0} mappings record "{1}" is empty.
     * 
     */
    public static String INVALID_MAPPING_KEY_EMPTY(Object arg0, Object arg1) {
        return localizer.localize(localizableINVALID_MAPPING_KEY_EMPTY(arg0, arg1));
    }

    public static Localizable localizableERROR_RESOURCE_JAVA_METHOD_INVOCATION() {
        return messageFactory.getMessage("error.resource.java.method.invocation");
    }

    /**
     * Resource Java method invocation error.
     * 
     */
    public static String ERROR_RESOURCE_JAVA_METHOD_INVOCATION() {
        return localizer.localize(localizableERROR_RESOURCE_JAVA_METHOD_INVOCATION());
    }

    public static Localizable localizableERROR_SCANNING_CLASS_NOT_FOUND(Object arg0) {
        return messageFactory.getMessage("error.scanning.class.not.found", arg0);
    }

    /**
     * A class file of the class name, {0}, is identified but the class could not be found.
     * 
     */
    public static String ERROR_SCANNING_CLASS_NOT_FOUND(Object arg0) {
        return localizer.localize(localizableERROR_SCANNING_CLASS_NOT_FOUND(arg0));
    }

    public static Localizable localizableGET_RETURNS_VOID(Object arg0) {
        return messageFactory.getMessage("get.returns.void", arg0);
    }

    /**
     * A HTTP GET method, {0}, returns a void type. It can be intentional and perfectly fine, but it is a little uncommon that GET method returns always "204 No Content".
     * 
     */
    public static String GET_RETURNS_VOID(Object arg0) {
        return localizer.localize(localizableGET_RETURNS_VOID(arg0));
    }

    public static Localizable localizableERROR_PRIMITIVE_TYPE_NULL() {
        return messageFactory.getMessage("error.primitive.type.null");
    }

    /**
     * The request entity cannot be empty.
     * 
     */
    public static String ERROR_PRIMITIVE_TYPE_NULL() {
        return localizer.localize(localizableERROR_PRIMITIVE_TYPE_NULL());
    }

    public static Localizable localizableWARNING_MONITORING_FEATURE_ENABLED(Object arg0) {
        return messageFactory.getMessage("warning.monitoring.feature.enabled", arg0);
    }

    /**
     * MonitoringFeature is registered but the configuration property "{0}" (enabling basic monitoring statistics) is FALSE. Monitoring statistics will be disabled. The configuration is inconsistent and may produce unwanted behaviour. Disable MBeans exposure or enable monitoring statistics.
     * 
     */
    public static String WARNING_MONITORING_FEATURE_ENABLED(Object arg0) {
        return localizer.localize(localizableWARNING_MONITORING_FEATURE_ENABLED(arg0));
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_RESOURCE_PATH(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.builder.generation.resource.path", arg0, arg1);
    }

    /**
     * Error generating wadl for Resource {0} with path "{1}".
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_RESOURCE_PATH(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_RESOURCE_PATH(arg0, arg1));
    }

    public static Localizable localizableERROR_CLOSING_FINDER(Object arg0) {
        return messageFactory.getMessage("error.closing.finder", arg0);
    }

    /**
     * Error while closing {0} resource finder.
     * 
     */
    public static String ERROR_CLOSING_FINDER(Object arg0) {
        return localizer.localize(localizableERROR_CLOSING_FINDER(arg0));
    }

    public static Localizable localizableLOGGING_NAME_BOUND_REQUEST_FILTERS() {
        return messageFactory.getMessage("logging.name.bound.request.filters");
    }

    /**
     * Name-bound Request Filters
     * 
     */
    public static String LOGGING_NAME_BOUND_REQUEST_FILTERS() {
        return localizer.localize(localizableLOGGING_NAME_BOUND_REQUEST_FILTERS());
    }

    public static Localizable localizableEXCEPTION_MAPPER_THROWS_EXCEPTION(Object arg0) {
        return messageFactory.getMessage("exception.mapper.throws.exception", arg0);
    }

    /**
     * An exception has been thrown from an exception mapper {0}.
     * 
     */
    public static String EXCEPTION_MAPPER_THROWS_EXCEPTION(Object arg0) {
        return localizer.localize(localizableEXCEPTION_MAPPER_THROWS_EXCEPTION(arg0));
    }

    public static Localizable localizableCALLBACK_ARRAY_NULL() {
        return messageFactory.getMessage("callback.array.null");
    }

    /**
     * Additional array of callbacks is null.
     * 
     */
    public static String CALLBACK_ARRAY_NULL() {
        return localizer.localize(localizableCALLBACK_ARRAY_NULL());
    }

    public static Localizable localizableERROR_PARAMETER_INVALID_CHAR_VALUE(Object arg0) {
        return messageFactory.getMessage("error.parameter.invalid.char.value", arg0);
    }

    /**
     * Value "{0}" is not a character.
     * 
     */
    public static String ERROR_PARAMETER_INVALID_CHAR_VALUE(Object arg0) {
        return localizer.localize(localizableERROR_PARAMETER_INVALID_CHAR_VALUE(arg0));
    }

    public static Localizable localizableERROR_REQUEST_SET_ENTITY_STREAM_IN_RESPONSE_PHASE() {
        return messageFactory.getMessage("error.request.set.entity.stream.in.response.phase");
    }

    /**
     * The entity stream cannot be set in the request as it is already in the response processing phase.
     * 
     */
    public static String ERROR_REQUEST_SET_ENTITY_STREAM_IN_RESPONSE_PHASE() {
        return localizer.localize(localizableERROR_REQUEST_SET_ENTITY_STREAM_IN_RESPONSE_PHASE());
    }

    public static Localizable localizableRESOURCE_UPDATED_METHOD_DOES_NOT_EXIST(Object arg0) {
        return messageFactory.getMessage("resource.updated.method.does.not.exist", arg0);
    }

    /**
     * Updated resource method does not exist in the model: {0}.
     * 
     */
    public static String RESOURCE_UPDATED_METHOD_DOES_NOT_EXIST(Object arg0) {
        return localizer.localize(localizableRESOURCE_UPDATED_METHOD_DOES_NOT_EXIST(arg0));
    }

    public static Localizable localizableRESOURCE_CONTAINS_RES_METHODS_AND_LOCATOR(Object arg0, Object arg1) {
        return messageFactory.getMessage("resource.contains.res.methods.and.locator", arg0, arg1);
    }

    /**
     * The resource (or sub resource) {0} with path "{1}" contains (sub) resource method(s) and sub resource locator. The resource cannot have both, methods and locator, defined on same path. The locator will be ignored.
     * 
     */
    public static String RESOURCE_CONTAINS_RES_METHODS_AND_LOCATOR(Object arg0, Object arg1) {
        return localizer.localize(localizableRESOURCE_CONTAINS_RES_METHODS_AND_LOCATOR(arg0, arg1));
    }

    public static Localizable localizableINJECTED_WEBTARGET_URI_INVALID(Object arg0) {
        return messageFactory.getMessage("injected.webtarget.uri.invalid", arg0);
    }

    /**
     * "@Uri" annotation value is not a valid URI template: "{0}"
     * 
     */
    public static String INJECTED_WEBTARGET_URI_INVALID(Object arg0) {
        return localizer.localize(localizableINJECTED_WEBTARGET_URI_INVALID(arg0));
    }

    public static Localizable localizableRESOURCE_AMBIGUOUS(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("resource.ambiguous", arg0, arg1, arg2);
    }

    /**
     * A resource, {0}, has ambiguous path definition with resource {1}. Both resources match to the same path pattern {2}.
     * 
     */
    public static String RESOURCE_AMBIGUOUS(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableRESOURCE_AMBIGUOUS(arg0, arg1, arg2));
    }

    public static Localizable localizableAMBIGUOUS_SRLS(Object arg0, Object arg1) {
        return messageFactory.getMessage("ambiguous.srls", arg0, arg1);
    }

    /**
     * A resource, {0}, has ambiguous sub-resource locators on path {1}.
     * 
     */
    public static String AMBIGUOUS_SRLS(Object arg0, Object arg1) {
        return localizer.localize(localizableAMBIGUOUS_SRLS(arg0, arg1));
    }

    public static Localizable localizableLOGGING_NAME_BOUND_RESPONSE_FILTERS() {
        return messageFactory.getMessage("logging.name.bound.response.filters");
    }

    /**
     * Name-bound Response Filters
     * 
     */
    public static String LOGGING_NAME_BOUND_RESPONSE_FILTERS() {
        return localizer.localize(localizableLOGGING_NAME_BOUND_RESPONSE_FILTERS());
    }

    public static Localizable localizableERROR_COMMITTING_OUTPUT_STREAM() {
        return messageFactory.getMessage("error.committing.output.stream");
    }

    /**
     * Error while committing the output stream.
     * 
     */
    public static String ERROR_COMMITTING_OUTPUT_STREAM() {
        return localizer.localize(localizableERROR_COMMITTING_OUTPUT_STREAM());
    }

    public static Localizable localizableLOGGING_NAME_BOUND_READER_INTERCEPTORS() {
        return messageFactory.getMessage("logging.name.bound.reader.interceptors");
    }

    /**
     * Name-bound Reader Interceptors
     * 
     */
    public static String LOGGING_NAME_BOUND_READER_INTERCEPTORS() {
        return localizer.localize(localizableLOGGING_NAME_BOUND_READER_INTERCEPTORS());
    }

    public static Localizable localizableLOGGING_PRE_MATCH_FILTERS() {
        return messageFactory.getMessage("logging.pre.match.filters");
    }

    /**
     * Pre-match Filters
     * 
     */
    public static String LOGGING_PRE_MATCH_FILTERS() {
        return localizer.localize(localizableLOGGING_PRE_MATCH_FILTERS());
    }

    public static Localizable localizableERROR_EXCEPTION_MAPPING_THROWN_TO_CONTAINER() {
        return messageFactory.getMessage("error.exception.mapping.thrown.to.container");
    }

    /**
     * An exception mapping did not successfully produce and processed a response. Logging the exception propagated to the container.
     * 
     */
    public static String ERROR_EXCEPTION_MAPPING_THROWN_TO_CONTAINER() {
        return localizer.localize(localizableERROR_EXCEPTION_MAPPING_THROWN_TO_CONTAINER());
    }

    public static Localizable localizableERROR_MARSHALLING_JAXB(Object arg0) {
        return messageFactory.getMessage("error.marshalling.jaxb", arg0);
    }

    /**
     * Error marshalling JAXB object of type "{0}".
     * 
     */
    public static String ERROR_MARSHALLING_JAXB(Object arg0) {
        return localizer.localize(localizableERROR_MARSHALLING_JAXB(arg0));
    }

    public static Localizable localizableGET_CONSUMES_ENTITY(Object arg0) {
        return messageFactory.getMessage("get.consumes.entity", arg0);
    }

    /**
     * A HTTP GET method, {0}, should not consume any entity.
     * 
     */
    public static String GET_CONSUMES_ENTITY(Object arg0) {
        return localizer.localize(localizableGET_CONSUMES_ENTITY(arg0));
    }

    public static Localizable localizableRESOURCE_EMPTY(Object arg0, Object arg1) {
        return messageFactory.getMessage("resource.empty", arg0, arg1);
    }

    /**
     * A resource, {0}, with path "{1}" is empty. It has no resource (or sub resource) methods neither sub resource locators defined.
     * 
     */
    public static String RESOURCE_EMPTY(Object arg0, Object arg1) {
        return localizer.localize(localizableRESOURCE_EMPTY(arg0, arg1));
    }

    public static Localizable localizableFORM_PARAM_METHOD_ERROR() {
        return messageFactory.getMessage("form.param.method.error");
    }

    /**
     * The "@FormParam" is utilized when the request method is GET
     * 
     */
    public static String FORM_PARAM_METHOD_ERROR() {
        return localizer.localize(localizableFORM_PARAM_METHOD_ERROR());
    }

    public static Localizable localizablePARAMETER_UNRESOLVABLE(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("parameter.unresolvable", arg0, arg1, arg2);
    }

    /**
     * Parameter {0} of type {1} from {2} is not resolvable to a concrete type.
     * 
     */
    public static String PARAMETER_UNRESOLVABLE(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizablePARAMETER_UNRESOLVABLE(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_RESOURCE(Object arg0) {
        return messageFactory.getMessage("error.wadl.builder.generation.resource", arg0);
    }

    /**
     * Error generating wadl application for one specific resource {0}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_RESOURCE(Object arg0) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_RESOURCE(arg0));
    }

    public static Localizable localizableGET_CONSUMES_FORM_PARAM(Object arg0) {
        return messageFactory.getMessage("get.consumes.form.param", arg0);
    }

    /**
     * A HTTP GET method, {0}, should not consume any form parameter.
     * 
     */
    public static String GET_CONSUMES_FORM_PARAM(Object arg0) {
        return localizer.localize(localizableGET_CONSUMES_FORM_PARAM(arg0));
    }

    public static Localizable localizableUNABLE_TO_LOAD_CLASS(Object arg0) {
        return messageFactory.getMessage("unable.to.load.class", arg0);
    }

    /**
     * Class "{0}" cannot be loaded.
     * 
     */
    public static String UNABLE_TO_LOAD_CLASS(Object arg0) {
        return localizer.localize(localizableUNABLE_TO_LOAD_CLASS(arg0));
    }

    public static Localizable localizableTYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(Object arg0, Object arg1) {
        return messageFactory.getMessage("type.of.method.not.resolvable.to.concrete.type", arg0, arg1);
    }

    /**
     * Return type, {0}, of method, {1}, is not resolvable to a concrete type.
     * 
     */
    public static String TYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(Object arg0, Object arg1) {
        return localizer.localize(localizableTYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(arg0, arg1));
    }

    public static Localizable localizableLOGGING_APPLICATION_INITIALIZED() {
        return messageFactory.getMessage("logging.application.initialized");
    }

    /**
     * Jersey application initialized.
     * 
     */
    public static String LOGGING_APPLICATION_INITIALIZED() {
        return localizer.localize(localizableLOGGING_APPLICATION_INITIALIZED());
    }

    public static Localizable localizableNON_PUB_SUB_RES_METHOD(Object arg0) {
        return messageFactory.getMessage("non.pub.sub.res.method", arg0);
    }

    /**
     * A sub-resource method, {0}, MUST be public scoped otherwise the method is ignored
     * 
     */
    public static String NON_PUB_SUB_RES_METHOD(Object arg0) {
        return localizer.localize(localizableNON_PUB_SUB_RES_METHOD(arg0));
    }

    public static Localizable localizableERROR_WRITING_RESPONSE_ENTITY() {
        return messageFactory.getMessage("error.writing.response.entity");
    }

    /**
     * An I/O error has occurred while writing a response message entity to the container output stream.
     * 
     */
    public static String ERROR_WRITING_RESPONSE_ENTITY() {
        return localizer.localize(localizableERROR_WRITING_RESPONSE_ENTITY());
    }

    public static Localizable localizableERROR_PARAMETER_TYPE_PROCESSING(Object arg0) {
        return messageFactory.getMessage("error.parameter.type.processing", arg0);
    }

    /**
     * Could not process parameter type {0}.
     * 
     */
    public static String ERROR_PARAMETER_TYPE_PROCESSING(Object arg0) {
        return localizer.localize(localizableERROR_PARAMETER_TYPE_PROCESSING(arg0));
    }

    public static Localizable localizableERROR_MONITORING_QUEUE_REQUEST() {
        return messageFactory.getMessage("error.monitoring.queue.request");
    }

    /**
     * Failed to add the monitoring event into the Request Items Queue - queue is full. One of the registered MonitoringStatisticsListeners might be blocking the event processing.
     * 
     */
    public static String ERROR_MONITORING_QUEUE_REQUEST() {
        return localizer.localize(localizableERROR_MONITORING_QUEUE_REQUEST());
    }

    public static Localizable localizableWARNING_MONITORING_FEATURE_DISABLED(Object arg0) {
        return messageFactory.getMessage("warning.monitoring.feature.disabled", arg0);
    }

    /**
     * MonitoringFeature is registered but the configuration property "{0}" (enabling basic monitoring statistics) is FALSE. However, the feature is configured to enable exposure of monitoring MBeans (either by property or by direct instance setup), so the monitoring statistics will be enabled as this is prerequisite for Monitoring MBeans. The configuration is inconsistent and may produce unwanted behaviour. Unregister the feature or change the property value.
     * 
     */
    public static String WARNING_MONITORING_FEATURE_DISABLED(Object arg0) {
        return localizer.localize(localizableWARNING_MONITORING_FEATURE_DISABLED(arg0));
    }

    public static Localizable localizableINVALID_MAPPING_FORMAT(Object arg0, Object arg1) {
        return messageFactory.getMessage("invalid.mapping.format", arg0, arg1);
    }

    /**
     * Provided {0} property value "{1}" is invalid. It should contain two parts, key and value, separated by ':'.
     * 
     */
    public static String INVALID_MAPPING_FORMAT(Object arg0, Object arg1) {
        return localizer.localize(localizableINVALID_MAPPING_FORMAT(arg0, arg1));
    }

    public static Localizable localizableNON_PUB_SUB_RES_LOC(Object arg0) {
        return messageFactory.getMessage("non.pub.sub.res.loc", arg0);
    }

    /**
     * A sub-resource model, {0}, MUST be public scoped otherwise the method is ignored
     * 
     */
    public static String NON_PUB_SUB_RES_LOC(Object arg0) {
        return localizer.localize(localizableNON_PUB_SUB_RES_LOC(arg0));
    }

    public static Localizable localizableLOGGING_GLOBAL_WRITER_INTERCEPTORS() {
        return messageFactory.getMessage("logging.global.writer.interceptors");
    }

    /**
     * Global Writer Interceptors
     * 
     */
    public static String LOGGING_GLOBAL_WRITER_INTERCEPTORS() {
        return localizer.localize(localizableLOGGING_GLOBAL_WRITER_INTERCEPTORS());
    }

    public static Localizable localizableERROR_MONITORING_QUEUE_FLOODED(Object arg0) {
        return messageFactory.getMessage("error.monitoring.queue.flooded", arg0);
    }

    /**
     * A Monitoring Event Queue is being flooded. The monitoring statistics will show inaccurate measurements; especially in case of short time window statistics. The queue size is {0}.
     * 
     */
    public static String ERROR_MONITORING_QUEUE_FLOODED(Object arg0) {
        return localizer.localize(localizableERROR_MONITORING_QUEUE_FLOODED(arg0));
    }

    public static Localizable localizableMETHOD_PARAMETER_CANNOT_BE_NULL(Object arg0) {
        return messageFactory.getMessage("method.parameter.cannot.be.null", arg0);
    }

    /**
     * Method parameter "{0}" cannot be null.
     * 
     */
    public static String METHOD_PARAMETER_CANNOT_BE_NULL(Object arg0) {
        return localizer.localize(localizableMETHOD_PARAMETER_CANNOT_BE_NULL(arg0));
    }

    public static Localizable localizableLOGGING_MESSAGE_BODY_WRITERS() {
        return messageFactory.getMessage("logging.message.body.writers");
    }

    /**
     * Message Body Writers
     * 
     */
    public static String LOGGING_MESSAGE_BODY_WRITERS() {
        return localizer.localize(localizableLOGGING_MESSAGE_BODY_WRITERS());
    }

    public static Localizable localizableAMBIGUOUS_FATAL_RMS(Object arg0, Object arg1, Object arg2, Object arg3) {
        return messageFactory.getMessage("ambiguous.fatal.rms", arg0, arg1, arg2, arg3);
    }

    /**
     * A resource model has ambiguous (sub-)resource method for HTTP method {0} and input mime-types as defined by"@Consumes" and "@Produces" annotations at Java methods {1} and {2} at matching regular expression {3}. These two methods produces and consumes exactly the same mime-types and therefore their invocation as a resource methods will always fail.
     * 
     */
    public static String AMBIGUOUS_FATAL_RMS(Object arg0, Object arg1, Object arg2, Object arg3) {
        return localizer.localize(localizableAMBIGUOUS_FATAL_RMS(arg0, arg1, arg2, arg3));
    }

    public static Localizable localizableAMBIGUOUS_SRLS_PATH_PATTERN(Object arg0) {
        return messageFactory.getMessage("ambiguous.srls.pathPattern", arg0);
    }

    /**
     * A resource model has ambiguous sub-resource locators on path pattern {0}.
     * 
     */
    public static String AMBIGUOUS_SRLS_PATH_PATTERN(Object arg0) {
        return localizer.localize(localizableAMBIGUOUS_SRLS_PATH_PATTERN(arg0));
    }

    public static Localizable localizableSUSPEND_NOT_SUSPENDED() {
        return messageFactory.getMessage("suspend.not.suspended");
    }

    /**
     * Not suspended.
     * 
     */
    public static String SUSPEND_NOT_SUSPENDED() {
        return localizer.localize(localizableSUSPEND_NOT_SUSPENDED());
    }

    public static Localizable localizablePROPERTY_VALUE_TOSTRING_THROWS_EXCEPTION(Object arg0, Object arg1) {
        return messageFactory.getMessage("property.value.tostring.throws.exception", arg0, arg1);
    }

    /**
     * [{0} thrown from property value toString(): {1}]
     * 
     */
    public static String PROPERTY_VALUE_TOSTRING_THROWS_EXCEPTION(Object arg0, Object arg1) {
        return localizer.localize(localizablePROPERTY_VALUE_TOSTRING_THROWS_EXCEPTION(arg0, arg1));
    }

    public static Localizable localizableERROR_WADL_GENERATOR_CONFIG_LOADER(Object arg0) {
        return messageFactory.getMessage("error.wadl.generator.config.loader", arg0);
    }

    /**
     * Could not load WadlGeneratorConfiguration, check the configuration of "{0}".
     * 
     */
    public static String ERROR_WADL_GENERATOR_CONFIG_LOADER(Object arg0) {
        return localizer.localize(localizableERROR_WADL_GENERATOR_CONFIG_LOADER(arg0));
    }

    public static Localizable localizableERROR_MONITORING_MBEANS_REGISTRATION(Object arg0) {
        return messageFactory.getMessage("error.monitoring.mbeans.registration", arg0);
    }

    /**
     * Error when registering Jersey monitoring MBeans. Registration of MBean with name "{0}" failed.
     * 
     */
    public static String ERROR_MONITORING_MBEANS_REGISTRATION(Object arg0) {
        return localizer.localize(localizableERROR_MONITORING_MBEANS_REGISTRATION(arg0));
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_METHOD(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.builder.generation.method", arg0, arg1);
    }

    /**
     * Error generating wadl for method {0} of resource {1}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_METHOD(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_METHOD(arg0, arg1));
    }

    public static Localizable localizableERROR_MONITORING_QUEUE_MAPPER() {
        return messageFactory.getMessage("error.monitoring.queue.mapper");
    }

    /**
     * Failed to add the monitoring event into the Exception Mapper Events Queue - queue is full. One of the registered MonitoringStatisticsListeners might be blocking the event processing.
     * 
     */
    public static String ERROR_MONITORING_QUEUE_MAPPER() {
        return localizer.localize(localizableERROR_MONITORING_QUEUE_MAPPER());
    }

    public static Localizable localizableERROR_PARAMETER_MISSING_VALUE_PROVIDER(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.parameter.missing.value.provider", arg0, arg1);
    }

    /**
     * No injection source found for a parameter of type {1} at index {0}.
     * 
     */
    public static String ERROR_PARAMETER_MISSING_VALUE_PROVIDER(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_PARAMETER_MISSING_VALUE_PROVIDER(arg0, arg1));
    }

    public static Localizable localizableERROR_EXCEPTION_MAPPING_ORIGINAL_EXCEPTION() {
        return messageFactory.getMessage("error.exception.mapping.original.exception");
    }

    /**
     * An exception mapping did not successfully produce and processed a response. Logging the original error.
     * 
     */
    public static String ERROR_EXCEPTION_MAPPING_ORIGINAL_EXCEPTION() {
        return localizer.localize(localizableERROR_EXCEPTION_MAPPING_ORIGINAL_EXCEPTION());
    }

    public static Localizable localizableERROR_REQUEST_ABORT_IN_RESPONSE_PHASE() {
        return messageFactory.getMessage("error.request.abort.in.response.phase");
    }

    /**
     * The request cannot be aborted as it is already in the response processing phase.
     * 
     */
    public static String ERROR_REQUEST_ABORT_IN_RESPONSE_PHASE() {
        return localizer.localize(localizableERROR_REQUEST_ABORT_IN_RESPONSE_PHASE());
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_RESPONSE(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.builder.generation.response", arg0, arg1);
    }

    /**
     * Error generating wadl response representation for method {0} of resource {1}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_RESPONSE(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_RESPONSE(arg0, arg1));
    }

    public static Localizable localizableERROR_MONITORING_SHUTDOWN_INTERRUPTED() {
        return messageFactory.getMessage("error.monitoring.shutdown.interrupted");
    }

    /**
     * Waiting for shutdown of MonitoringStatisticsProcessor has been interrupted.
     * 
     */
    public static String ERROR_MONITORING_SHUTDOWN_INTERRUPTED() {
        return localizer.localize(localizableERROR_MONITORING_SHUTDOWN_INTERRUPTED());
    }

    public static Localizable localizableERROR_WADL_GRAMMAR_ALREADY_CONTAINS() {
        return messageFactory.getMessage("error.wadl.grammar.already.contains");
    }

    /**
     * The wadl application already contains a grammars element, we're adding elements of the provided grammars file.
     * 
     */
    public static String ERROR_WADL_GRAMMAR_ALREADY_CONTAINS() {
        return localizer.localize(localizableERROR_WADL_GRAMMAR_ALREADY_CONTAINS());
    }

    public static Localizable localizableRESOURCE_MODEL_VALIDATION_FAILED_AT_INIT() {
        return messageFactory.getMessage("resource.model.validation.failed.at.init");
    }

    /**
     * Validation of the application resource model has failed during application initialization.
     * 
     */
    public static String RESOURCE_MODEL_VALIDATION_FAILED_AT_INIT() {
        return localizer.localize(localizableRESOURCE_MODEL_VALIDATION_FAILED_AT_INIT());
    }

    public static Localizable localizableSUBRES_LOC_CACHE_LOAD_FAILED(Object arg0) {
        return messageFactory.getMessage("subres.loc.cache.load.failed", arg0);
    }

    /**
     * Loading model and router from sub-resource locator cache failed for "{0}".
     * 
     */
    public static String SUBRES_LOC_CACHE_LOAD_FAILED(Object arg0) {
        return localizer.localize(localizableSUBRES_LOC_CACHE_LOAD_FAILED(arg0));
    }

    public static Localizable localizableMETHOD_EMPTY_PATH_ANNOTATION(Object arg0, Object arg1) {
        return messageFactory.getMessage("method.empty.path.annotation", arg0, arg1);
    }

    /**
     * The (sub)resource method {0} in {1} contains empty path annotation.
     * 
     */
    public static String METHOD_EMPTY_PATH_ANNOTATION(Object arg0, Object arg1) {
        return localizer.localize(localizableMETHOD_EMPTY_PATH_ANNOTATION(arg0, arg1));
    }

    public static Localizable localizableUSER_NOT_AUTHORIZED() {
        return messageFactory.getMessage("user.not.authorized");
    }

    /**
     * User not authorized.
     * 
     */
    public static String USER_NOT_AUTHORIZED() {
        return localizer.localize(localizableUSER_NOT_AUTHORIZED());
    }

    public static Localizable localizableCLOSEABLE_UNABLE_TO_CLOSE(Object arg0) {
        return messageFactory.getMessage("closeable.unable.to.close", arg0);
    }

    /**
     * Error while closing {0}.
     * 
     */
    public static String CLOSEABLE_UNABLE_TO_CLOSE(Object arg0) {
        return localizer.localize(localizableCLOSEABLE_UNABLE_TO_CLOSE(arg0));
    }

    public static Localizable localizableWARNING_TOO_MANY_EXTERNAL_REQ_SCOPES(Object arg0) {
        return messageFactory.getMessage("warning.too.many.external.req.scopes", arg0);
    }

    /**
     * More than one external request scope found. None of them will be used. Jersey runtime can only accommodate a single external request scope: {0}
     * 
     */
    public static String WARNING_TOO_MANY_EXTERNAL_REQ_SCOPES(Object arg0) {
        return localizer.localize(localizableWARNING_TOO_MANY_EXTERNAL_REQ_SCOPES(arg0));
    }

    public static Localizable localizableNEW_AR_CREATED_BY_INTROSPECTION_MODELER(Object arg0) {
        return messageFactory.getMessage("new.ar.created.by.introspection.modeler", arg0);
    }

    /**
     * A new abstract resource created by IntrospectionModeler: {0}
     * 
     */
    public static String NEW_AR_CREATED_BY_INTROSPECTION_MODELER(Object arg0) {
        return localizer.localize(localizableNEW_AR_CREATED_BY_INTROSPECTION_MODELER(arg0));
    }

    public static Localizable localizableERROR_WADL_RESOURCE_MARSHAL() {
        return messageFactory.getMessage("error.wadl.resource.marshal");
    }

    /**
     * Could not marshal the wadl Application.
     * 
     */
    public static String ERROR_WADL_RESOURCE_MARSHAL() {
        return localizer.localize(localizableERROR_WADL_RESOURCE_MARSHAL());
    }

    public static Localizable localizableERROR_WADL_GENERATOR_CONFIG_LOADER_PROPERTY(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.generator.config.loader.property", arg0, arg1);
    }

    /**
     * The property {0} is an invalid type: {1} (supported: String, Class<? extends WadlGeneratorConfiguration>, WadlGeneratorConfiguration).
     * 
     */
    public static String ERROR_WADL_GENERATOR_CONFIG_LOADER_PROPERTY(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_GENERATOR_CONFIG_LOADER_PROPERTY(arg0, arg1));
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_REQUEST_MEDIA_TYPE(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.wadl.builder.generation.request.media.type", arg0, arg1, arg2);
    }

    /**
     * Error generating wadl request representation for media type {0} of method {1} of resource {2}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_REQUEST_MEDIA_TYPE(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_REQUEST_MEDIA_TYPE(arg0, arg1, arg2));
    }

    public static Localizable localizableRELEASING_REQUEST_PROCESSING_RESOURCES_FAILED() {
        return messageFactory.getMessage("releasing.request.processing.resources.failed");
    }

    /**
     * Attempt to release request processing resources has failed for a request.
     * 
     */
    public static String RELEASING_REQUEST_PROCESSING_RESOURCES_FAILED() {
        return localizer.localize(localizableRELEASING_REQUEST_PROCESSING_RESOURCES_FAILED());
    }

    public static Localizable localizableINVALID_CONFIG_PROPERTY_VALUE(Object arg0, Object arg1) {
        return messageFactory.getMessage("invalid.config.property.value", arg0, arg1);
    }

    /**
     * Invalid value for {0} configuration property: {1}
     * 
     */
    public static String INVALID_CONFIG_PROPERTY_VALUE(Object arg0, Object arg1) {
        return localizer.localize(localizableINVALID_CONFIG_PROPERTY_VALUE(arg0, arg1));
    }

    public static Localizable localizableEXCEPTION_MAPPING_WAE_NO_ENTITY(Object arg0) {
        return messageFactory.getMessage("exception.mapping.wae.no.entity", arg0);
    }

    /**
     * WebApplicationException (WAE) with no entity thrown and no ExceptionMappers have been found for this WAE. Response with status {0} is directly generated from the WAE.
     * 
     */
    public static String EXCEPTION_MAPPING_WAE_NO_ENTITY(Object arg0) {
        return localizer.localize(localizableEXCEPTION_MAPPING_WAE_NO_ENTITY(arg0));
    }

    public static Localizable localizableDEFAULT_COULD_NOT_PROCESS_METHOD(Object arg0, Object arg1) {
        return messageFactory.getMessage("default.could.not.process.method", arg0, arg1);
    }

    /**
     * Default value, {0} could not be processed by method {1}.
     * 
     */
    public static String DEFAULT_COULD_NOT_PROCESS_METHOD(Object arg0, Object arg1) {
        return localizer.localize(localizableDEFAULT_COULD_NOT_PROCESS_METHOD(arg0, arg1));
    }

    public static Localizable localizableLOGGING_PROVIDER_BOUND(Object arg0, Object arg1) {
        return messageFactory.getMessage("logging.provider.bound", arg0, arg1);
    }

    /**
     * {0} bound to {1}
     * 
     */
    public static String LOGGING_PROVIDER_BOUND(Object arg0, Object arg1) {
        return localizer.localize(localizableLOGGING_PROVIDER_BOUND(arg0, arg1));
    }

    public static Localizable localizableRC_NOT_MODIFIABLE() {
        return messageFactory.getMessage("rc.not.modifiable");
    }

    /**
     * The resource configuration is not modifiable in this context.
     * 
     */
    public static String RC_NOT_MODIFIABLE() {
        return localizer.localize(localizableRC_NOT_MODIFIABLE());
    }

    public static Localizable localizableSUB_RES_METHOD_TREATED_AS_RES_METHOD(Object arg0, Object arg1) {
        return messageFactory.getMessage("sub.res.method.treated.as.res.method", arg0, arg1);
    }

    /**
     * A sub-resource method, {0}, with URI template, "{1}", is treated as a resource method
     * 
     */
    public static String SUB_RES_METHOD_TREATED_AS_RES_METHOD(Object arg0, Object arg1) {
        return localizer.localize(localizableSUB_RES_METHOD_TREATED_AS_RES_METHOD(arg0, arg1));
    }

    public static Localizable localizableERROR_WADL_GENERATOR_LOAD() {
        return messageFactory.getMessage("error.wadl.generator.load");
    }

    /**
     * Could not load wadl generators from wadlGeneratorDescriptions.
     * 
     */
    public static String ERROR_WADL_GENERATOR_LOAD() {
        return localizer.localize(localizableERROR_WADL_GENERATOR_LOAD());
    }

    public static Localizable localizableSUBRES_LOC_URI_PATH_INVALID(Object arg0, Object arg1) {
        return messageFactory.getMessage("subres.loc.uri.path.invalid", arg0, arg1);
    }

    /**
     * A sub-resource model, {0}, has an invalid URI path: {1}
     * 
     */
    public static String SUBRES_LOC_URI_PATH_INVALID(Object arg0, Object arg1) {
        return localizer.localize(localizableSUBRES_LOC_URI_PATH_INVALID(arg0, arg1));
    }

    public static Localizable localizableFORM_PARAM_CONTENT_TYPE_ERROR() {
        return messageFactory.getMessage("form.param.content-type.error");
    }

    /**
     * The @FormParam is utilized when the content type of the request entity is not application/x-www-form-urlencoded
     * 
     */
    public static String FORM_PARAM_CONTENT_TYPE_ERROR() {
        return localizer.localize(localizableFORM_PARAM_CONTENT_TYPE_ERROR());
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_REQUEST(Object arg0, Object arg1) {
        return messageFactory.getMessage("error.wadl.builder.generation.request", arg0, arg1);
    }

    /**
     * Error generating wadl for request representation of resource method {0} of resource {1}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_REQUEST(Object arg0, Object arg1) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_REQUEST(arg0, arg1));
    }

    public static Localizable localizableSECURITY_CONTEXT_WAS_NOT_SET() {
        return messageFactory.getMessage("security.context.was.not.set");
    }

    /**
     * SecurityContext was not set.
     * 
     */
    public static String SECURITY_CONTEXT_WAS_NOT_SET() {
        return localizer.localize(localizableSECURITY_CONTEXT_WAS_NOT_SET());
    }

    public static Localizable localizableRESOURCE_IMPLEMENTS_PROVIDER(Object arg0, Object arg1) {
        return messageFactory.getMessage("resource.implements.provider", arg0, arg1);
    }

    /**
     * A resource, {0}, implements provider interface {1} but does not explicitly define the scope (@Singleton, @PerLookup). The resource class will be managed as singleton.
     * 
     */
    public static String RESOURCE_IMPLEMENTS_PROVIDER(Object arg0, Object arg1) {
        return localizer.localize(localizableRESOURCE_IMPLEMENTS_PROVIDER(arg0, arg1));
    }

    public static Localizable localizableLOGGING_GLOBAL_RESPONSE_FILTERS() {
        return messageFactory.getMessage("logging.global.response.filters");
    }

    /**
     * Global Response Filters
     * 
     */
    public static String LOGGING_GLOBAL_RESPONSE_FILTERS() {
        return localizer.localize(localizableLOGGING_GLOBAL_RESPONSE_FILTERS());
    }

    public static Localizable localizableERROR_WADL_BUILDER_GENERATION_PARAM(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("error.wadl.builder.generation.param", arg0, arg1, arg2);
    }

    /**
     * Error generating wadl for parameter {0} of resource {1} and resource method {2}.
     * 
     */
    public static String ERROR_WADL_BUILDER_GENERATION_PARAM(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableERROR_WADL_BUILDER_GENERATION_PARAM(arg0, arg1, arg2));
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

    public static Localizable localizableBROADCASTER_LISTENER_EXCEPTION(Object arg0) {
        return messageFactory.getMessage("broadcaster.listener.exception", arg0);
    }

    /**
     * {0} thrown from BroadcasterListener.
     * 
     */
    public static String BROADCASTER_LISTENER_EXCEPTION(Object arg0) {
        return localizer.localize(localizableBROADCASTER_LISTENER_EXCEPTION(arg0));
    }

    public static Localizable localizableRESOURCE_MERGE_CONFLICT_LOCATORS(Object arg0, Object arg1, Object arg2) {
        return messageFactory.getMessage("resource.merge.conflict.locators", arg0, arg1, arg2);
    }

    /**
     * Both resources, resource {0} and resource {1}, contains sub resource locators on the same path {2}.
     * 
     */
    public static String RESOURCE_MERGE_CONFLICT_LOCATORS(Object arg0, Object arg1, Object arg2) {
        return localizer.localize(localizableRESOURCE_MERGE_CONFLICT_LOCATORS(arg0, arg1, arg2));
    }

    public static Localizable localizableERROR_RESOURCES_CANNOT_MERGE() {
        return messageFactory.getMessage("error.resources.cannot.merge");
    }

    /**
     * Resources do not have the same path and cannot be merged.
     * 
     */
    public static String ERROR_RESOURCES_CANNOT_MERGE() {
        return localizer.localize(localizableERROR_RESOURCES_CANNOT_MERGE());
    }

    public static Localizable localizableERROR_MONITORING_STATISTICS_LISTENER_DESTROY(Object arg0) {
        return messageFactory.getMessage("error.monitoring.statistics.listener.destroy", arg0);
    }

    /**
     * Exception thrown when listener {0} has been processing the destroy event.
     * 
     */
    public static String ERROR_MONITORING_STATISTICS_LISTENER_DESTROY(Object arg0) {
        return localizer.localize(localizableERROR_MONITORING_STATISTICS_LISTENER_DESTROY(arg0));
    }

    public static Localizable localizableWADL_JAXB_CONTEXT_FALLBACK() {
        return messageFactory.getMessage("wadl.jaxb.context.fallback");
    }

    /**
     * Error creating a JAXBContext for wadl serialization. Trying a fallback solution for osgi environments.
     * 
     */
    public static String WADL_JAXB_CONTEXT_FALLBACK() {
        return localizer.localize(localizableWADL_JAXB_CONTEXT_FALLBACK());
    }

    public static Localizable localizableMETHOD_INVOCABLE_FROM_PREMATCH_FILTERS_ONLY() {
        return messageFactory.getMessage("method.invocable.from.prematch.filters.only");
    }

    /**
     * Method can only be invoked from pre-matching request filters.
     * 
     */
    public static String METHOD_INVOCABLE_FROM_PREMATCH_FILTERS_ONLY() {
        return localizer.localize(localizableMETHOD_INVOCABLE_FROM_PREMATCH_FILTERS_ONLY());
    }

    public static Localizable localizableLOGGING_NAME_BOUND_WRITER_INTERCEPTORS() {
        return messageFactory.getMessage("logging.name.bound.writer.interceptors");
    }

    /**
     * Name-bound Writer Interceptors
     * 
     */
    public static String LOGGING_NAME_BOUND_WRITER_INTERCEPTORS() {
        return localizer.localize(localizableLOGGING_NAME_BOUND_WRITER_INTERCEPTORS());
    }

    public static Localizable localizableCALLBACK_ARRAY_ELEMENT_NULL() {
        return messageFactory.getMessage("callback.array.element.null");
    }

    /**
     * One of additional callbacks is null.
     * 
     */
    public static String CALLBACK_ARRAY_ELEMENT_NULL() {
        return localizer.localize(localizableCALLBACK_ARRAY_ELEMENT_NULL());
    }

    public static Localizable localizableERROR_CLOSING_COMMIT_OUTPUT_STREAM() {
        return messageFactory.getMessage("error.closing.commit.output.stream");
    }

    /**
     * Error while closing the output stream in order to commit response.
     * 
     */
    public static String ERROR_CLOSING_COMMIT_OUTPUT_STREAM() {
        return localizer.localize(localizableERROR_CLOSING_COMMIT_OUTPUT_STREAM());
    }

    public static Localizable localizableERROR_MSG(Object arg0) {
        return messageFactory.getMessage("error.msg", arg0);
    }

    /**
     * ERROR: {0}
     * 
     */
    public static String ERROR_MSG(Object arg0) {
        return localizer.localize(localizableERROR_MSG(arg0));
    }

    public static Localizable localizableWADL_DOC_SIMPLE_WADL(Object arg0, Object arg1) {
        return messageFactory.getMessage("wadl.doc.simple.wadl", arg0, arg1);
    }

    /**
     * This is simplified WADL with user and core resources only. To get full WADL with extended resources use the query parameter {0}. Link: {1}
     * 
     */
    public static String WADL_DOC_SIMPLE_WADL(Object arg0, Object arg1) {
        return localizer.localize(localizableWADL_DOC_SIMPLE_WADL(arg0, arg1));
    }

    public static Localizable localizableLOGGING_DYNAMIC_FEATURES() {
        return messageFactory.getMessage("logging.dynamic.features");
    }

    /**
     * Dynamic Features
     * 
     */
    public static String LOGGING_DYNAMIC_FEATURES() {
        return localizer.localize(localizableLOGGING_DYNAMIC_FEATURES());
    }

    public static Localizable localizableMETHOD_PARAMETER_CANNOT_BE_NULL_OR_EMPTY(Object arg0) {
        return messageFactory.getMessage("method.parameter.cannot.be.null.or.empty", arg0);
    }

    /**
     * Method parameter "{0}" cannot be null or empty.
     * 
     */
    public static String METHOD_PARAMETER_CANNOT_BE_NULL_OR_EMPTY(Object arg0) {
        return localizer.localize(localizableMETHOD_PARAMETER_CANNOT_BE_NULL_OR_EMPTY(arg0));
    }

    public static Localizable localizableAMBIGUOUS_NON_ANNOTATED_PARAMETER(Object arg0, Object arg1) {
        return messageFactory.getMessage("ambiguous.non.annotated.parameter", arg0, arg1);
    }

    /**
     * Method {0} on resource {1} contains multiple parameters with no annotation. Unable to resolve the injection source.
     * 
     */
    public static String AMBIGUOUS_NON_ANNOTATED_PARAMETER(Object arg0, Object arg1) {
        return localizer.localize(localizableAMBIGUOUS_NON_ANNOTATED_PARAMETER(arg0, arg1));
    }

    public static Localizable localizableMULTIPLE_HTTP_METHOD_DESIGNATORS(Object arg0, Object arg1) {
        return messageFactory.getMessage("multiple.http.method.designators", arg0, arg1);
    }

    /**
     * A (sub-)resource method, {0}, should have only one HTTP method designator. It currently has the following designators defined: {1}
     * 
     */
    public static String MULTIPLE_HTTP_METHOD_DESIGNATORS(Object arg0, Object arg1) {
        return localizer.localize(localizableMULTIPLE_HTTP_METHOD_DESIGNATORS(arg0, arg1));
    }

}
