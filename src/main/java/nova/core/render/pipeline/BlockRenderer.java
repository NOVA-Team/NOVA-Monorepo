package nova.core.render.pipeline;

import nova.core.block.Block;
import nova.core.component.misc.Collider;
import nova.core.component.transform.Orientation;
import nova.core.render.Color;
import nova.core.render.RenderException;
import nova.core.render.model.Face;
import nova.core.render.model.Model;
import nova.core.render.model.Vertex;
import nova.core.render.model.VertexModel;
import nova.core.render.texture.Texture;
import nova.core.util.Direction;
import nova.core.util.shape.Cuboid;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A block rendering builder that generates a function that renders a block model.
 *
 * @author Calclavia
 */
public abstract class BlockRenderer implements Consumer<RenderStream> {

	public final Block block;

	/**
	 * Called to get the texture of this block for a certain side.
	 * side - The side of the block that the texture is for.
	 * Returns -  An optional of the texture.
	 */
	public Function<Direction, Optional<Texture>> texture = (dir) -> Optional.empty();

	/**
	 * Called to get a shape of this block to be rendered.
	 * Defaults to collision box or to Cuboid.ONE, if there
	 * is no Collider supplied.
	 */
	public Supplier<Cuboid> bounds;

	/**
	 * Called when this block is to be rendered.
	 * Direction - The direction to render
	 * Returns - true if the side should render
	 */
	public Predicate<Direction> renderSide = (dir) -> true;

	/**
	 * Gets the color of a specific face. This is called by the default block
	 * renderer.
	 * direction - The side of the block.
	 * Returns the color
	 */
	public Function<Direction, Color> colorMultiplier = (dir) -> Color.white;

	public BlockRenderer(Block block) {
		this.block = block;
		bounds = () -> block.getOp(Collider.class).map(c -> c.boundingBox.get()).orElse(Cuboid.ONE);
	}

	public BlockRenderer with(Function<Direction, Optional<Texture>> texture) {
		this.texture = texture;
		return this;
	}

	public BlockRenderer with(Texture t) {
		Objects.requireNonNull(t, "Texture is null, please initiate the texture before the block");
		this.texture = (dir) -> Optional.of(t);
		return this;
	}

	public BlockRenderer with(Supplier<Cuboid> bounds) {
		this.bounds = bounds;
		return this;
	}

	public BlockRenderer with(Cuboid bounds) {
		this.bounds = () -> bounds;
		return this;
	}

	public BlockRenderer filter(Predicate<Direction> renderSide) {
		this.renderSide = renderSide;
		return this;
	}

	public BlockRenderer withColor(Color colorMultiplier) {
		this.colorMultiplier = dir -> colorMultiplier;
		return this;
	}

	public BlockRenderer withColor(Function<Direction, Color> colorMultiplier) {
		this.colorMultiplier = colorMultiplier;
		return this;
	}

	@Override
	public void accept(RenderStream renderStream) {
		renderStream.result = model -> model.addChild(draw(new VertexModel()));
	}

	public static class ApplyOrientation implements Consumer<Model> {
		public final Orientation orientation;

		public ApplyOrientation(Orientation orientation) {
			this.orientation = orientation;
		}

		@Override
		public void accept(Model model) {
			model.matrix.rotate(orientation.orientation().rotation);
		}
	}

