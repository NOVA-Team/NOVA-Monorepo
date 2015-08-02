package nova.core.wrapper.mc18.wrapper.entity.forward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.wrapper.mc18.render.RenderUtility;
import nova.core.wrapper.mc18.wrapper.render.BWModel;
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
		super(Minecraft.getMinecraft().getRenderManager());
	}

	public static void render(Entity wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
		Optional<DynamicRenderer> opRenderer = entity.getOp(DynamicRenderer.class);

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
