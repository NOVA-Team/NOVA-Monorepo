package nova.core.event;

public class EventCancelException extends EventException {
	public EventCancelException(String message, Class<? extends Event> event) {
		super(message, event);
	}
}
