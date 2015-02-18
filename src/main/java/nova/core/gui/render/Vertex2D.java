package nova.core.gui.render;

public class Vertex2D {

	public final boolean uv;
	public final int u, v, x, y;

	public Vertex2D(int x, int y) {
		uv = false;
		u = v = 0;
		this.x = x;
		this.y = y;
	}

	public Vertex2D(int x, int y, int u, int v) {
		uv = true;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
}
