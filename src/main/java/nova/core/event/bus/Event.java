package nova.core.event.bus;

/**
 * All events extend this class.
 * @author Calclavia
 */
public class Event {

	public enum Result {
		DENY,
		DEFAULT,
		ALLOW
	}

}
