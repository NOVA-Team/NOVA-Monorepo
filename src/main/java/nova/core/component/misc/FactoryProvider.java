package nova.core.component.misc;

import nova.core.component.Component;
import nova.core.util.Factory;

/**
 * Provides a reference to the factory that created this object
 * @author Calclavia
 */
public class FactoryProvider extends Component {
	public final Factory<?, ?> factory;

	public FactoryProvider(Factory<?, ?> factory) {
		this.factory = factory;
	}
}
