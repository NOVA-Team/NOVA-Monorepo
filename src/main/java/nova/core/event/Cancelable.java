package nova.core.event;

/**
 * Implemented by events which can be canceled. Note that events will be published
 * to all listeners no matter if they are canceled or not, but their canceled flag
 * will be set to true.
 * 
 * @author Stan Hebben
 */
public interface Cancelable
{
	/**
	 * Cancels an event. Sets the canceled flag.
	 */
	public void cancel();
	
	/**
	 * Checks if the canceled flag has been set.
	 * 
	 * @return canceled status
	 */
	public boolean isCanceled();
}
