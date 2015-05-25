package nova.wrapper.mc1710.forward.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import nova.core.block.components.DynamicRenderer;
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
		if (entity instanceof DynamicRenderer) {
			BWModel model = new BWModel();
			model.matrix = new MatrixStack().translate(x, y, z).rotate(entity.transform.rotation()).getMatrix();
			((DynamicRenderer) entity).renderDynamic(model);
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
