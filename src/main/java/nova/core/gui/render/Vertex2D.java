package nova.core.gui.render;

public class Vertex2D {

	public final boolean uv;
	public final double x, y, u, v;

	public Vertex2D(double x, double y) {
		uv = false;
		u = v = 0;
		this.x = x;
		this.y = y;
	}

	public Vertex2D(double x, double y, double u, double v) {
		uv = true;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}

	private Vertex2D(double x, double y, double u, double v, boolean uv) {
		this.uv = uv;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}

	public Vertex2D offset(double x, double y) {
		return new Vertex2D(this.x + x, this.y + y, u, v, uv);
	}

	@Override
	public String toString() {
		return "[Vertex2D] " + x + ", " + y + ", " + u + ", " + v + ", " + uv;
	}
}
