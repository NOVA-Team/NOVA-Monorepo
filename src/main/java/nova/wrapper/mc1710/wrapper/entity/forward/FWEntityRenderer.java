package nova.wrapper.mc1710.wrapper.entity.forward;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import nova.core.component.renderer.DynamicRenderer;
import nova.core.util.transform.matrix.MatrixStack;
import nova.wrapper.mc1710.backward.render.BWModel;

import java.util.Optional;

/**
 * Renders entities.
 * @author Calclavia
 */
public class FWEntityRenderer extends Render {
	public static final FWEntityRenderer instance = new FWEntityRenderer();

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
		if (entity instanceof FWEntity) {
			render(entity, ((FWEntity) entity).wrapped, x, y, z);
		} else if (entity instanceof FWEntityFX) {
			render(entity, ((FWEntityFX) entity).wrapped, x, y, z);
		}
	}

	private void render(Entity wrapper, nova.core.entity.Entity entity, double x, double y, double z) {
		Optional<DynamicRenderer> opRenderer = entity.getOp(DynamicRenderer.class);

		if (opRenderer.isPresent()) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack()
				.translate(entity.pivot())
				.rotate(entity.rotation())
				.translate(entity.pivot().negate())
				.translate(x, y, z)
				.scale(entity.scale())
				.getMatrix();

			opRenderer.get().onRender.accept(model);
			Tessellator.instance.startDrawingQuads();
			model.render(Optional.of(renderManager));
			Tessellator.instance.draw();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
}
