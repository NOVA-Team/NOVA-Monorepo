package nova.core.util;

import nova.core.event.EventListener;
import nova.core.event.EventListenerHandle;
import nova.core.event.EventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A dictionary where each identifying string represents a set of objects
 * and each object can have a set of identifiers.
 *
 * @param <T> The object type
 */
public class Dictionary<T> {
	private final Map<String, Set<T>> entries = new HashMap<>();
	private final Map<T, Set<String>> locations = new HashMap<>();
	private final EventBus<AddEvent<T>> addEventListeners = new EventBus<>();
	private final EventBus<RemoveEvent<T>> removeEventListeners = new EventBus<>();

	public Dictionary() {

	}

	/**
	 * Add an object to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param object the object to register.
	 */
	public void add(String key, T object) {
		// TODO: Enforce name to be in camelCase
		if (!entries.containsKey(key)) {
			entries.put(key, new HashSet<>());
		}

		entries.get(key).add(object);

		if (!locations.containsKey(object)) {
			locations.put(object, new HashSet<>());
		}

		locations.get(object).add(key);

		addEventListeners.publish(new AddEvent<>(key, object));
	}

	/**
	 * .
	 * Removes an object from the dictionary
	 *
	 * @param key the name of the object
	 * @param object the object to remove
	 */
	public void remove(String key, T object) {
		if (!entries.containsKey(key))
			return;

		entries.get(key).remove(object);
		locations.get(object).remove(key);

		removeEventListeners.publish(new RemoveEvent<>(key, object));
	}

	/**
	 * Get an object set from the dictionary.
	 *
	 * @param name the dictionary name.
	 * @return the list of objects.
	 */
	public Set<T> get(String name) {
		if (!entries.containsKey(name)) {
			entries.put(name, new HashSet<>());
		}

		return Collections.unmodifiableSet(entries.get(name));
	}

	/**
	 * Find the names of a given object.
	 *
	 * @param object the object to find.
	 * @return the list of names this object is identified by
	 */
	public Set<String> find(T object) {
		if (!locations.containsKey(object)) {
			locations.put(object, new HashSet<>());
		}

		return Collections.unmodifiableSet(locations.get(object));
	}

	/**
	 * @return a {@link java.util.Set Set} of the names in this dictionary.
	 */
	public Set<String> keys() {
		return entries.keySet();
	}

	public EventListenerHandle<AddEvent<T>> whenEntryAdded(EventListener<AddEvent<T>> listener) {
		return addEventListeners.add(listener);
	}

	public EventListenerHandle<RemoveEvent<T>> whenEntryRemoved(EventListener<RemoveEvent<T>> listener) {
		return removeEventListeners.add(listener);
	}

	public static class AddEvent<T> {
		public final String key;
		public final T value;

		public AddEvent(String key, T value) {
			this.key = key;
			this.value = value;
		}
	}

	public static class RemoveEvent<T> {
		public final String key;
		public final T value;

		public RemoveEvent(String key, T value) {
			this.key = key;
			this.value = value;
		}
	}
}
