package nova.core.render.model;

import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A face is defined as at least 3 vertices joined together. It is group of vertices that result in a surface.
 *
 * @author Calclavia
 */
public class Face implements Cloneable {
	//The vertices that make up this face.
	public final List<Vertex> vertices = new ArrayList<>();
	//The normal (or direction) this face is facing. Normals must be unit vectors.
	public Vector3D normal = Vector3D.ZERO;
	//The texture that is to be rendered on this face.
	public Optional<Texture> texture = Optional.empty();
	//The brightness value defines how bright the face should be rendered. The default value will let NOVA decide the brightness based on the world surroundings.
	public double brightness = -1;

	/**
	 * Binds a specific texture to this artist.
	 *
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
	 *
	 * @return Center
	 */
	public Vector3D getCenter() {
		if (vertices.size() >= 3) {
			return vertices
				.stream()
				.map(v -> v.vec)
				.reduce(Vector3D.ZERO, (a, b) -> a.add(b))
				.scalarMultiply(1f / vertices.size());
		}

		return Vector3D.ZERO;
	}

	@Override
	protected Face clone() {
		Face face = new Face();
		face.vertices.addAll(vertices.stream().map(Vertex::clone).collect(Collectors.toList()));
		face.normal = normal;
		face.texture = texture;
		face.brightness = brightness;
		return face;
	}
}
