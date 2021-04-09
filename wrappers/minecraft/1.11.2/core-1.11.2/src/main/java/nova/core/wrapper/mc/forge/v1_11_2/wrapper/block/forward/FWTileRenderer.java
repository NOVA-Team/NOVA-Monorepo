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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import nova.core.block.Block;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWModel;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * @author Calclavia
 */
public class FWTileRenderer extends TileEntitySpecialRenderer<FWTile> {

	public static final FWTileRenderer instance = new FWTileRenderer();

	@Override
	public void renderTileEntityAt(FWTile te, double x, double y, double z, float partialTicks, int destroyStage) {
		Block block = te.getBlock();
		Optional<DynamicRenderer> opRenderer = block.components.getOp(DynamicRenderer.class);
		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix.translate(x + 0.5, y + 0.5, z + 0.5);
			opRenderer.get().onRender.accept(model);
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			RenderUtility.enableBlending();
			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.BLOCK);
			model.render(te.getWorld());
			Tessellator.getInstance().draw();
			RenderUtility.disableBlending();
		}
	}
}
