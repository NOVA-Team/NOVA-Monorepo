package nova.core.component.renderer;

import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * This interface specifies that a block requires custom static rendering.
 * This type of rendering only updates its render state every time the world changes (block change)
 * See {@link StaticBlockRenderer}
 */
public class StaticRenderer extends Renderer {

	public final ComponentProvider provider;

	public StaticRenderer(ComponentProvider provider) {
		this.provider = provider;
	}
}
