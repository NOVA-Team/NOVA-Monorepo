package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.Require;
import nova.core.component.renderer.StaticRenderer;
import nova.core.render.model.BlockModelUtil;
import nova.core.render.model.Model;

/**
 * Renders a block with rotation based on its direction
 * @author Calclavia
 */
@Require(Oriented.class)
class RotatedRenderer extends StaticRenderer {

	public RotatedRenderer(Block provider) {
		super(provider);
	}

	@Override
	public void renderStatic(Model model) {
		BlockModelUtil.drawBlock(model, (Block) provider);
		model.rotate(provider.getComponent(Oriented.class).get().direction.rotation);
	}
}
