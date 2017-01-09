package nova.core.retention;

import java.lang.annotation.*;

/**
 * Annotation for **immutable** objects that can be converted to/from Data.
 * @author soniex2
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataConvertible {
	Class<? extends DataConverter> value();
}
