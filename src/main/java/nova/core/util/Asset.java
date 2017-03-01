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

import java.util.Objects;

/**
 * @author Calclavia
 */
public class Asset implements Identifiable {
	//The domain of the assets
	public final String domain;
	//The name of the file
	public final String name;

	public Asset(String domain, String name) {
		this.domain = domain.replace(':', '/');
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Asset other = (Asset) obj;
			return getID().equals(other.getID());
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 41 * hash + Objects.hashCode(this.domain);
		hash = 41 * hash + Objects.hashCode(this.name);
		return hash;
	}

	public String path() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + '[' + getID() + ']';
	}

	@Override
	public final String getID() {
		return (domain + ':' + name).toLowerCase();
	}

	protected static final String addDefaultSuffix(String name, String suffix) {
		String file = name.substring(name.lastIndexOf('/') + 1);
		if (file.indexOf('.') == -1) {
			if (suffix.indexOf('.') == -1)
				return name + '.' + suffix;
			else
				return name + suffix;
		}
		return name;
	}
}
