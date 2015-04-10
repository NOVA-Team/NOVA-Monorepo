package nova.core.block.components;

import nova.core.render.model.Model;

/**
 * This interface specifies that a block requires custom static rendering.
 * (That is, called upon item render or network synchronization.)
 */
public interface Modelled {

	/**
	 * Called when this block is to be rendered.
	 * 
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public void renderStatic(Model model);

	/**
	 * Called when the item of this block is to be rendered.
	 *
	 * @param model A {@link nova.core.render.model.Model} to use.
	 */
	public default void renderItem(Model model) {
		renderStatic(model);
	}
	
}
