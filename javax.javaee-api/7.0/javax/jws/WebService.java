package javax.jws;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface WebService {
    String name() default "";
    String targetNamespace() default "";
    String serviceName() default "";
    String portName() default "";
    String wsdlLocation() default "";
    String endpointInterface() default "";
}
