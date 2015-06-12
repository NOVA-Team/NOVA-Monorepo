package nova.core.gui.render;

import nova.core.render.RenderException;
import nova.core.util.shape.Rectangle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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

	public default Vector2D centroid() {
		Vertex2D[] vertices = vertices();
		int n = size();

		double cx = 0, cy = 0;
		int i, j;

		double f = 0;
		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			f = (vertices[i].getX() * vertices[j].getY() - vertices[j].getX() * vertices[i].getY());
			cx += (vertices[i].getX() + vertices[j].getX()) * f;
			cy += (vertices[i].getY() + vertices[j].getY()) * f;
		}
		f = 1.0 / (6.0 * area());
		cx *= f;
		cy *= f;

		return new Vector2D(cx, cy);
	}

	public default double area() {
		Vertex2D[] vertices = vertices();
		int n = size();

		double a = 0;
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % n;
			a += vertices[i].getX() * vertices[j].getY();
			a -= vertices[i].getY() * vertices[j].getX();
		}

		a /= 2.0;
		return a;
	}

	public static class PolygonShape implements Shape2D {

		private final Vertex2D[] vertices;

		public PolygonShape(Vertex2D... vertices) {
			if (vertices.length < 3)
				throw new RenderException("A polygon must contain at least 3 vertices!");
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

		public RectangleShape(Rectangle rect) {
			vertices = new Vertex2D[4];
			vertices[0] = new Vertex2D(rect.minXi(), rect.minYi());
			vertices[1] = new Vertex2D(rect.maxXi(), rect.minYi());
			vertices[2] = new Vertex2D(rect.maxXi(), rect.maxYi());
			vertices[2] = new Vertex2D(rect.minXi(), rect.maxYi());
		}

		public RectangleShape(int x, int y, int width, int height) {
			vertices = new Vertex2D[4];
			vertices[0] = new Vertex2D(x, y);
			vertices[1] = new Vertex2D(x + width, y);
			vertices[2] = new Vertex2D(x + width, y + height);
			vertices[2] = new Vertex2D(x, y + height);
		}

		@Override
		public Vector2D centroid() {
			return new Vector2D((vertices[0].getX() + vertices[1].getY()) / 2, (vertices[1].getY() + vertices[2].getY()) / 2);
		}

		@Override
		public double area() {
			return (vertices[1].getX() - vertices[0].getX()) * (vertices[2].getY() - vertices[1].getY());
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
