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

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import nova.core.render.model.CustomModel;
import nova.core.render.model.MeshModel;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.Texture;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.assets.AssetConverter;

import java.util.Optional;

/**
 * BWModel for dynamic rendering
 * @author Calclavia
 */
public class BWModel extends MeshModel {

	public void render() {
		render(Optional.empty());
	}

	public void render(Optional<RenderManager> entityRenderManager) {
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
						// TODO: See if this works, and possibly fix it
						// Brightness is defined as: skyLight << 20 | blockLight << 4
						if (face.getBrightness() >= 0) {
							worldRenderer.lightmap((int)(face.getBrightness() * (15 << 20)), (int)(face.getBrightness() * (11 << 4)));
							//worldRenderer.setBrightness((int) (face.getBrightness() * (15 << 20 | 11 << 4)));
						} else {
							// Determine nearest adjacent block.
							worldRenderer.lightmap(15 << 20, 11 << 4);
							//worldRenderer.setBrightness(15 << 20 | 11 << 4);
						}

						worldRenderer.normal((float) face.normal.getX(), (float) face.normal.getY(), (float) face.normal.getZ());

						System.out.println(face.texture);
						if (face.texture.isPresent()) {
							if (entityRenderManager.isPresent() && face.texture.get() instanceof EntityTexture) {
								//We're not working on an atlas, so just do... this.
								Texture t = face.texture.get();
								entityRenderManager.get().renderEngine.bindTexture(AssetConverter.instance().toNative(t));
								face.vertices.forEach(
									v -> {
										worldRenderer.color(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
										worldRenderer.tex(v.uv.getX(), v.uv.getY());
										worldRenderer.pos(v.vec.getX(), v.vec.getY(), v.vec.getZ());
									}
								);
							} else {
								Texture texture = face.texture.get();
								TextureAtlasSprite icon = RenderUtility.instance.getTexture(texture);
								face.vertices.forEach(
									v -> {
										worldRenderer.color(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
										if (icon != null) {
											worldRenderer.tex(icon.getInterpolatedU(16 * v.uv.getX()), icon.getInterpolatedV(16 * v.uv.getY()));
											worldRenderer.pos(v.vec.getX(), v.vec.getY(), v.vec.getZ());
										} else {
											worldRenderer.tex(v.uv.getX(), v.uv.getY());
											worldRenderer.pos(v.vec.getX(), v.vec.getY(), v.vec.getZ());
										}
									}
								);
							}
						} else {
							face.vertices.forEach(
								v -> {
									worldRenderer.color(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
									worldRenderer.pos(v.vec.getX(), v.vec.getY(), v.vec.getZ());
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
}
