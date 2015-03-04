package nova.core.event;

import nova.core.util.exception.NovaException;

/**
 * A base class for an event that may or may not be cancelable, depending on
 * weather a sub event has the {@link CancelableEvent.Cancelable} annotation on.
 * 
 * @author Vic Nightfall
 */
public abstract class CancelableEvent implements Cancelable {

	private boolean canceled;
	private boolean isCancelable;

	public CancelableEvent() {
		isCancelable = getClass().isAnnotationPresent(CancelableEvent.Cancelable.class);
	}

	@Override
	public void cancel() {
		if (!isCancelable)
			throw new NovaException("Attempted to cancel an uncancelable event " + getClass() + " !");
		canceled = true;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	public @interface Cancelable {

	}
}
