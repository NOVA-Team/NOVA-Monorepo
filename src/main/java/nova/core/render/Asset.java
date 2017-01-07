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

package nova.core.render;

import nova.core.util.Identifiable;

/**
 * @author Calclavia
 */
//TODO: Texture should extend Asset
public abstract class Asset implements Identifiable {
	//The domain of the assets
	public final String domain;
	//The name of the file
	public final String name;

	public Asset(String domain, String name) {
		this.domain = domain;
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Asset other = (Asset) obj;
			return other.getID().equals(getID());
		}

		return false;
	}

	@Override
	public final String getID() {
		return domain + ":" + name;
	}
}
