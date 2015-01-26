package nova.core.core;

public class Position {
	private final long x, y, z;

	public Position(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public long x() { return x; }
	public long y() { return y; }
	public long z() { return z; }
}
