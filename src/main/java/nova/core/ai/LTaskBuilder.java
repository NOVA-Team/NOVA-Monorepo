package nova.core.ai;

import nova.core.event.Cancelable;

public interface LTaskBuilder {

	public LTaskBuilder then(Task task);

	public TaskList compile();

	public default Cancelable start(AI<?> ai) {
		return ai.start(compile());
	}

	public default LTaskBuilder wait(float seconds) {
		then(new WaitTask(seconds));
		return this;
	}

	public default LTaskBuilder repeat(int n, Task task) {
		then(new RepetiveTask(n, task));
		return this;
	}
}
