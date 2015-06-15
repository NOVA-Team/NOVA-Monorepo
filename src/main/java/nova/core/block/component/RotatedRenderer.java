package nova.core.block.component;

import nova.core.block.Block;
import nova.core.component.Require;
import nova.core.component.renderer.Renderer;
import nova.core.component.transform.Orientation;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * Renders a block withPriority rotation based on its direction
 * @author Calclavia
 */
@Require(Orientation.class)
public class RotatedRenderer extends StaticBlockRenderer {

	public Consumer<Model> rotateModel = model -> model.matrix.rotate(provider.get(Orientation.class).orientation().rotation);

	/**
	 * @param provider block to render rotated
	 */
	public RotatedRenderer(Block provider) {
		super(provider);
		setOnRender(onRender);
	}

	@Override
	public Renderer setOnRender(Consumer<Model> onRender) {
		return super.setOnRender(rotateModel.andThen(onRender));
	}
}
