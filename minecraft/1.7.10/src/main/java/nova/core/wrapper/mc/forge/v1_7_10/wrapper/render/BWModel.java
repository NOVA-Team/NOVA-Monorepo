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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import nova.core.render.model.CustomModel;
import nova.core.render.model.MeshModel;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.Texture;
import nova.core.util.math.Vector3DUtil;
import nova.core.wrapper.mc.forge.v1_7_10.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.assets.AssetConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * BWModel for dynamic rendering
 * @author Calclavia
 */
public class BWModel extends MeshModel {

	/**
	 * Completes this rendering for a block.
	 *
	 * @param blockAccess The {@link IBlockAccess} instance.
	 * @throws NullPointerException If {@code blockAccess} is {@code null}
	 */
	public void render(IBlockAccess blockAccess) {
		render(Optional.of(blockAccess), Optional.empty());
	}

	public void render() {
		render(Optional.empty(), Optional.empty());
	}

	public void render(Optional<RenderManager> entityRenderManager) {
		render(Optional.empty(), entityRenderManager);
	}

	public void render(Optional<IBlockAccess> access, Optional<RenderManager> entityRenderManager) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		/**
		 * Convert textures and UV into Minecraft equivalent.
		 */
		flatten().forEach(
			model -> {
				if (model instanceof MeshModel) {
					MeshModel meshModel = (MeshModel) model;
					meshModel.faces.forEach(face -> {
						// Brightness is defined as: skyLight << 20 | blockLight << 4
						if (face.getBrightness() >= 0) {
							tessellator.setBrightness((int) (face.getBrightness() * (15 << 20 | 11 << 4)));
						} else if (access.isPresent()) {
							// Determine nearest adjacent block.
							Vector3D nearestPos = Vector3DUtil.floor(face.getCenter().add(face.normal.scalarMultiply(0.05)));
							Block block = access.get().getBlock((int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ());
							int brightness = block.getMixedBrightnessForBlock(access.get(), (int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ());

							// TODO: Add Ambient Occlusion
						/*
						int aoBrightnessXYNN = block.getMixedBrightnessForBlock(access.get(), (int) nearestPos.getX() - 1, (int) nearestPos.getY(), (int) nearestPos.getZ());
						int aoBrightnessYZNN = block.getMixedBrightnessForBlock(access.get(), (int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ() - 1);
						int aoBrightnessYZNP = block.getMixedBrightnessForBlock(access.get(), (int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ() + 1);
						int aoBrightnessXYPN = block.getMixedBrightnessForBlock(access.get(), (int) nearestPos.getX() + 1, (int) nearestPos.getY(), (int) nearestPos.getZ());

						int brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, i1);
						int brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, i1);
						int brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, i1);
						int brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, i1);
						*/

							tessellator.setBrightness(brightness);

						} else {
							// Determine nearest adjacent block.
							tessellator.setBrightness(15 << 20 | 11 << 4);
						}

						tessellator.setNormal((float) face.normal.getX(), (float) face.normal.getY(), (float) face.normal.getZ());

						if (face.texture.isPresent()) {
							if (entityRenderManager.isPresent() && face.texture.get() instanceof EntityTexture) {
								//We're not working on an atlas, so just do... this.
								Texture t = face.texture.get();
								entityRenderManager.get().renderEngine.bindTexture(AssetConverter.instance().toNative(t));
								face.vertices.forEach(
									v -> {
										if (v.normal.isPresent())
											tessellator.setNormal((float) v.normal.get().getX(), (float) v.normal.get().getY(), (float) v.normal.get().getZ());
										else
											tessellator.setNormal((float) face.normal.getX(), (float) face.normal.getY(), (float) face.normal.getZ());

										tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
										tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), v.uv.getX(), v.uv.getY());
									}
								);
							} else {
								Texture texture = face.texture.get();
								IIcon icon = RenderUtility.instance.getIcon(texture);
								face.vertices.forEach(
									v -> {
										if (v.normal.isPresent())
											tessellator.setNormal((float) v.normal.get().getX(), (float) v.normal.get().getY(), (float) v.normal.get().getZ());
										else
											tessellator.setNormal((float) face.normal.getX(), (float) face.normal.getY(), (float) face.normal.getZ());

										tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
										if (icon != null) {
											tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), icon.getInterpolatedU(16 * v.uv.getX()), icon.getInterpolatedV(16 * v.uv.getY()));
										} else {
											tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), v.uv.getX(), v.uv.getY());
										}
									}
								);
							}
						} else {
							face.vertices.forEach(
								v -> {
									if (v.normal.isPresent())
										tessellator.setNormal((float) v.normal.get().getX(), (float) v.normal.get().getY(), (float) v.normal.get().getZ());
									else
										tessellator.setNormal((float) face.normal.getX(), (float) face.normal.getY(), (float) face.normal.getZ());

									tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
									tessellator.addVertex(v.vec.getX(), v.vec.getY(), v.vec.getZ());
								}
							);
						}
					});
				} else if (model instanceof CustomModel) {
					CustomModel customModel = (CustomModel) model;
					customModel.render.accept(customModel);
				}
			}
		);
	}

	public int getAoBrightness(int a, int b, int c, int d) {
		if (a == 0) {
			a = d;
		}

		if (b == 0) {
			b = d;
		}

		if (c == 0) {
			c = d;
		}

		return a + b + c + d >> 2 & 16711935;
	}
}
