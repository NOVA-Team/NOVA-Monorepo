package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.Require;
import nova.core.component.renderer.StaticRenderer;
import nova.core.component.transform.Orientation;
import nova.core.render.model.BlockModelUtil;
import nova.core.render.model.Model;

/**
 * Renders a block with rotation based on its direction
 * @author Calclavia
 */
@Require(Orientation.class)
class RotatedRenderer extends StaticRenderer {

	public RotatedRenderer(Block provider) {
		super(provider);
		onRender = this::renderStatic;
	}

	protected void renderStatic(Model model) {
		BlockModelUtil.drawBlock(model, (Block) provider);
		model.rotate(provider.get(Orientation.class).orientation().rotation);
	}
}
