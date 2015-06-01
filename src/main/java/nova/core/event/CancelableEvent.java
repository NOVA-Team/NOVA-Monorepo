package nova.core.event;

import nova.core.util.exception.NovaException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A base class for an event that may or may not be cancelable, depending on
 * weather a sub event has the {@link CancelableEvent.Cancelable} annotation on.
 * @author Vic Nightfall
 */
public abstract class CancelableEvent extends Event implements Cancelable {

	private boolean canceled;
	private boolean isCancelable;

	public CancelableEvent() {
		isCancelable = getClass().isAnnotationPresent(CancelableEvent.Cancelable.class);
	}

	@Override
	public void cancel() {
		if (!isCancelable) {
			throw new NovaException("Attempted to cancel an uncancelable event " + getClass() + " !");
		}
		canceled = true;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Cancelable {

	}
}
