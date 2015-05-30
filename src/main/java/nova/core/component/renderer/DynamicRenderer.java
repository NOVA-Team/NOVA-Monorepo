package nova.core.component.renderer;

import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * This interface signals that a block needs dynamic rendering.
 */
public class DynamicRenderer extends Renderer {

	public final ComponentProvider provider;

	public DynamicRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	public DynamicRenderer onRender(Consumer<Model> onRender) {
		this.onRender = onRender;
		return this;
	}
}
