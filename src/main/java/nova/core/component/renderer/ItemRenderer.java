package nova.core.component.renderer;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.render.model.Model;

import java.util.Optional;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
public class ItemRenderer extends Component {

	public final ComponentProvider provider;

	public ItemRenderer(ComponentProvider provider) {
		this.provider = provider;
	}

	/**
	 * Called when the item of this block is to be rendered.
	 * @param model A {@link Model} to use.
	 */
	public void renderItem(Model model) {
		Optional<StaticRenderer> opComponent = provider.getOp(StaticRenderer.class);
		if (opComponent.isPresent()) {
			opComponent.get().renderStatic(model);
		} else {
			provider.getOp(DynamicRenderer.class).ifPresent(c -> c.renderDynamic(model));
		}
	}

}
