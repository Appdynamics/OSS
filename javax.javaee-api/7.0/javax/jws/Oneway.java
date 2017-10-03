package javax.jws;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface Oneway {
}
