package nova.core.util;

import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * A registry of one type of identifiable object.
 * @param &lt;T&gt; The object type
 */
public class Registry<T extends Identifiable> implements Iterable<T> {
	private final HashBiMap<String, T> objects = HashBiMap.create();

	public Registry() {

	}

	/**
	 * Registers an identifiable object in the registry.
	 * @param object the object to register.
	 */
	public void register(T object) {
		objects.put(object.getID(), object);
	}

	/**
	 * Tests whether a given ID is in the Registry.
	 * @param ID the id to find.
	 * @return true if the registry contains the object with the given ID.
	 */
	public boolean contains(String ID) {
		return objects.containsKey(ID);
	}

	/**
	 * Gets the object with the given id from the registry.
	 * @param id the id to find.
	 * @return the object found or empty Optional if not found.
	 */
	public Optional<T> get(String ID) {
		return Optional.ofNullable(objects.get(ID));
	}

	/**
	 * Gets the name of a given object if contained in registry.
	 * @param object the object to find.
	 * @return the name of the object or empty Optional if not found.
	 */
	public Optional<String> getName(T object) {
		return Optional.ofNullable(objects.inverse().get(object));
	}

	/**
	 * @return an iterator on values of the registry.
	 */
	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}

	public Spliterator<T> spliterator() {
		return objects.values().spliterator();
	}

	public Stream<T> stream() {
		return objects.values().stream();
	}
}
