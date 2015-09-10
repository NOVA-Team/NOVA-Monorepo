/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.loader;

import se.jbee.inject.bootstrap.Bundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation placed on the NOVA mod's main loading class.
 *
 * @author Calclavia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	/**
	 * The unique mod identifier for this mod
	 *
	 * @return Mod ID
	 */
	String id();

	/**
	 * The user friendly name for the mod
	 *
	 * @return Mod name
	 */
	String name();

	/**
	 * The version identifier of this mod
	 *
	 * @return Mod Version
	 */
	String version();

	/**
	 * A simple description of the mod.
	 *
	 * @return Mod description
	 */
	String description() default "";

	/**
	 * The version of Nova this mod is compatible with
	 *
	 * @return Nova version
	 */
	String novaVersion();

	/**
	 * The domains used by the mod for its assets.
	 * These domain names are used to load the assets from file.
	 * The assets should be placed in assets/domain/*
	 *
	 * @return An array of domain names.
	 */
	String[] domains() default {};

	/**
	 * An array of the dependencies for this mod. The mod will load after all the dependencies are loaded.
	 *
	 * String format:
	 *
	 * "x" is the version wildcard.
	 * Adding "f" after the version will force the dependency to be a requirement.
	 * E.g: BuildCraft@6.1.x?
	 *
	 * @return The dependencies
	 */
	String[] dependencies() default {};

	/**
	 * Modules of Dependency Injection that will be added to core injector allowing provision of modules by mods.
	 *
	 * @return The modules
	 */
	Class<? extends Bundle>[] modules() default {};

	/**
	 * This method is used by APIs to allow them to load before most mods load.
	 *
	 * @return The priority for the plugin to load. The higher the number, the higher the load priority.
	 */
	int priority() default 0;
}
