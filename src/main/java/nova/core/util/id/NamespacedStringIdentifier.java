package nova.core.util.id;

import java.util.Objects;

/**
 * A namespace:name identifier
 *
 * @author soniex2
 */
public class NamespacedStringIdentifier extends AbstractIdentifier<NamespacedStringIdentifier.NamespacedString> {

	/**
	 * Constructs a new NamespacedStringIdentifier.
	 *
	 * @param namespace The namespace.
	 * @param name The name.
	 */
	public NamespacedStringIdentifier(String namespace, String name) {
		super(new NamespacedString(namespace, name));
	}

	/**
	 * Constructs a new NamespacedStringIdentifier.
	 *
	 * @param namespacedname The colon-separated namespaced name.
	 */
	public NamespacedStringIdentifier(String namespacedname) {
		super(new NamespacedString(namespacedname));
	}

	/**
	 * Constructs a new NamespacedStringIdentifier.
	 *
	 * @param namespacedstring The NamespacedString.
	 */
	public NamespacedStringIdentifier(NamespacedString namespacedstring) {
		super(namespacedstring);
	}

	/**
	 * Returns the namespaced string.
	 *
	 * @return The namespaced string.
	 */
	public NamespacedString asNamespacedString() {
		return id;
	}

	public static final class NamespacedString {
		private final String namespace;
		private final String name;

		/**
		 * Constructs a new NamespacedString.
		 *
		 * @param namespace The namespace.
		 * @param name The name.
		 */
		public NamespacedString(String namespace, String name) {
			if (namespace.contains(":")) {
				throw new IllegalArgumentException("Namespace must not contain a colon");
			}
			this.namespace = Objects.requireNonNull(namespace).intern();
			this.name = Objects.requireNonNull(name).intern();
		}

		/**
		 * Constructs a new NamespacedString.
		 *
		 * @param namespacedname The colon-separated namespaced name.
		 */
		public NamespacedString(String namespacedname) {
			this(nsOf(namespacedname), nameOf(namespacedname));
		}

		/**
		 * Returns the namespace.
		 *
		 * @return The namespace.
		 */
		public String getNamespace() {
			return namespace;
		}

		/**
		 * Returns the name.
		 *
		 * @return The name.
		 */
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return namespace + ":" + name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			NamespacedString that = (NamespacedString) o;

			if (!namespace.equals(that.namespace))
				return false;
			return name.equals(that.name);
		}

		@Override
		public int hashCode() {
			return 31 * namespace.hashCode() + name.hashCode();
		}

		private static String nameOf(String namespacedname) {
			int index = namespacedname.indexOf(':');
			if (index < 0) {
				return namespacedname;
			} else {
				return namespacedname.substring(index + 1);
			}
		}

		private static String nsOf(String namespacedname) {
			int index = namespacedname.indexOf(':');
			if (index < 0) {
				return "";
			} else {
				return namespacedname.substring(0, index);
			}
		}
	}
}
