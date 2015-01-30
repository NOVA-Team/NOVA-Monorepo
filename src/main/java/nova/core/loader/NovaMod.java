package nova.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation placed in the mod's main loading class.
 * @author Calclavia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NovaMod {
	/**
	 * The unique mod identifier for this mod
	 */
	String id();

	/**
	 * A user friendly name for the mod
	 */
	String name();

	/**
	 * A version of this mod
	 */
	String version();

	/**
	 * A version of Nova version for this mod
	 */
	String novaVersion();

	/**
	 * An array of dependencies for this mod. The mod will load after all the dependencies are loaded.
	 *
	 * String format:
	 *
	 * "x" is the version wildcard.
	 * Adding "f" after the version will force the dependency to be a requirement.
	 * E.g: BuildCraft@6.1.x?
	 */
	String[] dependencies() default { };
}