	/**
	 * Draws a standard block.
	 *
	 * @param model VertexModel to use
	 * @return This VertexModel
	 */
	public VertexModel draw(VertexModel model) {
		Cuboid boundingBox = bounds.get();
		double minX = boundingBox.min.getX() - 0.5;
		double minY = boundingBox.min.getY() - 0.5;
		double minZ = boundingBox.min.getZ() - 0.5;
		double maxX = boundingBox.max.getX() - 0.5;
		double maxY = boundingBox.max.getY() - 0.5;
		double maxZ = boundingBox.max.getZ() - 0.5;

		if (renderSide.test(Direction.DOWN)) {
			Color downColor = colorMultiplier.apply(Direction.DOWN);
			Face face = drawDown(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.DOWN);
			face.vertices.forEach(v -> v.color = downColor);
		}
		if (renderSide.test(Direction.UP)) {
			Color upColor = colorMultiplier.apply(Direction.UP);
			Face face = drawUp(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.UP);
			face.vertices.forEach(v -> v.color = upColor);
		}
		if (renderSide.test(Direction.NORTH)) {
			Color northColor = colorMultiplier.apply(Direction.NORTH);
			Face face = drawNorth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.NORTH);
			face.vertices.forEach(v -> v.color = northColor);
		}
		if (renderSide.test(Direction.SOUTH)) {
			Color southColor = colorMultiplier.apply(Direction.SOUTH);
			Face face = drawSouth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.SOUTH);
			face.vertices.forEach(v -> v.color = southColor);
		}
		if (renderSide.test(Direction.WEST)) {
			Color westColor = colorMultiplier.apply(Direction.WEST);
			Face face = drawWest(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.WEST);
			face.vertices.forEach(v -> v.color = westColor);
		}
		if (renderSide.test(Direction.EAST)) {
			Color eastColor = colorMultiplier.apply(Direction.EAST);
			Face face = drawEast(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
			face.texture = texture.apply(Direction.EAST);
			face.vertices.forEach(v -> v.color = eastColor);
		}
		return model;
	}

	/**
	 * Creates the botom face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The bottom face of the model
	 */
	public static Face drawDown(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face down = new Face();
		down.normal = Direction.DOWN.toVector();
		//Top-left corner
		down.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getBottomMinU(), textureCoordinates.getBottomMinV()));
		//Top-right corner
		down.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getBottomMaxU(), textureCoordinates.getBottomMinV()));
		//Bottom-right corner
		down.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getBottomMaxU(), textureCoordinates.getBottomMaxV()));
		//Bottom-left corner
		down.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getBottomMinU(), textureCoordinates.getBottomMaxV()));
		model.drawFace(down);
		return down;
	}

	/**
	 * Creates the top face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The top face of the model
	 */
	public static Face drawUp(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face up = new Face();
		up.normal = Direction.UP.toVector();
		//Bottom-left corner
		up.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getTopMinU(), textureCoordinates.getTopMaxV()));
		//Bottom-right corner
		up.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getTopMaxU(), textureCoordinates.getTopMaxV()));
		//Top-right corner
		up.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getTopMaxU(), textureCoordinates.getTopMinV()));
		//Top-left corner
		up.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getTopMinU(), textureCoordinates.getTopMinV()));
		model.drawFace(up);
		return up;
	}

	/**
	 * Creates the north face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The north face of the model
	 */
	public static Face drawNorth(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face north = new Face();
		north.normal = Direction.NORTH.toVector();
		//Top-left corner
		north.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getNorthMinU(), textureCoordinates.getNorthMinV()));
		//Top-right corner
		north.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getNorthMaxU(), textureCoordinates.getNorthMinV()));
		//Bottom-right corner
		north.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getNorthMaxU(), textureCoordinates.getNorthMaxV()));
		//Bottom-left corner
		north.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getNorthMinU(), textureCoordinates.getNorthMaxV()));
		model.drawFace(north);

		return north;
	}

	/**
	 * Creates the south face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The south face of the model
	 */
	public static Face drawSouth(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face south = new Face();
		south.normal = Direction.SOUTH.toVector();
		//Bottom-left corner
		south.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getSouthMinU(), textureCoordinates.getSouthMaxV()));
		//Bottom-right corner
		south.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getSouthMaxU(), textureCoordinates.getSouthMaxV()));
		//Top-right corner
		south.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getSouthMaxU(), textureCoordinates.getSouthMinV()));
		//Top-left corner
		south.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getSouthMinU(), textureCoordinates.getSouthMinV()));
		model.drawFace(south);

		return south;
	}

	/**
	 * Creates the west face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The west face of the model
	 */
	public static Face drawWest(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face west = new Face();
		west.normal = Direction.WEST.toVector();
		//Bottom-left corner
		west.drawVertex(new Vertex(minX, minY, minZ, textureCoordinates.getWestMinU(), textureCoordinates.getWestMaxV()));
		//Bottom-right corner
		west.drawVertex(new Vertex(minX, minY, maxZ, textureCoordinates.getWestMaxU(), textureCoordinates.getWestMaxV()));
		//Top-right corner
		west.drawVertex(new Vertex(minX, maxY, maxZ, textureCoordinates.getWestMaxU(), textureCoordinates.getWestMinV()));
		//Top-left corner
		west.drawVertex(new Vertex(minX, maxY, minZ, textureCoordinates.getWestMinU(), textureCoordinates.getWestMinV()));
		model.drawFace(west);

		return west;
	}

	/**
	 * Creates the east face of the model
	 *
	 * @param model The model to render
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The east face of the model
	 */
	public static Face drawEast(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face east = new Face();
		east.normal = Direction.EAST.toVector();
		//Top-left corner
		east.drawVertex(new Vertex(maxX, maxY, minZ, textureCoordinates.getEastMinU(), textureCoordinates.getEastMinV()));
		//Top-right corner
		east.drawVertex(new Vertex(maxX, maxY, maxZ, textureCoordinates.getEastMaxU(), textureCoordinates.getEastMinV()));
		//Bottom-right corner
		east.drawVertex(new Vertex(maxX, minY, maxZ, textureCoordinates.getEastMaxU(), textureCoordinates.getEastMaxV()));
		//Bottom-left corner
		east.drawVertex(new Vertex(maxX, minY, minZ, textureCoordinates.getEastMinU(), textureCoordinates.getEastMaxV()));
		model.drawFace(east);

		return east;
	}

	/**
	 * Creates a face of the model in a specified direction
	 *
	 * @param dir The direction of the face to make
	 * @param model The model to use
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The face of the model in that dirction
	 */
	public static Face drawDir(Direction dir,
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		switch (dir) {
			case DOWN:
				return drawDown(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
			case UP:
				return drawUp(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
			case NORTH:
				return drawNorth(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
			case SOUTH:
				return drawSouth(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
			case EAST:
				return drawEast(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
			case WEST:
				return drawWest(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		}

		throw new RenderException("Invalid draw direction!");
	}

	/**
	 * Applies the textures to the model
	 *
	 * @param model The model to use
	 * @param minX Min X coord
	 * @param minY Min Y coord
	 * @param minZ Min Z coord
	 * @param maxX Max X coord
	 * @param maxY Max Y coord
	 * @param maxZ Max Z coord
	 * @param textureCoordinates Texture coordinates to render
	 * @return The cube model with textures
	 */
	public static VertexModel drawCube(
		VertexModel model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		drawDown(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		drawUp(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		drawNorth(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		drawSouth(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		drawWest(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);
		drawEast(model, minX, minY, minZ, maxX, maxY, maxZ, textureCoordinates);

		return model;
	}

	/**
	 * Binds the specified texturecoordinates to the model for the specified cuboid for rendering
	 *
	 * @param model The model to apply the textures to
	 * @param cuboid The cuboid where the moddel aplies to
	 * @param textureCoordinates The texturecoordinates to use
	 * @return The model with the textures aplied
	 */
	public static VertexModel drawCube(VertexModel model, Cuboid cuboid, CubeTextureCoordinates textureCoordinates) {
		return drawCube(model, cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ(), textureCoordinates);
	}

	public static VertexModel drawCube(VertexModel model) {
		return drawCube(model, -0.5, -0.5, -0.5, 0.5, 0.5, 0.5, StaticCubeTextureCoordinates.instance);
	}

}
