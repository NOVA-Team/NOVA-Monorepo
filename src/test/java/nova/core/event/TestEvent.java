package nova.core.event;

/**
 * @author Stan Hebben
 */
public class TestEvent implements Cancelable {
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
