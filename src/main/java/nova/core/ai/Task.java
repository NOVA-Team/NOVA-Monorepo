package nova.core.ai;


@FunctionalInterface
public interface Task {

	public void execute(AI<?> ai);
}
