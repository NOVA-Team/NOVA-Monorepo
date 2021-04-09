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
 */package nova.core.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to fetch and load configs easily.
 * Configs are saved in
 * <a href="https://github.com/typesafehub/config/blob/master/HOCON.md">HOCON (Human-Optimized Config Object Notation)</a>
 * format, which is a nice JSON superset with less pedantic syntax and comments.
 * It has a tree structure, so any element could be reached via package-like path.<p>
 * Example usage:
 * <pre>
 * &#64;Config("items.rocket.params")
 * public int maxCount = 3; //4 is the default value
 * </pre>
 * This code will load the <tt>items.rocket.params.maxCount</tt> config setting into this variable.<p>
 * The config file content may look like this:
 * <pre>
 * items{
 *     rocket{
 *         params{
 *             maxCount = 14
 *             enabled: true
 *         }
 *     }
 * }
 * baz = string
 * </pre><p>
 * For root values you could just use {@code &#64;Config} annotation with no parameters:
 * <pre>
 * &#64;Config
 * public String baz = "default";
 * </pre><p>
 * Note, that your class must have an {@link nova.core.config.ConfigHolder ConfigHolder} annotation
 * for this to work properly.
 * <p>
 * Also, if you allow {@code &#64;ConfigHolder} annotation to {@link nova.core.config.ConfigHolder#value scan}
 * inner {@code &#64;ConfigHolder} types, then this types could represent HOCON objects of your config.
 *
 * @author anti344
 * @see nova.core.config.ConfigHolder ConfigHolder
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Config {

	Config DEFAULT = new Config(){

		@Override
		public String value() {
			return "";
		}

		@Override
		public String comment() {
			return "";
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return Config.class;
		}
	};

	/**
	 * @return Path to the parent object of this field.
	 */
	String value() default "";

	/**
	 * This comment message will be added to the config file if this field do not yet exist
	 *
	 * @return Default comment message.
	 */
	String comment() default "";
}
