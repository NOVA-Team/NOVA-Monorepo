package nova.core.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import nova.core.component.exception.ComponentException;
import nova.core.util.ClassLoaderUtil;
import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.RegistrationException;
import nova.core.util.Registry;

/**
 * Used to instantiate components.
 *
 * You should ALWAYS call ComponentManager.make() in order to create new
 * components. Do not create new instances of components yourself.
 * 
 * @author Calclavia
 */
public class ComponentManager extends Manager<Component, ComponentManager.ComponentFactory> {

	private Map<Class<? extends Component>, String> classToComponent = new HashMap<>();
	private Map<Class<?>, Class<? extends Component>> passthroughComponents = new HashMap<>();

	private ComponentManager(Registry<ComponentFactory> registry) {
		super(registry);
	}

	// TODO Not sure how this is meant to be. I added this here, in case that it
	// fits better otherwise, feel free to move it around.
	/**
	 * Use this to register components tagged with {@link Passthrough}.
	 * 
	 * @param componentClazz
	 */
	public void registerNativePassthrough(Class<? extends Component> componentClazz) {
		Passthrough[] pts = componentClazz.getAnnotationsByType(Passthrough.class);
		if (pts.length == 0)
			throw new ComponentException("No passthrough defined by component %s.", componentClazz, componentClazz);
		for (Passthrough pt : pts) {
			try {
				Class<?> intfClazz = Class.forName(pt.value());
				if (!intfClazz.isAssignableFrom(componentClazz)) {
					throw new ComponentException("Invalid passthrough \"%s\" on component %s, the specified interface isn't implemented.", componentClazz, intfClazz, componentClazz);
				}
				// TODO This might cause mods to conflict with each other as
				// they are trying to implement the same common interface using
				// different components
				if (passthroughComponents.containsKey(intfClazz)) {
					throw new ComponentException("Duplicate component %s for interface %s.", componentClazz, componentClazz, intfClazz);
				}
				passthroughComponents.put(intfClazz, componentClazz);
			} catch (ClassNotFoundException e) {
				throw new ClassLoaderUtil.ClassLoaderException(e);
			}
		}
	}

	/**
	 * Internal
	 */
	@Deprecated
	public Set<Class<? extends Component>> getPassthroughtComponents(Object nativeObject) {
		Class<?> nativeClazz = nativeObject.getClass();
		Set<Class<? extends Component>> componentClazzes = new HashSet<>();
		for (Class<?> intfClazz : nativeClazz.getInterfaces()) {
			if (passthroughComponents.containsKey(intfClazz)) {
				componentClazzes.add(passthroughComponents.get(intfClazz));
			}
		}
		return componentClazzes;
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
	 * Instantiates a new node based on its interface. This is not as reliable
	 * as make with componentID.
	 * 
	 * @param theInterface The interface associated with the new component
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
	 * 
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
