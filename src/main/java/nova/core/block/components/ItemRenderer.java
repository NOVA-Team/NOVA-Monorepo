package nova.core.block.components;

import nova.core.render.model.Model;

/**
 * This interface specifies that a block requires custom static item rendering.
 * (That is, called upon item render or network synchronization.)
 */
public interface ItemRenderer {
	/**
	 * Called when the item of this block is to be rendered.
	 * @param model A {@link Model} to use.
	 */
	default void renderItem(Model model) {
		if (this instanceof StaticRenderer) {
			((StaticRenderer) this).renderStatic(model);
		} else if (this instanceof DynamicRenderer) {
			((DynamicRenderer) this).renderDynamic(model);
		}
	}

}
