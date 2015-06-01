package nova.core.event;

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
