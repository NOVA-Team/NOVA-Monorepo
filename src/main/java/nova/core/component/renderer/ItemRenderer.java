package nova.core.component.renderer;

import nova.core.component.ComponentProvider;

import java.util.Optional;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
public class ItemRenderer extends Renderer {

	public ItemRenderer() {

	}

	public ItemRenderer(ComponentProvider provider) {
		onRender = model -> {
			Optional<StaticRenderer> opComponent = provider.getOp(StaticRenderer.class);
			if (opComponent.isPresent()) {
				opComponent.get().onRender.accept(model);
			} else {
				provider.getOp(DynamicRenderer.class).ifPresent(c -> c.onRender.accept(model));
			}
		};
	}
}
