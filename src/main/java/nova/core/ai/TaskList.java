package nova.core.ai;

import java.util.LinkedList;
import java.util.List;

import nova.core.event.EventListener;

public final class TaskList implements EventListener<Event> {

	private List<Task> taskList = new LinkedList<>();

	public static void ret() {
		throw new ReturnException();
	}

	public static void error(String message) {
		throw new TaskException(message);
	}

	public static void error(Throwable cause) {
		throw new TaskException(cause);
	}

	public static void error(String message, Throwable cause) {
		throw new TaskException(message, cause);
	}

	/**
	 * Exception used to break out of a task list. Does not generate a valid
	 * stack trace.
	 * 
	 * @author Vic Nightfall
	 */
	private static class ReturnException extends RuntimeException {

		private static final long serialVersionUID = -1462234987936168173L;

		@Override
		public synchronized Throwable fillInStackTrace() {
			// Prevents stack trace generation
			return this;
		}
	}

	private static class TaskException extends RuntimeException {

		private static final long serialVersionUID = -59859232047647580L;

		public TaskException(String message) {
			super(message);
		}

		public TaskException(Throwable cause) {
			super(cause);
		}

		public TaskException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	@Override
	public void onEvent(Event event) {

	}
}
