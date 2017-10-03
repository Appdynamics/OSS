package javax.jws;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface WebResult {
    
    String name() default "";
    String partName() default "";
    String targetNamespace() default "";
    boolean header() default false;
}
