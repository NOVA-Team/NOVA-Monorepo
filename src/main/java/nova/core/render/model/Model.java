package nova.core.render.model;

import nova.core.block.Block;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector2d;
import nova.core.util.transform.Vector3d;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model is capable of containing multiple faces.
 * @author Calclavia
 */
public class Model implements Cloneable {

	//The name of the model
	public final String name;
	/**
	 * A list of all the shapes drawn.
	 */
	public final Set<Face> faces = new HashSet<>();
	public final Set<Model> children = new HashSet<>();

	//The translation of the face.
	public Vector3d translation = Vector3d.zero;
	//The rotation offset
	public Vector3d offset = Vector3d.zero;
	//The Quaternion rotation of the model.
	public Quaternion rotation = Quaternion.identity;
	//The scale of the face.
	public Vector3d scale = Vector3d.one;
	//The offset of the texture.
	public Vector2d textureOffset = Vector2d.zero;

	public Model(String name) {
		this.name = Objects.requireNonNull(name, "Model name cannot be null!");
	}

	public Model() {
		this("");
	}

	/**
	 * Binds all the faces and all child models with this texture.
	 */
	public void bind(Texture texture) {
		faces.forEach(f -> f.texture = Optional.of(texture));
		children.forEach(m -> m.bind(texture));
	}

	/**
	 * Starts drawing, by returning an Face for the Model to work on.
	 * Add vertices to this Face and finish it by calling drawShape()
	 * @return new {@link Face}
	 */
	public Face createShape() {
		return new Face();
	}

	/**
	 * Finish drawing the Face by adding it into the list of Faces.
	 * @param Face - The finished masterpiece.
	 */
	public void drawShape(Face Face) {
		faces.add(Face);
	}

	/**
	 * Draws a standard block.
	 * @param block Block to draw
	 * @return This Model
	 */
	public Model renderBlock(Block block) {
		//TODO: Change render size based on block bounds and check sides
		/**
		 * Draw down
		 */
		Face down = createShape();
		down.normal = Direction.DOWN.toVector().toDouble();
		down.texture = block.getTexture(Direction.UP);
		//Top-left corner
		down.drawVertex(new Vertex(0.5, -0.5, 0.5, 0, 0));
		//Top-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 0));
		//Bottom-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		down.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1));
		drawShape(down);

		/**
		 * Draw up
		 */
		Face up = createShape();
		up.normal = Direction.UP.toVector().toDouble();
		up.texture = block.getTexture(Direction.UP);
		//Bottom-left corner
		up.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 1));
		//Bottom-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, -0.5, 1, 1));
		//Top-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		up.drawVertex(new Vertex(0.5, 0.5, 0.5, 0, 0));
		drawShape(up);

		/**
		 * Draw north
		 */
		Face north = createShape();
		north.normal = Direction.NORTH.toVector().toDouble();
		north.texture = block.getTexture(Direction.NORTH);
		//Top-left corner
		north.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		north.drawVertex(new Vertex(0.5, 0.5, -0.5, 1, 0));
		//Bottom-right corner
		north.drawVertex(new Vertex(0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		north.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1));
		drawShape(north);

		/**
		 * Draw south
		 */
		Face south = createShape();
		south.normal = Direction.SOUTH.toVector().toDouble();
		south.texture = block.getTexture(Direction.SOUTH);
		//Bottom-left corner
		south.drawVertex(new Vertex(-0.5, -0.5, 0.5, 0, 1));
		//Bottom-right corner
		south.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		south.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		south.drawVertex(new Vertex(-0.5, 0.5, 0.5, 0, 0));
		drawShape(south);

		/**
		 * Draw west
		 */
		Face west = createShape();
		west.normal = Direction.WEST.toVector().toDouble();
		west.texture = block.getTexture(Direction.WEST);
		//Bottom-left corner
		west.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1));
		//Bottom-right corner
		west.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		west.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		west.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0));
		drawShape(west);

		/**
		 * Draw east
		 */
		Face east = createShape();
		east.normal = Direction.EAST.toVector().toDouble();
		east.texture = block.getTexture(Direction.EAST);
		//Top-left corner
		east.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		east.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0));
		//Bottom-right corner
		east.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1));
		//Bottom-left corner
		east.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1));
		drawShape(east);

		return this;
	}

	public Model drawCube() {
		/**
		 * Draw down
		 */
		Face down = createShape();
		down.normal = Direction.DOWN.toVector().toDouble();
		//Top-left corner
		down.drawVertex(new Vertex(0.5, -0.5, 0.5, 0, 0));
		//Top-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 0));
		//Bottom-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		down.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1));
		drawShape(down);

		/**
		 * Draw up
		 */
		Face up = createShape();
		up.normal = Direction.UP.toVector().toDouble();
		//Bottom-left corner
		up.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 1));
		//Bottom-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, -0.5, 1, 1));
		//Top-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		up.drawVertex(new Vertex(0.5, 0.5, 0.5, 0, 0));
		drawShape(up);

		/**
		 * Draw north
		 */
		Face north = createShape();
		north.normal = Direction.NORTH.toVector().toDouble();
		//Top-left corner
		north.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		north.drawVertex(new Vertex(0.5, 0.5, -0.5, 1, 0));
		//Bottom-right corner
		north.drawVertex(new Vertex(0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		north.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1));
		drawShape(north);

		/**
		 * Draw south
		 */
		Face south = createShape();
		south.normal = Direction.SOUTH.toVector().toDouble();
		//Bottom-left corner
		south.drawVertex(new Vertex(-0.5, -0.5, 0.5, 0, 1));
		//Bottom-right corner
		south.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		south.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		south.drawVertex(new Vertex(-0.5, 0.5, 0.5, 0, 0));
		drawShape(south);

		/**
		 * Draw west
		 */
		Face west = createShape();
		west.normal = Direction.WEST.toVector().toDouble();
		//Bottom-left corner
		west.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1));
		//Bottom-right corner
		west.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		west.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		west.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0));
		drawShape(west);

		/**
		 * Draw east
		 */
		Face east = createShape();
		east.normal = Direction.EAST.toVector().toDouble();
		//Top-left corner
		east.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		east.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0));
		//Bottom-right corner
		east.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1));
		//Bottom-left corner
		east.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1));
		drawShape(east);

		return this;
	}

	public Set<Model> flatten() {
		return flatten(Vector3d.zero, Vector3d.zero, Quaternion.identity, Vector3d.one);
	}

	/**
	 * Flattens the model into a set of models with no additional transformations,
	 * applying all the transformations into the individual vertices.
	 */
	public Set<Model> flatten(Vector3d translation, Vector3d offset, Quaternion rotation, Vector3d scale) {
		Set<Model> models = new HashSet<>();

		Vector3d finalTranslation = this.translation.add(translation);
		Vector3d finalOffset = this.offset.add(offset);
		Quaternion finalRotation = this.rotation.multiply(rotation);
		Vector3d finalScale = this.scale.multiply(scale);

		//Create a new model with transformation applied.
		Model transformedModel = new Model();
		transformedModel.faces.addAll(faces);
		transformedModel.faces.forEach(f ->
				f.vertices.forEach(v -> {
					//Order: offset -> transform -> -offset -> scale -> translate
					v.vec = v.vec
						.add(finalOffset)
						.transform(finalRotation)
						.add(finalOffset.inverse())
						.multiply(finalScale)
						.add(finalTranslation);

				})
		);

		models.add(transformedModel);
		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(finalTranslation, finalOffset, finalRotation, finalScale).stream()).collect(Collectors.toSet()));
		return models;
	}

}
