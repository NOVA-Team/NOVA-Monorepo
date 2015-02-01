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
public class Shape {
	public final List<Vertex> vertices = new ArrayList<>();
	public Vector3d normal = Vector3d.zero;
	public Vector3d translation = Vector3d.zero;
	public Quaternion rotation = Quaternion.identity;
	public Vector3d scale = Vector3d.zero;
	public double brightness = 1;
	public Optional<Texture> texture = Optional.empty();

	/**
	 * Binds a specific texture to this artist.
	 * @param texture Texture to bind
	 * @return This Artist
	 */
	public Shape bindTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}

	public Shape drawVertex(Vertex vert) {
		vertices.add(vert);
		return this;
	}
}
