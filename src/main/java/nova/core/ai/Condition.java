package nova.core.ai;

@FunctionalInterface
public interface Condition {

	public boolean evaluate(AI<?> ai);

	public default Condition and(Condition other) {
		return ai -> evaluate(ai) && other.evaluate(ai);
	}

	public default Condition or(Condition other) {
		return ai -> evaluate(ai) || other.evaluate(ai);
	}

	public default Condition not() {
		return ai -> !evaluate(ai);
	}
}
