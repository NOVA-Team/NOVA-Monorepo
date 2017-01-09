package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.DataConverter;
import nova.core.retention.DataConvertible;
import nova.core.retention.DataException;

/**
 * A Class Identifier.
 *
 * @author soniex2
 */
@DataConvertible(ClassIdentifier.Converter.class)
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

	public static final class Converter implements DataConverter {
		@Override
		public Object fromData(Data d) {
			try {
				Class c = Class.forName(d.get("value"));
				return new ClassIdentifier(c);
			} catch (ClassNotFoundException e) {
				throw new DataException(e);
			}
		}

		@Override
		public void toData(Object o, Data data) {
			data.put("value", ((ClassIdentifier) o).asClass().getName());
		}
	}
}
