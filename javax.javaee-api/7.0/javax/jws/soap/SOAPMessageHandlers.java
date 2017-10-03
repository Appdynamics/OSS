package javax.jws.soap;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

/*
 * @Deprecated
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target({TYPE})
@Deprecated public @interface SOAPMessageHandlers {
    SOAPMessageHandler[] value();
}
