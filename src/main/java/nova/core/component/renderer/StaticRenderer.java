package nova.core.component.renderer;

import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * This interface specifies that a block requires custom static rendering.
 * This type of rendering only updates its render state every time the world changes (block change)
 * See {@link StaticBlockRenderer}
 */
public class StaticRenderer extends Component {

	public final ComponentProvider provider;

	/**
	 * Called when this block is to be rendered.
	 * model - A {@link nova.core.render.model.Model} to use.
	 */
	public Consumer<Model> onRender = model -> {
	};

	public StaticRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	public StaticRenderer onRender(Consumer<Model> onRender) {
		this.onRender = onRender;
		return this;
	}
}
