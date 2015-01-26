package nova.core.util;

import java.util.List;

public abstract class Property<T> {
	private String name;

	public Property(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract List<T> getAllowedValues();
}
