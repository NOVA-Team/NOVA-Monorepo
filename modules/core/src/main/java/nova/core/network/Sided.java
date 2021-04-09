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
 */package nova.core.network;

import nova.core.network.NetworkTarget.IllegalSideException;
import nova.core.network.NetworkTarget.Side;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO source only or should we modify the ClassLoader to reject sided methods?
/**
 * Sided is used to mark methods, classes and other types in order to clarify
 * that it should only be used on the specified side. <b>Referencing such a type
 * will most likely trigger an {@link IllegalSideException} or will malfunction
 * at worst!</b>
 *
 * @author Vic Nightfall
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Sided {
	Side value();
}
