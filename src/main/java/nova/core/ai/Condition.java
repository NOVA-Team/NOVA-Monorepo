package nova.core.ai;

@FunctionalInterface
public interface Condition extends Value<Boolean> {

	public default Condition and(Condition other) {
		return ai -> get(ai) && other.get(ai);
	}

	public default Condition or(Condition other) {
		return ai -> get(ai) || other.get(ai);
	}

	public default Condition not() {
		return ai -> !get(ai);
	}

	public static Condition of(Condition cd) {
		return cd;
	}
}
