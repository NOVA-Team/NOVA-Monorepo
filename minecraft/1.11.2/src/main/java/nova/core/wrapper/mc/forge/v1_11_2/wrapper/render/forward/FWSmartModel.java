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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward;

import com.google.common.primitives.Ints;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import nova.core.render.model.CustomModel;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
@SuppressWarnings("deprecation")
public abstract class FWSmartModel implements IBakedModel {

	protected static final VertexFormat NOVA_VERTEX_FORMAT;

	protected final VertexFormat format;
	// Default item transforms. Can be changed in subclasses.
	protected ItemCameraTransforms itemCameraTransforms = ItemCameraTransforms.DEFAULT;

	static {
		NOVA_VERTEX_FORMAT = DefaultVertexFormats.ITEM;
//		NOVA_VERTEX_FORMAT = new VertexFormat();
//		NOVA_VERTEX_FORMAT.addElement(DefaultVertexFormats.POSITION_3F);
//		NOVA_VERTEX_FORMAT.addElement(DefaultVertexFormats.COLOR_4UB);
//		NOVA_VERTEX_FORMAT.addElement(DefaultVertexFormats.TEX_2F);
//		NOVA_VERTEX_FORMAT.addElement(DefaultVertexFormats.NORMAL_3B);
//		NOVA_VERTEX_FORMAT.addElement(DefaultVertexFormats.PADDING_1B);
	}

	protected FWSmartModel(VertexFormat format) {
		this.format = format;
	}

	public FWSmartModel() {
		this.format = DefaultVertexFormats.ITEM;
	}

	public static int[] vertexToInts(Vertex vertex, TextureAtlasSprite texture, Vector3D normal) {
		return new int[] {
			Float.floatToRawIntBits((float) vertex.vec.getX()),
			Float.floatToRawIntBits((float) vertex.vec.getY()),
			Float.floatToRawIntBits((float) vertex.vec.getZ()),
			vertex.color.argb(),
			Float.floatToRawIntBits(texture.getInterpolatedU(16 * vertex.uv.getX())),
			Float.floatToRawIntBits(texture.getInterpolatedV(16 * vertex.uv.getY())),
			0 // TODO: Normal
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
										.map(v -> vertexToInts(v, texture, face.normal))
										.collect(Collectors.toList());

									int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
									//TODO: The facing might be wrong
									return new BakedQuad(Arrays.copyOf(data, MathUtil.max(data.length, 0)), -1, EnumFacing.values()[Direction.fromVector(face.normal).ordinal()],
											getParticleTexture(), true, getFormat());
								}
							);
					}
					//TODO: Handle BW Rendering
					return Stream.empty();
				}
			)
			.collect(Collectors.toList());
	}

	public VertexFormat getFormat() {
		return format;
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
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return itemCameraTransforms;
	}
}
