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

package nova.core.render.texture;

import nova.core.util.id.Identifiable;
import nova.core.util.id.Identifier;
import nova.core.util.id.NamespacedStringIdentifier;
import nova.core.util.math.Vector2DUtil;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A texture has a file location. All texture must be included in
 * /assets/domain/textures/*
 *
 * @author Calclavia
 */
public class Texture extends Icon implements Identifiable {

	public final String domain;
	public final String resource;
	public final Vector2D dimension;

	@SuppressWarnings("deprecation")
	public Texture(String domain, String resource) {
		this.domain = domain;
		this.resource = resource;
		this.dimension = Game.render().getDimension(this);

		super.texture = this;
		super.minUV = Vector2DUtil.ONE;
		super.maxUV = Vector2D.ZERO;
	}

	public String getResource() {
		return domain + ":" + resource;
	}

	public String getPath() {
		return resource + ".png";
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getPath() + "]";
	}

	@Override
	public final Identifier getID() {
		return new NamespacedStringIdentifier(domain, resource);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			Texture other = (Texture) obj;
			return other.getID().equals(getID());
		}

		return false;
	}
}
