package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

/**
 * This interface specifies that a block requires custom static rendering.
 * This type of rendering only updates its render state every time the world changes (block change)
 * See {@Link StaticBlockRenderer}
 */
public class StaticRenderer extends Component {

	public final ComponentProvider provider;

	public StaticRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	/**
	 * Called when this block is to be rendered.
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public void renderStatic(Model model) {

	}
}
