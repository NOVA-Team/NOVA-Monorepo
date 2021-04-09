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

package nova.core.block.component;

import nova.core.util.Direction;

/**
 * Implement this interface on BlockLogics which allow insertion of
 * objects into them.
 */
//TODO: Convert to component
public interface Insertable {
	/**
	 * @param type The type of the inserted object.
	 * @param side The side the object is inserted on.
	 * @return {@code true} if this type can be injected
	 */
	boolean canInsert(Class<?> type, Direction side);

	/**
	 * @param object The inserted object.
	 * @param side The side the object is inserted on.
	 * @param simulate Whether to simulate the insertion.
	 * @return An object representing the remainder of the insertion, or null
	 * if the object has been used up completely.
	 */
	Object insert(Object object, Direction side, boolean simulate);
}

