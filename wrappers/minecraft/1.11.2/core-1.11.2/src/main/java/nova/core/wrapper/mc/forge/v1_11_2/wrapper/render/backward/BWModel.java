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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.render.model.CustomModel;
import nova.core.render.model.MeshModel;
import nova.core.render.model.Vertex;
import nova.core.render.texture.EntityTexture;
import nova.core.util.math.Vector3DUtil;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.AssetConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * BWModel for dynamic rendering
 * @author Calclavia
 */
public class BWModel extends MeshModel {

	public void render() {
		render(Optional.empty(), Optional.empty());
	}

	public void render(@Nonnull IBlockAccess access) {
		render(Optional.of(access), Optional.empty());
	}

	public void render(@Nonnull RenderManager entityRenderManager) {
		render(Optional.ofNullable(entityRenderManager.world), Optional.of(entityRenderManager));
	}

	public void render(Optional<IBlockAccess> access, Optional<RenderManager> entityRenderManager) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getBuffer();
		worldRenderer.color(1F, 1F, 1F, 1F);

		/**
		 * Convert textures and UV into Minecraft equivalent.
		 */
		flatten().forEach(
			model -> {
				if (model instanceof MeshModel) {
					MeshModel meshModel = (MeshModel) model;
					meshModel.faces.forEach(face -> {
						boolean isEntityTexture = face.texture.map(t -> t instanceof EntityTexture).orElse(false);
						if (entityRenderManager.isPresent() && isEntityTexture) {
							entityRenderManager.get().renderEngine.bindTexture(AssetConverter.instance().toNative(face.texture.get()));
						}

						final List<Vertex> vertices;
						switch (worldRenderer.getDrawMode()) {
							case GL11.GL_TRIANGLES:
								vertices = new ArrayList<>(3);
								vertices.add(face.vertices.get(0));
								vertices.add(face.vertices.get(1));
								vertices.add(face.vertices.get(2));
								break;
							case GL11.GL_QUADS:
								vertices = new ArrayList<>(4);
								vertices.add(face.vertices.get(0));
								vertices.add(face.vertices.get(1));
								vertices.add(face.vertices.get(2));
								if (face.vertices.size() >= 4)
									vertices.add(face.vertices.get(3));
								else
									vertices.add(face.vertices.get(2));
								break;
							default:
								vertices = new ArrayList<>(face.vertices);
								break;
						}
						vertices.forEach(v -> {
							worldRenderer.getVertexFormat().getElements().forEach(vfe -> {
								switch (vfe.getUsage())	{
									case POSITION: {
										worldRenderer.pos(v.vec.getX(), v.vec.getY(), v.vec.getZ());
										break;
									} case NORMAL: {
										Vector3D normal = face.normal;//v.normal.orElse(face.normal);
										worldRenderer.normal((float)normal.getX(), (float)normal.getY(), (float)normal.getZ());
										break;
									} case COLOR: {
										worldRenderer.color(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
										break;
									} case UV: {
										if (vfe.getIndex() == 0) {
											if (entityRenderManager.isPresent() && isEntityTexture) {
												//We're not working on an atlas, so just do... this.
												worldRenderer.tex(v.uv.getX(), v.uv.getY());
											} else {
												TextureAtlasSprite tex = face.texture.map(RenderUtility.instance::getTexture)
													.orElseGet(Minecraft.getMinecraft().getTextureMapBlocks()::getMissingSprite);
												worldRenderer.tex(tex.getInterpolatedU(16 * v.uv.getX()), tex.getInterpolatedV(16 * v.uv.getY()));
											}
										} else if (vfe.getIndex() == 1) {
											if (face.getBrightness() >= 0) {
												worldRenderer.lightmap((int)(face.getBrightness() * 15), (int)(face.getBrightness() * 11));
											} else if(access.isPresent()) {
												// Determine nearest adjacent block.
												Vector3D nearestPos = Vector3DUtil.floor(face.getCenter().add(face.normal.scalarMultiply(0.05)));
												BlockPos blockPos = VectorConverter.instance().toNative(nearestPos);
												IBlockState state = access.get().getBlockState(blockPos);
												int brightness = state.getPackedLightmapCoords(access.get(), blockPos);

												// TODO: Add Ambient Occlusion
											/*
											int aoBrightnessXYNN = state.getPackedLightmapCoords(access.get(), blockPos.east());
											int aoBrightnessYZNN = state.getPackedLightmapCoords(access.get(), blockPos.north());
											int aoBrightnessYZNP = state.getPackedLightmapCoords(access.get(), blockPos.south());
											int aoBrightnessXYPN = state.getPackedLightmapCoords(access.get(), blockPos.west());

											int brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, i1);
											int brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, i1);
											int brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, i1);
											int brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, i1);
											*/

												worldRenderer.lightmap((brightness >>> 20) & 0xFFFF, (brightness >>> 4) & 0xFFFF);
											} else {
												worldRenderer.lightmap(15, 11);
											}
										}
										break;
									}
								}
							});
							worldRenderer.endVertex();
						});
					});
				} else if (model instanceof CustomModel) {
					CustomModel customModel = (CustomModel) model;
					customModel.render.accept(customModel);
				}
			}
		);
	}
}
