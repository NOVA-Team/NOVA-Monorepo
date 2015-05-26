package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
public class ItemRenderer extends Component {

	public final ComponentProvider provider;

	/**
	 * Called when the item of this block is to be rendered.
	 * model - A {@link Model} to use.
	 */
	public Consumer<Model> onRender = this::renderItem;

	public ItemRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	protected void renderItem(Model model) {
		Optional<StaticRenderer> opComponent = provider.getOp(StaticRenderer.class);
		if (opComponent.isPresent()) {
			opComponent.get().onRender.accept(model);
		} else {
			provider.getOp(DynamicRenderer.class).ifPresent(c -> c.onRender.accept(model));
		}
	}

	public ItemRenderer onRender(Consumer<Model> onRender) {
		this.onRender = onRender;
		return this;
	}
}
