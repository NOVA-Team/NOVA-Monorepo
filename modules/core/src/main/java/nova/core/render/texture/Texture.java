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
 */package nova.core.render.texture;

import nova.core.util.Asset;
import nova.core.util.Identifiable;
import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A texture has a file location. All texture must be included in
 * /assets/domain/textures/*
 *
 * @author Calclavia
 */
public class Texture extends Asset implements Identifiable {

	public final Vector2D dimension;
	public final Vector2D minUV;
	public final Vector2D maxUV;

	@SuppressWarnings("deprecation")
	public Texture(String domain, String name) {
		super(domain, name);
		this.dimension = Vector2DUtil.ONE;
		this.minUV = Vector2DUtil.ONE;
		this.maxUV = Vector2D.ZERO;
	}

	@SuppressWarnings("deprecation")
	Texture(String domain, String name, Vector2D minUV, Vector2D maxUV) {
		super(domain, name);
		this.dimension = Vector2DUtil.ONE;
		this.minUV = minUV;
		this.maxUV = maxUV;
	}

	@Override
	public String path() {
		return "textures/" + addDefaultSuffix(super.path(), "png");
	}
}
