package nova.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark a class, that could hold {@link nova.core.config.Config @Config} values in it.<p>
 * Allows custom classes to represent inner objects of given config.
 *
 * @author anti344
 * @see nova.core.config.ConfigHolder#value ConfigHolder#value
 * @see nova.core.config.Config Config
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigHolder {

	/**
	 * If {@code true} then types of {@code @Config} fields, which also have this annotation will be scanned,
	 * and their paths will be appended to paths of those fields.<p>
	 * This means you could represent a HOCON object as a class.
	 *
	 * @return {@code true} to scan inner holders.
	 */
	boolean value() default false;

	/**
	 * If {@code true} then instead of searching for fields, marked as {@code @Config}, all the fields without this
	 * annotation will be treated as if they have an {@code @Config} above them.
	 *
	 * @return {@code true} to use all the fields.
	 */
	boolean useAll() default false;
}