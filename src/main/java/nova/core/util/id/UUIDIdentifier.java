package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.DataConverter;
import nova.core.retention.DataConvertible;

import java.util.UUID;

/**
 * An UUID Identifier.
 *
 * @author soniex2
 */
@DataConvertible(UUIDIdentifier.Converter.class)
public final class UUIDIdentifier extends AbstractIdentifier<UUID> implements Identifier {

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

	public static final class Converter implements DataConverter {
		@Override
		public Object fromData(Data d) {
			return new UUIDIdentifier(UUID.fromString(d.get("value")));
		}

		@Override
		public void toData(Object o, Data data) {
			data.put("value", ((UUIDIdentifier) o).asString());
		}
	}
}
