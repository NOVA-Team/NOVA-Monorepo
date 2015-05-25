package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

/**
 * This interface signals that a block needs dynamic rendering.
 */
public class DynamicRenderer extends Component {

	public final ComponentProvider provider;

	public DynamicRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	/**
	 * Called for a dynamic render.
	 * @param model A {@link nova.core.render.model.Model} to use
	 */
	public void renderDynamic(Model model) {

	}
}
