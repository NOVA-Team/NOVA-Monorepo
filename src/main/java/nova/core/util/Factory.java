package nova.core.util;

import java.util.function.Function;

/**
 * @param <T> Type of produced object
 * @author Calclavia
 */
public class Factory<T extends Identifiable> implements Identifiable {
	protected final Function<Object[], T> constructor;
	protected final T dummy;

	public Factory(Function<Object[], T> constructor) {
		this.constructor = constructor;
		this.dummy = constructor.apply(new Object[0]);
	}

	public T getDummy() {
		return dummy;
	}

	public String getID() {
		return dummy.getID();
	}
}
