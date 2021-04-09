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