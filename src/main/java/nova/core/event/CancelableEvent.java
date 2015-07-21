package nova.core.event;

/**
 * A base class for an cancelable event.
 * @author Vic Nightfall, anti344
 */
public abstract class CancelableEvent extends Event {

	private boolean canceled;

	public void cancel() {
		canceled = true;
	}

	public boolean isCanceled() {
		return canceled;
	}
}
