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

package nova.core.wrapper.mc.forge.v1_8.wrapper.render;

import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.wrapper.mc.forge.v1_8.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_8.wrapper.DirectionConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public abstract class FWSmartModel implements IFlexibleBakedModel {

	protected static final VertexFormat NOVA_VERTEX_FORMAT = DefaultVertexFormats.ITEM;

	protected final VertexFormat format;
	// Default item transforms. Can be changed in subclasses.
	@SuppressWarnings("deprecation")
	protected net.minecraft.client.renderer.block.model.ItemCameraTransforms itemCameraTransforms
		= net.minecraft.client.renderer.block.model.ItemCameraTransforms.DEFAULT;

	protected FWSmartModel(VertexFormat format) {
		this.format = format;
	}

	public FWSmartModel() {
		this(NOVA_VERTEX_FORMAT);
	}

	public static int[] vertexToInts(Vertex vertex, TextureAtlasSprite texture, Vector3D normal) {
		// TODO: Allow serialization of arbitrary vertex formats.
		if (vertex.normal.isPresent())
			normal = vertex.normal.get();
		return new int[] {
			Float.floatToRawIntBits((float) vertex.vec.getX()),
			Float.floatToRawIntBits((float) vertex.vec.getY()),
			Float.floatToRawIntBits((float) vertex.vec.getZ()),
			vertex.color.rgba(),
			Float.floatToRawIntBits(texture.getInterpolatedU(16 * vertex.uv.getX())),
			Float.floatToRawIntBits(texture.getInterpolatedV(16 * vertex.uv.getY())),
			((((byte)(normal.getX() * 127)) & 0xFF) |
			((((byte)(normal.getY() * 127)) & 0xFF) << 8) |
			((((byte)(normal.getZ() * 127)) & 0xFF) << 16))
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
							.filter(f -> f.vertices.size() >= 3) // Only render faces with at least 3 vertices
							.map(
								face -> {
									TextureAtlasSprite texture = face.texture.map(RenderUtility.instance::getTexture)
										.orElseGet(Minecraft.getMinecraft().getTextureMapBlocks()::getMissingSprite);
									List<int[]> vertexData = face.vertices
										.stream()
										.map(v -> vertexToInts(v, texture, face.normal))
										.collect(Collectors.toList());

									if (vertexData.size() < 4)
										// Do what Minecraft Forge does when rendering Wavefront OBJ models with triangles
										vertexData.add(vertexData.get(vertexData.size() - 1));

									int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
									//TODO: The facing might be wrong        // format.getNextOffset() is in byte count per vertex, and we are deling with ints, so we don't need to multiply by 4
									return new BakedQuad(Arrays.copyOf(data, MathUtil.min(data.length, format.getNextOffset())),
										-1, DirectionConverter.instance().toNative(Direction.fromVector(face.normal)));
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
		// Interface doesn't have the @Nullable annotation
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@Override
	@SuppressWarnings("deprecation")
	public net.minecraft.client.renderer.block.model.ItemCameraTransforms getItemCameraTransforms() {
		return itemCameraTransforms;
	}
}
