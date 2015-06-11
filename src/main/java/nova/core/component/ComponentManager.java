package nova.core.component;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.RegistrationException;
import nova.core.util.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Used to instantiate components.
 *
 * You should ALWAYS call ComponentManager.make() in order to create new components.
 * Do not create new instances of components yourself.
 * @author Calclavia
 */
public class ComponentManager extends Manager<Component> {

	private Map<Class<? extends Component>, String> classToComponent = new HashMap<>();

	private ComponentManager(Registry<Factory<Component>> registry) {
		super(registry);
	}

	@Override
	public Factory<Component> register(Factory<Component> factory) {
		classToComponent.put(factory.clazz, factory.getID());
		return super.register(factory);
	}

	/**
	 * Instantiates a new node based on its interface. This is not as reliable as make with componentID.
	 * @param theInterface The interface associated with the new component
	 * @param args The arguments for the component's constructor
	 * @param <N> The node type
	 * @return A new node of N type.
	 */
	@SuppressWarnings("unchecked")
	public <N> N make(Class<N> theInterface, Object... args) {
		Optional<Factory<Component>> first = registry.stream()
			.filter(n -> theInterface.isAssignableFrom(n.clazz))
			.findFirst();

		if (first.isPresent()) {
			return (N) first.get().resolve();
		} else {
			throw new RegistrationException("Attempt to create node that is not registered: " + theInterface);
		}
	}

	/**
	 * Instantiates a new node based on its componentID.
	 * @param componentID - The ID of the node
	 * @param args - The arguments for the constructor
	 * @return A new node.
	 */
	public Component make(String componentID, Object... args) {
		return registry.get(componentID).get().resolve();
	}
	
}
