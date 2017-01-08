package nova.core.util.id;

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
}
