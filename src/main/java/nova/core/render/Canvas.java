package nova.core.render;

import nova.core.util.transform.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Calclavia
 */
public class Canvas {
	public final List<Vector3d> vertices = new ArrayList<>();
	public Vector3d translation = Vector3d.ZERO;
	public Vector3d rotation = Vector3d.ZERO;
	public Vector3d scale = Vector3d.ONE;
	private Optional<Texture> texture = Optional.empty();

	public Canvas drawVertex(Vector3d pos) {
		vertices.add(pos);
		return this;
	}

	/**
	 * Binds a specific texture to this artist.
	 * @param texture Texture to bind
	 * @return This Canvas
	 */
	public Canvas bindTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}

	/**
	 * Draws a quadrilateral with four corners.
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return This Canvas
	 */
	public Canvas drawQuad(Vector3d a, Vector3d b, Vector3d c, Vector3d d) {
		drawVertex(a);
		drawVertex(b);
		drawVertex(c);
		drawVertex(d);
		return this;
	}

	/**
	 * Draws a 1x1x1 cube
	 * @return This Canvas
	 */
	public Canvas drawCube() {
		drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(-0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(-0.5, 0.5, -0.5));
		drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, 0.5), new Vector3d(-0.5, -0.5, 0.5));
		drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, -0.5));
		drawQuad(new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(0.5, -0.5, 0.5));
		drawQuad(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(-0.5, 0.5, 0.5));
		drawQuad(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(0.5, -0.5, 0.5));
		return this;
	}
}
