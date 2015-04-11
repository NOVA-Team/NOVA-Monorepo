package nova.core.ai;

public class Event implements Condition {

	@Override
	public Boolean get(AI<?> ai) {
		return ai.isEventQueued(this);
	}

}
