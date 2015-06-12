package nova.core.component.renderer;

import nova.core.component.ComponentProvider;
import nova.core.render.texture.ItemTexture;

import java.util.Optional;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
public class ItemRenderer extends Renderer {

	/**
	 * If there is no texture provided, it will not render any and default to onRender() method for custom item rendering.
	 * @return {@link ItemTexture} instance
	 */
	public Optional<ItemTexture> texture = Optional.empty();

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

	public ItemRenderer setTexture(ItemTexture texture) {
		this.texture = Optional.of(texture);
		return this;
	}
}
