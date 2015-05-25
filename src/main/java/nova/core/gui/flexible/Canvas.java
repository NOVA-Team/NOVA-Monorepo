package nova.core.gui.flexible;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * A canvas can hold different UI Components
 * @author Calclavia
 */
public class Canvas implements ComponentProvider {

	//TODO: Use ComponentManager/factory
	public final TransformUI transform = new TransformUI();
	private final Set<Component> components = new HashSet<>();
	/**
	 * The later determines how the Canvas will be rendered. A higher value means a layer more in-front.
	 */
	public int layer = 0;

	@Override
	public Set<Component> components() {
		return components;
	}
}
