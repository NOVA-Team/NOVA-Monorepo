package nova.core.component.renderer;

import nova.core.component.ComponentProvider;

/**
 * This interface signals that a block needs dynamic rendering.
 */
public class DynamicRenderer extends Renderer {

	public final ComponentProvider provider;

	public DynamicRenderer(ComponentProvider provider) {
		this.provider = provider;
	}
}
