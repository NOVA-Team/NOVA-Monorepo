package nova.core.gui.render;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Vertex2D extends Vector2D {

	public final boolean uv;
	public final double u, v;

	public Vertex2D(double x, double y) {
		super(x, y);
		uv = false;
		u = v = 0;
	}

	public Vertex2D(double x, double y, double u, double v) {
		super(x, y);
		uv = true;
		this.u = u;
		this.v = v;
	}

	private Vertex2D(double x, double y, double u, double v, boolean uv) {
		super(x, y);
		this.uv = uv;
		this.u = u;
		this.v = v;
	}

	public Vertex2D offset(double x, double y) {
		return new Vertex2D(this.getX() + x, this.getY() + y, u, v, uv);
	}

	@Override
	public String toString() {
		return "[Vertex2D] " + getX() + ", " + getY() + ", " + u + ", " + v + ", " + uv;
	}
}
