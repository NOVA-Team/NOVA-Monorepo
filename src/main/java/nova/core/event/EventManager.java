package nova.core.event;

/**
 * General event manager that handles basic events
 * @author Calclavia
 */
public class EventManager {

	public static final EventManager instance = new EventManager();

	//TODO: Check the eventBus type parameter?
	/**
	 * Called when the server starts
	 */
	public EventBus<Object> serverStarting = new EventBus<>();

	/**
	 * Called when the server stops
	 */
	public EventBus<Object> serverStopping = new EventBus<>();
}
