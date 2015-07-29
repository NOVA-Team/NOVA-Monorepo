package nova.core.component.renderer;

import nova.core.component.ComponentProvider;
import nova.core.render.pipeline.RenderStream;

/**
 * This interface specifies that a block requires custom static rendering.
 * This type of rendering only updates its render state every time the world changes (block change)
 * See {@link RenderStream} to easily pipeline models to prepare it for rendering
 */
public class StaticRenderer extends Renderer {

	public final ComponentProvider provider;

	//TODO: We don't need provider
	public StaticRenderer(ComponentProvider provider) {
		this.provider = provider;
	}
}
