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

package nova.core.util;

import nova.core.loader.Mod;
import nova.internal.core.launch.ModLoader;

import java.util.Optional;

/**
 * A generic interface signifying that this object is identifiable
 * by an ID
 */
public interface Identifiable {
	/**
	 * Get the ID to identify this object by
	 *
	 * @return the ID
	 */
	String getID();

	/**
	 * Compares the ID of the Identifialbes
	 *
	 * @param other Identifiable to compare to
	 * @return If the Identifiables are the same type
	 */

	default boolean sameType(Identifiable other) {
		return getID().equals(other.getID());
	}

	static String addPrefix(String id, boolean force) {
		int prefixEnd = id.lastIndexOf(':');
		String oldPrefix = prefixEnd < 0 ? "" : id.substring(0, prefixEnd);
		String newPrefix = null;
		Optional mod = ModLoader.instance().activeMod();

		if (mod.isPresent() && mod.get() instanceof Mod) {
			newPrefix = ((Mod)mod.get()).id();
		}

		if (newPrefix != null && (force ? !oldPrefix.startsWith(newPrefix) : oldPrefix.isEmpty())) {
			id = newPrefix + ':' + id;
		}

		return id;
	}
}
