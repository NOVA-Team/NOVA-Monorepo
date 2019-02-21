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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.forward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.wrapper.mc.forge.v1_11_2.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.particle.forward.FWParticle;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWModel;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

/**
 * Renders entities.
 * @author Calclavia
 */
public class FWEntityRenderer extends Render<FWEntity> {
	public static final IRenderFactory<FWEntity> instance = FWEntityRenderer::new;

	public FWEntityRenderer(RenderManager manager) {
		super(manager);
	}

	public static void render(FWParticle wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
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

			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.ITEM);
			model.render(Minecraft.getMinecraft().getRenderManager());
			Tessellator.getInstance().draw();

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				RenderUtility.disableBlending();
			}
		}
	}

	public static void render(FWEntity wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
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

			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.ITEM);
			model.render(Minecraft.getMinecraft().getRenderManager());
			Tessellator.getInstance().draw();

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				RenderUtility.disableBlending();
			}
		}
	}

	@Override
	public void doRender(FWEntity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		render(entity, entity.wrapped, x, y, z);
	}

	@Override
	protected ResourceLocation getEntityTexture(FWEntity entity) {
		return null;
	}
}
