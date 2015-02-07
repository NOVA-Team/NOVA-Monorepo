package nova.core.event;

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
		canceled = isCancelable;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	public @interface Cancelable {

	}
}
