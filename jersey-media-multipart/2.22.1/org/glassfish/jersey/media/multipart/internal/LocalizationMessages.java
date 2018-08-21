
package org.glassfish.jersey.media.multipart.internal;

import org.glassfish.jersey.internal.l10n.Localizable;
import org.glassfish.jersey.internal.l10n.LocalizableMessageFactory;
import org.glassfish.jersey.internal.l10n.Localizer;


/**
 * Defines string formatting method for each constant in the resource file
 * 
 */
public final class LocalizationMessages {

    private final static LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.media.multipart.internal.localization");
    private final static Localizer localizer = new Localizer();

    public static Localizable localizablePARSING_ERROR() {
        return messageFactory.getMessage("parsing.error");
    }

    /**
     * Exception occurred during parsing MultiPart message. Performing cleanup.
     * 
     */
    public static String PARSING_ERROR() {
        return localizer.localize(localizablePARSING_ERROR());
    }

    public static Localizable localizableENTITY_IS_EMPTY() {
        return messageFactory.getMessage("entity.is.empty");
    }

    /**
     * Entity is empty.
     * 
     */
    public static String ENTITY_IS_EMPTY() {
        return localizer.localize(localizableENTITY_IS_EMPTY());
    }

    public static Localizable localizableENTITY_HAS_WRONG_TYPE() {
        return messageFactory.getMessage("entity.has.wrong.type");
    }

    /**
     * Entity instance does not contain the unconverted content.
     * 
     */
    public static String ENTITY_HAS_WRONG_TYPE() {
        return localizer.localize(localizableENTITY_HAS_WRONG_TYPE());
    }

    public static Localizable localizableFORM_DATA_MULTIPART_CANNOT_CHANGE_MEDIATYPE() {
        return messageFactory.getMessage("form.data.multipart.cannot.change.mediatype");
    }

    /**
     * Cannot change media type of a FormDataMultiPart instance.
     * 
     */
    public static String FORM_DATA_MULTIPART_CANNOT_CHANGE_MEDIATYPE() {
        return localizer.localize(localizableFORM_DATA_MULTIPART_CANNOT_CHANGE_MEDIATYPE());
    }

    public static Localizable localizableTEMP_FILE_CANNOT_BE_CREATED(Object arg0) {
        return messageFactory.getMessage("temp.file.cannot.be.created", arg0);
    }

    /**
     * Cannot create temporary files. Multipart attachments will be limited to "{0}" bytes.
     * 
     */
    public static String TEMP_FILE_CANNOT_BE_CREATED(Object arg0) {
        return localizer.localize(localizableTEMP_FILE_CANNOT_BE_CREATED(arg0));
    }

    public static Localizable localizableNO_AVAILABLE_MBW(Object arg0, Object arg1) {
        return messageFactory.getMessage("no.available.mbw", arg0, arg1);
    }

    /**
     * No available MessageBodyWriter for class "{0}" and media type "{1}".
     * 
     */
    public static String NO_AVAILABLE_MBW(Object arg0, Object arg1) {
        return localizer.localize(localizableNO_AVAILABLE_MBW(arg0, arg1));
    }

    public static Localizable localizableERROR_PARSING_CONTENT_DISPOSITION(Object arg0) {
        return messageFactory.getMessage("error.parsing.content.disposition", arg0);
    }

    /**
     * Error parsing content disposition: {0}
     * 
     */
    public static String ERROR_PARSING_CONTENT_DISPOSITION(Object arg0) {
        return localizer.localize(localizableERROR_PARSING_CONTENT_DISPOSITION(arg0));
    }

    public static Localizable localizableCONTROL_NAME_CANNOT_BE_NULL() {
        return messageFactory.getMessage("control.name.cannot.be.null");
    }

    /**
     * Controls name can not be null.
     * 
     */
    public static String CONTROL_NAME_CANNOT_BE_NULL() {
        return localizer.localize(localizableCONTROL_NAME_CANNOT_BE_NULL());
    }

    public static Localizable localizableTEMP_FILE_NOT_DELETED(Object arg0) {
        return messageFactory.getMessage("temp.file.not.deleted", arg0);
    }

    /**
     * Temporary file {0} was not deleted.
     * 
     */
    public static String TEMP_FILE_NOT_DELETED(Object arg0) {
        return localizer.localize(localizableTEMP_FILE_NOT_DELETED(arg0));
    }

    public static Localizable localizableMISSING_ENTITY_OF_BODY_PART(Object arg0) {
        return messageFactory.getMessage("missing.entity.of.body.part", arg0);
    }

    /**
     * Missing body part entity of type "{0}".
     * 
     */
    public static String MISSING_ENTITY_OF_BODY_PART(Object arg0) {
        return localizer.localize(localizableMISSING_ENTITY_OF_BODY_PART(arg0));
    }

    public static Localizable localizableCANNOT_INJECT_FILE() {
        return messageFactory.getMessage("cannot.inject.file");
    }

    /**
     * Cannot provide file for an entity body part.
     * 
     */
    public static String CANNOT_INJECT_FILE() {
        return localizer.localize(localizableCANNOT_INJECT_FILE());
    }

    public static Localizable localizableNO_AVAILABLE_MBR(Object arg0, Object arg1) {
        return messageFactory.getMessage("no.available.mbr", arg0, arg1);
    }

    /**
     * No available MessageBodyReader for class "{0}" and media type "{1}".
     * 
     */
    public static String NO_AVAILABLE_MBR(Object arg0, Object arg1) {
        return localizer.localize(localizableNO_AVAILABLE_MBR(arg0, arg1));
    }

    public static Localizable localizableERROR_READING_ENTITY(Object arg0) {
        return messageFactory.getMessage("error.reading.entity", arg0);
    }

    /**
     * Error reading entity as {0}.
     * 
     */
    public static String ERROR_READING_ENTITY(Object arg0) {
        return localizer.localize(localizableERROR_READING_ENTITY(arg0));
    }

    public static Localizable localizableMUST_SPECIFY_BODY_PART() {
        return messageFactory.getMessage("must.specify.body.part");
    }

    /**
     * Must specify at least one body part.
     * 
     */
    public static String MUST_SPECIFY_BODY_PART() {
        return localizer.localize(localizableMUST_SPECIFY_BODY_PART());
    }

    public static Localizable localizableMISSING_MEDIA_TYPE_OF_BODY_PART() {
        return messageFactory.getMessage("missing.media.type.of.body.part");
    }

    /**
     * Missing media type of body part.
     * 
     */
    public static String MISSING_MEDIA_TYPE_OF_BODY_PART() {
        return localizer.localize(localizableMISSING_MEDIA_TYPE_OF_BODY_PART());
    }

    public static Localizable localizableMEDIA_TYPE_NOT_TEXT_PLAIN() {
        return messageFactory.getMessage("media.type.not.text.plain");
    }

    /**
     * Media type is not text/plain.
     * 
     */
    public static String MEDIA_TYPE_NOT_TEXT_PLAIN() {
        return localizer.localize(localizableMEDIA_TYPE_NOT_TEXT_PLAIN());
    }

}
