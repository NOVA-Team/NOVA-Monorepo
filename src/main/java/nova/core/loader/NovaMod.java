package nova.core.loader;

import se.jbee.inject.bootstrap.Bundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation placed on the mod's main loading class.
 * @author Calclavia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NovaMod {
	/**
	 * The unique mod identifier for this mod
	 * @return Mod ID
	 */
	String id();

	/**
	 * The user friendly name for the mod
	 * @return Mod name
	 */
	String name();

	/**
	 * The version identifier of this mod
	 * @return Mod Version
	 */
	String version();

	/**
	 * A simple description of the mod.
	 * @return Mod description
	 */
	String description() default "";

	/**
	 * The version of Nova this mod is compatible with
	 * @return Nova version
	 */
	String novaVersion();

	/**
	 * An array of the dependencies for this mod. The mod will load after all the dependencies are loaded.
	 *
	 * String format:
	 *
	 * "x" is the version wildcard.
	 * Adding "f" after the version will force the dependency to be a requirement.
	 * E.g: BuildCraft@6.1.x?
	 * @return The dependencies
	 */
	String[] dependencies() default {};

	/**
	 * Modules of Dependency Injection that will be added to core injector allowing provision of modules by mods.
	 * @return The modules
	 */
	Class<? extends Bundle>[] modules() default {};

	/**
	 * Is this NovaMod a plugin? A NOVA Plugin is usually an API and wrapper that works along NOVA to add more native features to NOVA.
	 * @return True if the mod is a NOVA Plugin.
	 */
	boolean isPlugin() default false;
}
