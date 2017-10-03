package javax.jws;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({PARAMETER})
public @interface WebParam {
    
    public enum Mode {
   	IN,
	OUT,
	INOUT
    };

    String name() default "";
    String partName() default "";
    String targetNamespace() default "";
    Mode mode() default Mode.IN;
    boolean header() default false;
}
