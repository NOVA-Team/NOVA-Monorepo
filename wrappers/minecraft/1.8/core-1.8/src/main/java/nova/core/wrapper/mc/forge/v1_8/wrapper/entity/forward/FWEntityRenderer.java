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

package nova.core.wrapper.mc.forge.v1_8.wrapper.entity.forward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.wrapper.mc.forge.v1_8.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_8.wrapper.render.BWModel;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

/**
 * Renders entities.
 * @author Calclavia
 */
public class FWEntityRenderer extends Render {
	public static final FWEntityRenderer instance = new FWEntityRenderer();

	public FWEntityRenderer() {
		super(FMLClientHandler.instance().getClient().getRenderManager());
	}

	public static void render(Entity wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
		Optional<DynamicRenderer> opRenderer = entity.components.getOp(DynamicRenderer.class);

		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix
				.translate(x, y, z)
				.scale(entity.scale())
				.translate(entity.pivot())
				.rotate(entity.rotation())
				.translate(entity.pivot().negate());

			opRenderer.get().onRender.accept(model);

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				glShadeModel(GL_SMOOTH);
				glEnable(GL_BLEND);
				GL11.glBlendFunc(model.blendSFactor, model.blendDFactor);
			}

			Tessellator.getInstance().getWorldRenderer().startDrawingQuads();
			model.render(Optional.of(Minecraft.getMinecraft().getRenderManager()));
			Tessellator.getInstance().draw();

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				RenderUtility.disableBlending();
			}
		}
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		if (entity instanceof FWEntity) {
			render(entity, ((FWEntity) entity).wrapped, x, y, z);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
}
