package nova.core.component.renderer;

import nova.core.component.ComponentProvider;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;

import java.util.Optional;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
//Item renderer should not exist. Instead, blocks should just edit the default ItemBlock renderer...
@Deprecated
public class ItemRenderer extends Renderer {

	/**
	 * If there is no texture provided, it will not render any and default to onRender() method for custom item rendering.
	 * <p>
	 * return - {@link ItemTexture} instance
	 */
	public Optional<Texture> texture = Optional.empty();

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

	public ItemRenderer setTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}
}
