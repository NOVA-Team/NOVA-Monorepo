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

package nova.core.wrapper.mc.forge.v18.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.core.wrapper.mc.forge.v18.render.RenderUtility;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public abstract class FWSmartModel implements IFlexibleBakedModel {

	protected final VertexFormat format;
	// Default item transforms. Can be changed in subclasses.
	protected ItemCameraTransforms itemCameraTransforms = ItemCameraTransforms.DEFAULT;

	public FWSmartModel() {
		this.format = new VertexFormat();
	}

	public static int[] vertexToInts(Vertex vertex, TextureAtlasSprite texture) {
		return new int[] {
			Float.floatToRawIntBits((float) vertex.vec.getX()),
			Float.floatToRawIntBits((float) vertex.vec.getY()),
			Float.floatToRawIntBits((float) vertex.vec.getZ()),
			vertex.color.rgba(),
			Float.floatToRawIntBits(texture.getInterpolatedU(16 * vertex.uv.getX())),
			Float.floatToRawIntBits(texture.getInterpolatedV(16 * vertex.uv.getY())),
			0
		};
	}

	protected List<BakedQuad> modelToQuads(Model modelIn) {
		return modelIn
			.flatten()
			.stream()
			.flatMap(
				model -> {
					if (model instanceof MeshModel) {
						MeshModel meshModel = (MeshModel) model;
						return meshModel.faces
							.stream()
							.map(
								face -> {
									TextureAtlasSprite texture = face.texture.map(RenderUtility.instance::getTexture)
										.orElse(Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
									List<int[]> vertexData = face.vertices
										.stream()
										.map(v -> vertexToInts(v, texture))
										.collect(Collectors.toList());

									int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
									//TODO: The facing might be wrong
									return new BakedQuad(data, -1, EnumFacing.values()[Direction.fromVector(face.normal).ordinal()]);
								}
							);
					}
					//TODO: Handle BW Rendering
					return Stream.empty();
				}
			)
			.collect(Collectors.toList());
	}

	@Override
	public VertexFormat getFormat() {
		return format;
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing facing) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return itemCameraTransforms;
	}
}
