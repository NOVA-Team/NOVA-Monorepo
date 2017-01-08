package nova.core.util.id;

import nova.core.retention.Data;

import java.util.Objects;
import java.util.UUID;

/**
 * An UUID Identifier.
 *
 * @author soniex2
 */
public class UUIDIdentifier extends AbstractIdentifier<UUID> implements Identifier {

	/**
	 * Constructs a new UUIDIdentifier.
	 *
	 * @param id The UUID.
	 */
	public UUIDIdentifier(UUID id) {
		super(id);
	}

	/**
	 * Returns this Identifier's UUID.
	 *
	 * @return The UUID.
	 */
	public UUID asUUID() {
		return id;
	}

	@Override
	public boolean equals(Object other) {
		return equalsImpl(this, other, UUIDIdentifier.class, UUIDIdentifier::asUUID);
	}

	public static class Loader extends IdentifierLoader<UUIDIdentifier> {

		public Loader(String id) {
			super(id);
		}

		@Override
		public Class<UUIDIdentifier> getIdentifierClass() {
			return UUIDIdentifier.class;
		}

		@Override
		public void save(Data data, UUIDIdentifier identifier) {
			data.put("id", identifier.asString());
		}

		@Override
		public UUIDIdentifier load(Data data) {
			return new UUIDIdentifier(UUID.fromString(data.get("id")));
		}

		@Override
		public UUIDIdentifier load(String data) {
			return new UUIDIdentifier(UUID.fromString(data));
		}
	}
}
