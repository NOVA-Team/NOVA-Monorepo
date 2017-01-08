package nova.core.util.id;

/**
 * A Class Identifier.
 *
 * @author soniex2
 */
public final class ClassIdentifier extends AbstractIdentifier<Class<?>> implements Identifier {

	/**
	 * Constructs a new ClassIdentifier.
	 *
	 * @param id The Class.
	 */
	public ClassIdentifier(Class<?> id) {
		super(id);
	}

	@Override
	public String asString() {
		return id.getSimpleName();
	}

	/**
	 * Returns this Identifier's Class.
	 *
	 * @return The Class.
	 */
	public Class<?> asClass() {
		return id;
	}
}
