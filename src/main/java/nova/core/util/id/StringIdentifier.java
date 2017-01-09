package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.DataConverter;
import nova.core.retention.DataConvertible;

/**
 * A String Identifier.
 *
 * @author soniex2
 */
@DataConvertible(StringIdentifier.Converter.class)
public final class StringIdentifier extends AbstractIdentifier<String> implements Identifier {

	/**
	 * Constructs a new StringIdentifier.
	 *
	 * @param id The String.
	 */
	public StringIdentifier(String id) {
		super(id);
	}

	public static final class Converter implements DataConverter {
		@Override
		public Object fromData(Data d) {
			return new StringIdentifier(d.get("value"));
		}

		@Override
		public void toData(Object o, Data data) {
			data.put("value", ((StringIdentifier) o).asString());
		}
	}
}
