package nova.core.ai;

class TaskBuilder implements CTaskBuilder, LTaskBuilder {

	private boolean listPhase = false;

	TaskBuilder(Event event) {

	}

	@Override
	public LTaskBuilder then(Task task) {
		// TODO Auto-generated method stub
		listPhase = true;
		return this;
	}

	@Override
	public CTaskBuilder and(Condition condition) {
		// TODO Auto-generated method stub
		if (listPhase)
			throw new IllegalStateException();
		return this;
	}

	@Override
	public CTaskBuilder or(Condition condition) {
		// TODO Auto-generated method stub
		if (listPhase)
			throw new IllegalStateException();
		return this;
	}

	@Override
	public TaskList compile() {
		// TODO Auto-generated method stub
		return null;
	}
}
