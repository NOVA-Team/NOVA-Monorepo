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

package nova.core.render.model;

import nova.core.util.Asset;

import java.io.InputStream;

/**
 * All models must be included in /assets/domain/models/
 *
 * @author Calclavia
 */
public abstract class ModelProvider extends Asset {

	/**
	 * Creates new ModelProvider
	 *
	 * @param domain dolain of the assets.
	 * @param name name of the model.
	 */
	public ModelProvider(String domain, String name) {
		super(domain, name);
	}

	/**
	 * Loads the model with a input stream.
	 *
	 * @param stream The {@link InputStream}
	 */
	public abstract void load(InputStream stream);

	public abstract Model getModel();

	public abstract String getType();

	@Override
	public String path() {
		return "models/" + addDefaultSuffix(super.path(), getType());
	}
}
