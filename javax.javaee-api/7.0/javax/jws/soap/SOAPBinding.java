package javax.jws.soap;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Retention(value=RetentionPolicy.RUNTIME)
@Target({TYPE,METHOD})
public @interface SOAPBinding {
    
    public enum Style {
       DOCUMENT,
       RPC
    };
   
    public enum Use {
   	LITERAL,
	ENCODED
    };

    public enum ParameterStyle {
   	BARE,
	WRAPPED
    };

    Style style() default Style.DOCUMENT;
    Use use() default Use.LITERAL;
    ParameterStyle parameterStyle() default ParameterStyle.WRAPPED;
}
