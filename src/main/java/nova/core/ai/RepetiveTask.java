package nova.core.ai;

public class RepetiveTask implements Task {

	private final Task wrapped;
	private final int times;

	public RepetiveTask(int times, Task wrapped) {
		this.wrapped = wrapped;
		this.times = times;
	}

	@Override
	public void execute(AI<?> ai) {
		for (int i = 0; i < times; i++) {
			wrapped.execute(ai);
		}
	}
}
