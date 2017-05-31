/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */
package nova.core.util;

import nova.core.render.model.MeshModel;
import nova.core.render.pipeline.BlockRenderPipeline;
import nova.core.render.texture.Texture;

/**
 * @author ExE Boss
 */
public class ModelUtil {

	public static MeshModel getMissingModel() {
		return BlockRenderPipeline.drawCube(new MeshModel()).bind(new Texture("nova", "null"));
	}
}
