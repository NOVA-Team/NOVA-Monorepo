package nova.core.util;

import java.util.List;

/**
 * Property of some kind
 * @param <T> Type of values
 */
public abstract class Property<T> {
	private String name;

	public Property(String name) {
		this.name = name;
	}

	/**
	 * @return Name of this property
	 */
	public String getName() {
		return name;
	}

	public abstract List<T> getAllowedValues();
}
