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

	public Vertex(Vector3D vert, Vector2D uv) {
		this.vec = vert;
		this.uv = uv;
		this.color = Color.white;
	}

	public Vertex(double x, double y, double z, double u, double v) {
		this(new Vector3D(x, y, z), new Vector2D(u, v));
	}

	public Vertex setColor(Color color) {
		this.color = color;
		return this;
	}

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