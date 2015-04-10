package nova.core.block.components;

import nova.core.render.model.Model;

/**
 * This interface signals that a block needs dynamic rendering.
 */
public interface DynamicallyModelled {

	/**
	 * Called for a dynamic render.
	 *
	 * @param model A {@link nova.core.render.model.Model} to use
	 */
	public void renderDynamic(Model model);

}
