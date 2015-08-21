package nova.core.event;

import nova.core.event.bus.Event;

/**
 * Server-side events
 * @author Calclavia
 */
public abstract class ServerEvent extends Event {
	/**
	 * Called when the server starts running
	 */
	public static class Start extends ServerEvent {

	}

	/**
	 * Called when the server stops running.
	 */
	public static class Stop extends ServerEvent {

	}
}
