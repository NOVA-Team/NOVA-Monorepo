package nova.core.render.model;

import nova.core.render.texture.Texture;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A face is defined as at least 3 vertices joined together. It is group of vertices that result in a surface.
 * @author Calclavia
 */
public class Face {
	//The vertices that make up this face.
	public final List<Vertex> vertices = new ArrayList<>();
	//The normal (or direction) this face is facing. Normals must be unit vectors.
	public Vector3d normal = Vector3d.zero;
	//The translation of the face.
	public Vector3d translation = Vector3d.zero;
	//The Quaternion rotation of the face.
	public Quaternion rotation = Quaternion.identity;
	//The scale of the face.
	public Vector3d scale = Vector3d.one;
	//The texture that is to be rendered on this face.
	public Optional<Texture> texture = Optional.empty();
	//The brightness value defines how bright the face should be rendered. The default value will let NOVA decide the brightness based on the world surroundings.
	protected double brightness = -1;

	/**
	 * Binds a specific texture to this artist.
	 * @param texture Texture to bind
	 * @return This Artist
	 */
	public Face bindTexture(Texture texture) {
		this.texture = Optional.of(texture);
		return this;
	}

	public Face drawVertex(Vertex vert) {
		vertices.add(vert);
		return this;
	}

	public double getBrightness() {
		return brightness;
	}

	/**
	 * Gets the center of this face.
	 */
	public Vector3d getCenter() {
		if (vertices.size() >= 3) {
			return vertices
				.stream()
				.map(v -> v.vec)
				.reduce(Vector3d.zero, (a, b) -> a.add(b))
				.divide(vertices.size());
		}

		return Vector3d.zero;
	}
}
