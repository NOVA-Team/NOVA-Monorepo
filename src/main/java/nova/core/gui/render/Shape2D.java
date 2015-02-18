package nova.core.gui.render;

import nova.core.util.exception.NovaException;
import nova.core.util.transform.Rectangle;
import nova.core.util.transform.Vector2i;

/**
 * A Shape2D is a polygon mesh in 2D space. It defines an array of
 * {@link Vertex2D} and can be drawn to a {@link Canvas} with a {@link Graphics}
 * object.
 * 
 * @author Vic Nightfall
 */
public interface Shape2D {
	
	public int size();
	
	public Vertex2D[] vertices();

	public default Vector2i centroid() {
		Vertex2D[] vertices = vertices();
		int n = size();

		double cx = 0, cy = 0;
		int i, j;

		double f = 0;
		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			f = (vertices[i].x * vertices[j].y - vertices[j].x * vertices[i].y);
			cx += (vertices[i].x + vertices[j].x) * f;
			cy += (vertices[i].y + vertices[j].y) * f;
		}
		f = 1.0 / (6.0 * area());
		cx *= f;
		cy *= f;

		return new Vector2i((int) cx, (int) cy);
	}

	public default double area() {
		Vertex2D[] vertices = vertices();
		int n = size();

		double a = 0;
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % n;
			a += vertices[i].x * vertices[j].y;
			a -= vertices[i].y * vertices[j].x;
		}

		a /= 2.0;
		return a;
	}

	public static class PolygonShape implements Shape2D {

		private final Vertex2D[] vertices;

		public PolygonShape(Vertex2D[] vertices) {
			if (vertices.length < 3)
				throw new NovaException("A polygon must contain at least 3 vertices!");
			this.vertices = vertices;
		}
		
		@Override
		public int size() {
			return vertices.length;
		}

		@Override
		public Vertex2D[] vertices() {
			return vertices;
		}
	}

	public static class RectangleShape implements Shape2D {

		private final Vertex2D[] vertices;

		public RectangleShape(Rectangle<?> rect) {
			vertices = new Vertex2D[4];
			vertices[0] = new Vertex2D(rect.x1i(), rect.y1i());
			vertices[1] = new Vertex2D(rect.x2i(), rect.y1i());
			vertices[2] = new Vertex2D(rect.x2i(), rect.y2i());
			vertices[2] = new Vertex2D(rect.x1i(), rect.y2i());
		}

		public RectangleShape(int x, int y, int width, int height) {
			vertices = new Vertex2D[4];
			vertices[0] = new Vertex2D(x, y);
			vertices[1] = new Vertex2D(x + width, y);
			vertices[2] = new Vertex2D(x + width, y + height);
			vertices[2] = new Vertex2D(x, y + height);
		}

		@Override
		public Vector2i centroid() {
			return new Vector2i((vertices[0].x + vertices[1].x) / 2, (vertices[1].y + vertices[2].y) / 2);
		}

		@Override
		public double area() {
			return (vertices[1].x - vertices[0].x) * (vertices[2].y - vertices[1].y);
		}

		@Override
		public int size() {
			return vertices.length;
		}

		@Override
		public Vertex2D[] vertices() {
			return vertices;
		}
	}
}
