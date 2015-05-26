package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * This interface signals that a block needs dynamic rendering.
 */
public class DynamicRenderer extends Component {

	public final ComponentProvider provider;

	/**
	 * Called for a dynamic render.
	 * model - A {@link nova.core.render.model.Model} to use
	 */
	public Consumer<Model> onRender = model -> {
	};

	public DynamicRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	public DynamicRenderer onRender(Consumer<Model> onRender) {
		this.onRender = onRender;
		return this;
	}
}
