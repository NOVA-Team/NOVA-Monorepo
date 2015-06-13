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
public class ComponentManager extends Manager<Component, ComponentManager.ComponentFactory> {

	private Map<Class<? extends Component>, String> classToComponent = new HashMap<>();

	private ComponentManager(Registry<ComponentFactory> registry) {
		super(registry);
	}

	@Override
	public ComponentFactory register(Function<Object[], Component> constructor) {
		return register(new ComponentFactory(constructor));
	}

	@Override
	public ComponentFactory register(ComponentFactory factory) {
		classToComponent.put(factory.getDummy().getClass(), factory.getID());
		return super.register(factory);
	}

	/**
	 * Instantiates a new node based on its interface. This is not as reliable as make withPriority componentID.
	 * @param theInterface The interface associated withPriority the new component
	 * @param args The arguments for the component's constructor
	 * @param <N> The node type
	 * @return A new node of N type.
	 */
	@SuppressWarnings("unchecked")
	public <N> N make(Class<N> theInterface, Object... args) {
		Optional<ComponentFactory> first = registry.stream()
			.filter(n -> theInterface.isAssignableFrom(n.getDummy().getClass()))
			.findFirst();

		if (first.isPresent()) {
			return (N) first.get().make(args);
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
		return registry.get(componentID).get().make(args);
	}

	public static class ComponentFactory extends Factory<Component> {
		public ComponentFactory(Function<Object[], Component> constructor) {
			super(constructor);
		}

		public Component make(Object... args) {
			return constructor.apply(args);
		}
	}
}
