package nova.core.gui.flexible;

import nova.core.component.ComponentProvider;

/**
 * A canvas can hold different UI Components
 * @author Calclavia
 */
public class Canvas extends ComponentProvider {

	//TODO: Use ComponentManager/factory
	public final TransformUI transform = new TransformUI();
	/**
	 * The later determines how the Canvas will be rendered. A higher value means a layer more in-front.
	 */
	public int layer = 0;
}
