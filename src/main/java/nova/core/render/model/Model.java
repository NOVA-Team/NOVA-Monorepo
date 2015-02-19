package nova.core.render.model;

import nova.core.block.Block;
import nova.core.render.Color;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.transform.Matrix4x4;
import nova.core.util.transform.MatrixStack;
import nova.core.util.transform.Vector2d;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A model is capable of containing multiple faces.
 *
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

	public Matrix4x4 matrix = Matrix4x4.IDENTITY;

	public Vector2d textureOffset = Vector2d.zero;

	public Model(String name) {
		this.name = Objects.requireNonNull(name, "Model name cannot be null!");
	}

	public Model() {
		this("");
	}

	/**
	 * Binds all the faces and all child models with this texture.
	 *
	 * @param texture The texture
	 */
	public void bind(Texture texture) {
		faces.forEach(f -> f.texture = Optional.of(texture));
		children.forEach(m -> m.bind(texture));
	}

	/**
	 * Starts drawing, by returning an Face for the Model to work on.
	 * Add vertices to this Face and finish it by calling drawShape()
	 *
	 * @return new {@link Face}
	 */
	public Face createShape() {
		return new Face();
	}

	/**
	 * Finish drawing the Face by adding it into the list of Faces.
	 *
	 * @param Face - The finished masterpiece.
	 */
	public void drawShape(Face Face) {
		faces.add(Face);
	}

	/**
	 * Draws a standard block.
	 *
	 * @param block Block to draw
	 * @return This Model
	 */
	public Model renderBlock(Block block) {
		//TODO: Change render size based on block bounds and check sides
		/**
		 * Draw down
		 */
		Face down = createShape();
		Color downColor = block.colorMultiplier(Direction.DOWN);
		down.normal = Direction.DOWN.toVector().toDouble();
		down.texture = block.getTexture(Direction.DOWN);
		//Top-left corner
		down.drawVertex(new Vertex(0.5, -0.5, 0.5, 0, 0).setColor(downColor));
		//Top-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 0).setColor(downColor));
		//Bottom-right corner
		down.drawVertex(new Vertex(-0.5, -0.5, -0.5, 1, 1).setColor(downColor));
		//Bottom-left corner
		down.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1).setColor(downColor));
		drawShape(down);

		/**
		 * Draw up
		 */
		Face up = createShape();
		Color upColor = block.colorMultiplier(Direction.UP);
		up.normal = Direction.UP.toVector().toDouble();
		up.texture = block.getTexture(Direction.UP);
		//Bottom-left corner
		up.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 1).setColor(upColor));
		//Bottom-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, -0.5, 1, 1).setColor(upColor));
		//Top-right corner
		up.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0).setColor(upColor));
		//Top-left corner
		up.drawVertex(new Vertex(0.5, 0.5, 0.5, 0, 0).setColor(upColor));
		drawShape(up);

		/**
		 * Draw north
		 */
		Face north = createShape();
		Color northColor = block.colorMultiplier(Direction.NORTH);
		north.normal = Direction.NORTH.toVector().toDouble();
		north.texture = block.getTexture(Direction.NORTH);
		//Top-left corner
		north.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0).setColor(northColor));
		//Top-right corner
		north.drawVertex(new Vertex(0.5, 0.5, -0.5, 1, 0).setColor(northColor));
		//Bottom-right corner
		north.drawVertex(new Vertex(0.5, -0.5, -0.5, 1, 1).setColor(northColor));
		//Bottom-left corner
		north.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1).setColor(northColor));
		drawShape(north);

		/**
		 * Draw south
		 */
		Face south = createShape();
		Color southColor = block.colorMultiplier(Direction.SOUTH);
		south.normal = Direction.SOUTH.toVector().toDouble();
		south.texture = block.getTexture(Direction.SOUTH);
		//Bottom-left corner
		south.drawVertex(new Vertex(-0.5, -0.5, 0.5, 0, 1).setColor(southColor));
		//Bottom-right corner
		south.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1).setColor(southColor));
		//Top-right corner
		south.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0).setColor(southColor));
		//Top-left corner
		south.drawVertex(new Vertex(-0.5, 0.5, 0.5, 0, 0).setColor(southColor));
		drawShape(south);

		/**
		 * Draw west
		 */
		Face west = createShape();
		Color westColor = block.colorMultiplier(Direction.WEST);
		west.normal = Direction.WEST.toVector().toDouble();
		west.texture = block.getTexture(Direction.WEST);
		//Bottom-left corner
		west.drawVertex(new Vertex(-0.5, -0.5, -0.5, 0, 1).setColor(westColor));
		//Bottom-right corner
		west.drawVertex(new Vertex(-0.5, -0.5, 0.5, 1, 1).setColor(westColor));
		//Top-right corner
		west.drawVertex(new Vertex(-0.5, 0.5, 0.5, 1, 0).setColor(westColor));
		//Top-left corner
		west.drawVertex(new Vertex(-0.5, 0.5, -0.5, 0, 0).setColor(westColor));
		drawShape(west);

		/**
		 * Draw east
		 */
		Face east = createShape();
		Color eastColor = block.colorMultiplier(Direction.EAST);
		east.normal = Direction.EAST.toVector().toDouble();
		east.texture = block.getTexture(Direction.EAST);
		//Top-left corner
		east.drawVertex(new Vertex(0.5, 0.5, -0.5, 0, 0).setColor(eastColor));
		//Top-right corner
		east.drawVertex(new Vertex(0.5, 0.5, 0.5, 1, 0).setColor(eastColor));
		//Bottom-right corner
		east.drawVertex(new Vertex(0.5, -0.5, 0.5, 1, 1).setColor(eastColor));
		//Bottom-left corner
		east.drawVertex(new Vertex(0.5, -0.5, -0.5, 0, 1).setColor(eastColor));
		drawShape(east);

		return this;
	}

	public Model drawCube(
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {
		/**
		 * Draw down
		 */
		Face down = createShape();
		down.normal = Direction.DOWN.toVector().toDouble();
		//Top-left corner
		down.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getBottomMinU(), textureCoordinates.getBottomMinV()));
		//Top-right corner
		down.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getBottomMaxU(), textureCoordinates.getBottomMinV()));
		//Bottom-right corner
		down.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getBottomMaxU(), textureCoordinates.getBottomMaxV()));
		//Bottom-left corner
		down.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getBottomMinU(), textureCoordinates.getBottomMaxV()));
		drawShape(down);

		/**
		 * Draw up
		 */
		Face up = createShape();
		up.normal = Direction.UP.toVector().toDouble();
		//Bottom-left corner
		up.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getTopMinU(), textureCoordinates.getTopMaxV()));
		//Bottom-right corner
		up.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getTopMaxU(), textureCoordinates.getTopMaxV()));
		//Top-right corner
		up.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getTopMaxU(), textureCoordinates.getTopMinV()));
		//Top-left corner
		up.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getTopMinU(), textureCoordinates.getTopMinV()));
		drawShape(up);

		/**
		 * Draw north
		 */
		Face north = createShape();
		north.normal = Direction.NORTH.toVector().toDouble();
		//Top-left corner
		north.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getNorthMinU(), textureCoordinates.getNorthMinV()));
		//Top-right corner
		north.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getNorthMaxU(), textureCoordinates.getNorthMinV()));
		//Bottom-right corner
		north.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getNorthMaxU(), textureCoordinates.getNorthMaxV()));
		//Bottom-left corner
		north.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getNorthMinU(), textureCoordinates.getNorthMaxV()));
		drawShape(north);

		/**
		 * Draw south
		 */
		Face south = createShape();
		south.normal = Direction.SOUTH.toVector().toDouble();
		//Bottom-left corner
		south.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getSouthMinU(), textureCoordinates.getSouthMaxV()));
		//Bottom-right corner
		south.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getSouthMaxU(), textureCoordinates.getSouthMaxV()));
		//Top-right corner
		south.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getSouthMaxU(), textureCoordinates.getSouthMinV()));
		//Top-left corner
		south.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getSouthMinU(), textureCoordinates.getSouthMinV()));
		drawShape(south);

		/**
		 * Draw west
		 */
		Face west = createShape();
		west.normal = Direction.WEST.toVector().toDouble();
		//Bottom-left corner
		west.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getWestMinU(), textureCoordinates.getWestMaxV()));
		//Bottom-right corner
		west.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getWestMaxU(), textureCoordinates.getWestMaxV()));
		//Top-right corner
		west.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getWestMaxU(), textureCoordinates.getWestMinV()));
		//Top-left corner
		west.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getWestMinU(), textureCoordinates.getWestMinV()));
		drawShape(west);

		/**
		 * Draw east
		 */
		Face east = createShape();
		east.normal = Direction.EAST.toVector().toDouble();
		//Top-left corner
		east.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getEastMinU(), textureCoordinates.getEastMinV()));
		//Top-right corner
		east.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getEastMaxU(), textureCoordinates.getEastMinV()));
		//Bottom-right corner
		east.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getEastMaxU(), textureCoordinates.getEastMaxV()));
		//Bottom-left corner
		east.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getEastMinU(), textureCoordinates.getEastMaxV()));
		drawShape(east);

		return this;
	}

	public Model drawCube() {
		return drawCube(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5, StaticCubeTextureCoordinates.INSTANCE);
	}

	public Set<Model> flatten() {
		return flatten(new MatrixStack());
	}

	/**
	 * Flattens the model into a set of models with no additional transformations,
	 * applying all the transformations into the individual vertices.
	 *
	 * @param matrixStack transformation matrix.
	 * @return Resulting set of models
	 */
	public Set<Model> flatten(MatrixStack matrixStack) {
		Set<Model> models = new HashSet<>();

		matrixStack.pushMatrix();
		matrixStack.transform(matrix);
		//Create a new model with transformation applied.
		Model transformedModel = clone();
		transformedModel.faces.stream().forEach(f -> {
				f.normal = f.normal; //TODO normal matrix;
				f.vertices.forEach(v -> v.vec = matrixStack.transform(v.vec));
			}
		);

		models.add(transformedModel);
		//Flatten child models
		models.addAll(children.stream().flatMap(m -> m.flatten(matrixStack).stream()).collect(Collectors.toSet()));
		matrixStack.popMatrix();
		return models;
	}

	@Override
	protected Model clone() {
		Model model = new Model(name);
		model.faces.addAll(faces.stream().map(Face::clone).collect(Collectors.toSet()));
		model.children.addAll(children.stream().map(Model::clone).collect(Collectors.toSet()));
		model.matrix = matrix;
		model.textureOffset = textureOffset;
		return model;
	}
}
