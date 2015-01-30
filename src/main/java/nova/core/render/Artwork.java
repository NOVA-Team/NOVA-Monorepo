package nova.core.render;

import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A artwork is classified as a shape. It is group of vertices that result in a surface.
 * @author Calclavia
 */
public class Artwork {
	public final List<Vertex5> vertices = new ArrayList<>();
	public Vector3d normal = new Vector3d();
	public Vector3d translation = new Vector3d();
	public Quaternion rotation = new Quaternion();
	public Vector3d scale = new Vector3d();
	private Optional<Texture> texture = Optional.empty();

	/**
	 * Binds a specific texture to this artist.
	 * @param texture Texture to bind
	 * @return This Artist
	 */
	public Artwork bindTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}

	public Artwork drawVertex(Vertex5 vert) {
		vertices.add(vert);
		return this;
	}

	/**
	 * Draws a quadrilateral with four corners.
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return This Artist
	 */
	public Artwork drawQuad(Vertex5 a, Vertex5 b, Vertex5 c, Vertex5 d) {
		drawVertex(a);
		drawVertex(b);
		drawVertex(c);
		drawVertex(d);
		return this;
	}

}
