package nova.core.util;

public class MockIdentifiable implements Identifiable {
	String ID;

	public MockIdentifiable(String ID) {
		this.ID = ID;
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String toString() {
		return getID();
	}
}
