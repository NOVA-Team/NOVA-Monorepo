package nova.core.ai;

public interface CTaskBuilder extends LTaskBuilder {

	public CTaskBuilder and(Condition condition);

	public CTaskBuilder or(Condition condition);
}
