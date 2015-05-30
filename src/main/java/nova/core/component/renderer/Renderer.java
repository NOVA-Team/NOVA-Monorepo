package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * @author Calclavia
 */
public abstract class Renderer extends Component {
	/**
	 * Called to render.
	 * model - A {@link nova.core.render.model.Model} to use
	 */
	public Consumer<Model> onRender = model -> {};

	public Renderer setOnRender(Consumer<Model> onRender) {
		this.onRender = onRender;
		return this;
	}
}
