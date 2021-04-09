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
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nova.core.render.Color;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.util.Direction;
import nova.core.util.math.MathUtil;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.DirectionConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
@SuppressWarnings("deprecation")
public abstract class FWSmartModel implements IBakedModel {

	protected static final VertexFormat NOVA_VERTEX_FORMAT = DefaultVertexFormats.ITEM;

	protected final VertexFormat format;
	// Default item transforms. Can be changed in subclasses.
	protected ItemCameraTransforms itemCameraTransforms = ItemCameraTransforms.DEFAULT;
	private final FWOverrideList overrides;

	protected FWSmartModel(VertexFormat format) {
		this.format = format;
		this.overrides = new FWOverrideList();
	}

	public FWSmartModel() {
		this(NOVA_VERTEX_FORMAT);
	}

	public static int[] vertexToInts(Vertex vertex, TextureAtlasSprite texture, Vector3D normal) {
		// TODO: Possibly support arbitrary `VertexFormat`
//		if (vertex.normal.isPresent())
//			normal = vertex.normal.get();
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

	public abstract IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity);

	protected List<BakedQuad> modelToQuads(Model modelIn) {
		return modelToQuads(modelIn, Optional.empty());
	}

	protected List<BakedQuad> modelToQuads(Model modelIn, Color colorMultiplier) {
		return modelToQuads(modelIn, Optional.of(colorMultiplier));
	}

	protected List<BakedQuad> modelToQuads(Model modelIn, Optional<Color> colorMultiplier) {
		return modelIn
			.flatten()
			.stream()
			.flatMap(
				model -> {
					if (model instanceof MeshModel) {
						MeshModel meshModel = (MeshModel) model;
						return meshModel.faces
							.stream()
							.filter(f -> f.vertices.size() > 2)
							.map(
								face -> {
									TextureAtlasSprite texture = face.texture.map(RenderUtility.instance::getTexture)
										.orElseGet(Minecraft.getMinecraft().getTextureMapBlocks()::getMissingSprite);
									List<int[]> vertexData = face.vertices
										.stream()
										.map(v -> {
											colorMultiplier.ifPresent(c -> v.color = v.color.multiply(c));
											return v;
										})
										.map(v -> vertexToInts(v, texture, face.normal))
										.collect(Collectors.toList());

									if (vertexData.size() < 4)
										vertexData.add(vertexData.get(vertexData.size() - 1));

									int[] data = Ints.concat(vertexData.toArray(new int[][] {}));
									//TODO: The facing might be wrong
									return new BakedQuad(Arrays.copyOf(data, MathUtil.min(data.length, format.getNextOffset())), -1,
										DirectionConverter.instance().toNative(Direction.fromVector(face.normal)), texture, true, getFormat());
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
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms() {
		return itemCameraTransforms;
	}

	@Override
	public final ItemOverrideList getOverrides() {
		return overrides;
	}

	private final class FWOverrideList extends ItemOverrideList {
		private FWOverrideList() {
			super(Collections.emptyList());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			return FWSmartModel.this.handleItemState(originalModel, stack, world, entity);
		}
	}
}
