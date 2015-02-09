package nova.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply this annotation to static fields only, otherwise will malfunction and fail. use this to
 * automate the configuration file activity, and as such, no need to "forget" to add a configuration
 * option
 *
 * Keep in mind, This configuration option will work for any type of Access level. this can be
 * applied to private, protected, public fields as far as they are static.
 *
 * @author Calclavia
 * @Config(category = "values", key = "RocketCount") public static int ROCKET_COUNT = 3;
 * <p/>
 * Another very Simple use is:
 * @Config public static int ROCKET_COUNT = 3;
 *
 * Which will in turn, put in the config this:
 *
 * ROCKET_COUNT=3
 *
 * As by reflection the Config Handler goes with Default Configuration.CATEGORY_GENERAL and uses the
 * Key as the Field name
 * @since 09/03/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config {
	public String category() default "general";

	/**
	 * The default key would be the name of the field. The name of the field must be in camel case.
	 */
	public String key() default "";

	public String comment() default "";
}
