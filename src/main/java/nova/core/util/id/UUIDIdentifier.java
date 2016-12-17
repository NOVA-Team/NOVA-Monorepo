package nova.core.util.id;

import java.util.UUID;

/**
 * An UUID Identifier.
 *
 * @author soniex2
 */
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
}
