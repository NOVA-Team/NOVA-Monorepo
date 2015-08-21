package nova.core.event;

import nova.core.event.bus.CancelableEvent;

/**
 * @author Stan Hebben
 */
public class TestEvent extends CancelableEvent {
	private final StringBuilder output;

	private boolean canceled = false;

	public TestEvent() {
		this.output = new StringBuilder();
	}

	public void append(String value) {
		output.append(value);
	}

	@Override
	public String toString() {
		return output.toString();
	}

	@Override
	public void cancel() {
		canceled = true;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}
}
