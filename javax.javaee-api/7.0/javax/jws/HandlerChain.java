package javax.jws;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, FIELD})
public @interface HandlerChain {
    String file();
    @Deprecated String name() default "";
}
