package nova.core.util.id;

import nova.core.retention.Data;

import java.util.Objects;

/**
 * A String Identifier.
 *
 * @author soniex2
 */
public class StringIdentifier extends AbstractIdentifier<String> implements Identifier {

	/**
	 * Constructs a new StringIdentifier.
	 *
	 * @param id The String.
	 */
	public StringIdentifier(String id) {
		super(id);
	}

	@Override
	public boolean equals(Object other) {
		return equalsImpl(this, other, StringIdentifier.class, Identifier::asString);
	}

	public static class Loader extends IdentifierLoader<StringIdentifier> {

		public Loader(String id) {
			super(id);
		}

		@Override
		public Class<StringIdentifier> getIdentifierClass() {
			return StringIdentifier.class;
		}

		@Override
		public void save(Data data, StringIdentifier identifier) {
			data.put("id", identifier.asString());
		}

		@Override
		public StringIdentifier load(Data data) {
			return new StringIdentifier(data.get("id"));
		}

		@Override
		public StringIdentifier load(String data) {
			return new StringIdentifier(data);
		}
	}
}
