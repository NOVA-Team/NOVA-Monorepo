package nova.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to Loadable objects that only should be loaded when a specific game is present
 * @author Calclavia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Deprecated
public @interface NativeLoader {

	/**
	 * @return The games this plugin works for.
	 */
	String forGame() default "";
}
