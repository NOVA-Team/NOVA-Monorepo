package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.util.math.MatrixStack;
import nova.wrapper.mc1710.backward.render.BWModel;
import nova.wrapper.mc1710.render.RenderUtility;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

/**
 * Renders entities.
 *
 * @author Calclavia
 */
public class FWEntityRenderer extends Render {
	public static final FWEntityRenderer instance = new FWEntityRenderer();

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		if (entity instanceof FWEntity) {
			render(entity, ((FWEntity) entity).wrapped, x, y, z);
		}
	}

	public static void render(Entity wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
		Optional<DynamicRenderer> opRenderer = entity.getOp(DynamicRenderer.class);

		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack()
				.translate(x, y, z)
				.scale(entity.scale())
				.translate(entity.pivot())
				.rotate(entity.rotation())
				.translate(entity.pivot().negate())
				.getMatrix();

			opRenderer.get().onRender.accept(model);

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				glShadeModel(GL_SMOOTH);
				glEnable(GL_BLEND);
				GL11.glBlendFunc(model.blendSFactor, model.blendDFactor);
			}

			Tessellator.instance.startDrawingQuads();
			model.render(Optional.of(RenderManager.instance));
			Tessellator.instance.draw();

			if (model.blendSFactor > 0 && model.blendDFactor > 0) {
				RenderUtility.disableBlending();
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
}
