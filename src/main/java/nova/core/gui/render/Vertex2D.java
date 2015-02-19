package nova.core.gui.render;

public class Vertex2D {

	public final boolean uv;
	public final int x, y;
	public final float u, v;

	public Vertex2D(int x, int y) {
		uv = false;
		u = v = 0;
		this.x = x;
		this.y = y;
	}

	public Vertex2D(int x, int y, float u, float v) {
		uv = true;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
	}
}
