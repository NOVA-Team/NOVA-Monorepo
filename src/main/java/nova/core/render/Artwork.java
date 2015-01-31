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
	public Vector3d normal = Vector3d.ZERO;
	public Vector3d translation = Vector3d.ZERO;
	public Quaternion rotation = new Quaternion();
	public Vector3d scale = Vector3d.ZERO;
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
}
