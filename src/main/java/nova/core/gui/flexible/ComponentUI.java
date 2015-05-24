package nova.core.gui.flexible;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;

/**
 * @author Calclavia
 */
public abstract class ComponentUI implements Component {
	public final ComponentProvider provider;

	public ComponentUI(ComponentProvider provider) {
		this.provider = provider;
	}

	@Override
	public ComponentProvider provider() {
		return provider;
	}
}
