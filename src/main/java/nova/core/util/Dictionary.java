package nova.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A dictionary where each identifying string represents a set of objects
 * and each object can have a set of identifiers.
 *
 * @param &lt;T&gt; The object type
 */
public class Dictionary<T> {
	private final Map<String, Set<T>> entries = new HashMap<>();
	private final Map<T, Set<String>> locations = new HashMap<>();

	public Dictionary() {

	}

	/**
	 * Add an object to the dictionary.
	 *
	 * @param name the name of the object.
	 * @param object the object to register.
	 */
	public void add(String name, T object) {
		// TODO: Enforce name to be in camelCase
		if (!entries.containsKey(name)) {
			entries.put(name, new HashSet<>());
		}

		entries.get(name).add(object);

		if (!locations.containsKey(object)) {
			locations.put(object, new HashSet<>());
		}

		locations.get(object).add(name);
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
}
