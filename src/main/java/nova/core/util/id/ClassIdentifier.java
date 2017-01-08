package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.DataException;

import java.util.Objects;

/**
 * A Class Identifier.
 *
 * @author soniex2
 */
public class ClassIdentifier extends AbstractIdentifier<Class<?>> implements Identifier {

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

	@Override
	public boolean equals(Object other) {
		return equalsImpl(this, other, ClassIdentifier.class, ClassIdentifier::asClass);
	}

	public static class Loader extends IdentifierLoader<ClassIdentifier> {

		public Loader(String id) {
			super(id);
		}

		@Override
		public Class<ClassIdentifier> getIdentifierClass() {
			return ClassIdentifier.class;
		}

		@Override
		public void save(Data data, ClassIdentifier identifier) {
			data.put("id", identifier.id.getName());
		}

		@Override
		public ClassIdentifier load(Data data) {
			try {
				return new ClassIdentifier(Class.forName(data.get("id")));
			} catch (ClassNotFoundException ex) {
				throw new DataException(ex);
			}
		}
	}
}
