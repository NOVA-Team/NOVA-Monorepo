package nova.core.render.model;

import nova.core.render.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A Vertex contains a position and UV data.
 * @author Calclavia, Kubuxu, inspired by ChickenBones
 */
public class Vertex implements Cloneable {
	public Vector3D vec;
	public Vector2D uv;

	/**
	 * A RGB color value from 0 to 1.
	 */
	public Color color;

	/**
	 * Constructor for vertex
	 * @param vertex coordinates in 3D sapce.
	 * @param uv coordinates on the texture.
	 */
	public Vertex(Vector3D vertex, Vector2D uv) {
		this.vec = vertex;
		this.uv = uv;
		this.color = Color.white;
	}

	/**
	 * Creates new instance of vertex using separate doubles.
	 * @param x coordinate in space.
	 * @param y coordinate in space.
	 * @param z coordinate in space.
	 * @param u coordinate on texture.
	 * @param v coordinate on texture.
	 */
	public Vertex(double x, double y, double z, double u, double v) {
		this(new Vector3D(x, y, z), new Vector2D(u, v));
	}


	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vertex5[" + new BigDecimal(vec.getX(), cont) + ", " + new BigDecimal(vec.getY(), cont) + ", " + new BigDecimal(vec.getZ(), cont) + "]" +
			"[" + new BigDecimal(uv.getX(), cont) + ", " + new BigDecimal(uv.getY()) + "]";
	}

	@Override
	protected Vertex clone() {
		Vertex vertex = new Vertex(vec, uv);
		vertex.color = color;
		return vertex;
	}
}