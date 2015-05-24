package nova.core.render.model;

import nova.core.render.Color;
import nova.core.util.transform.vector.Vector2d;
import nova.core.util.transform.vector.Vector3d;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A Vertex contains a position and UV data.
 *
 * @author Calclavia, Kubuxu, inspired by ChickenBones
 */
public class Vertex implements Cloneable {
	public Vector3d vec;
	public Vector2d uv;

	/**
	 * A RGB color value from 0 to 1.
	 */
	public Color color;

	public Vertex(Vector3d vert, Vector2d uv) {
		this.vec = vert;
		this.uv = uv;
		this.color = Color.white;
	}

	public Vertex(double x, double y, double z, double u, double v) {
		this(new Vector3d(x, y, z), new Vector2d(u, v));
	}

	public Vertex setColor(Color color) {
		this.color = color;
		return this;
	}

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vertex5[" + new BigDecimal(vec.x, cont) + ", " + new BigDecimal(vec.y, cont) + ", " + new BigDecimal(vec.z, cont) + "]" +
			"[" + new BigDecimal(uv.x, cont) + ", " + new BigDecimal(uv.y) + "]";
	}

	@Override
	protected Vertex clone() {
		Vertex vertex = new Vertex(vec, uv);
		vertex.color = color;
		return vertex;
	}
}