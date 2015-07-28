package nova.core.util;

import java.util.function.Function;

/**
 * Factories are immutable object builders that create objects.
 * @param <T> Type of produced object
 * @author Calclavia
 */
//TODO: Remove args
public class Factory<T extends Identifiable> implements Identifiable {
	protected final Function<Object[], T> constructor;
	protected T dummy;

	public Factory(Function<Object[], T> constructor) {
		this.constructor = constructor;
	}

	public T build(Object... args) {
		return constructor.apply(args);
	}

	public T getDummy() {
		if (dummy == null) {
			//TODO: This will cause problems!
			dummy = build();
		}
		return dummy;
	}

	public String getID() {
		return getDummy().getID();
	}
}
