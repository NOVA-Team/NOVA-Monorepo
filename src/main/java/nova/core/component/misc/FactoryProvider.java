package nova.core.component.misc;

import nova.core.component.Component;
import nova.core.component.UnsidedComponent;
import nova.core.util.registry.Factory;

/**
 * Provides a reference to the factory that created this object
 * @author Calclavia
 */
@UnsidedComponent
public class FactoryProvider extends Component {
	public final Factory<?, ?> factory;

	public FactoryProvider(Factory<?, ?> factory) {
		this.factory = factory;
	}
}
