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

package nova.core.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * This annotation can be used together with {@link Component} to specify an
 * interface to be passed through to the wrapper implementation. This can be
 * used to achieve basic native compatibility, for example for energy systems.
 * </p>
 * 
 * <p>
 * A component with a passthrough will cause the wrapper to implement an
 * interface on the native representation and respectively creates a new
 * instance of said passthrough component for every native representation that
 * implements the interface and isn't added by NOVA.
 * </p>
 * 
 * <p>
 * A passthrough component has to be registered with
 * {@link ComponentManager#registerNativePassthrough(Class)}
 * </p>
 * 
 * @author Vic Nightfall
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Passthrough {

	String value();
}
