package nova.core.render.model;

import nova.core.block.Block;
import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.misc.Collider;
import nova.core.render.Color;
import nova.core.render.RenderException;
import nova.core.util.Direction;
import nova.core.util.shape.Cuboid;

import java.util.Optional;

/**
 * Block model helper methods
 * @author Calclavia
 */
public class BlockModelUtil {

	/**
	 * Draws a standard block.
	 *
	 * @param model Model to use
	 * @param block Block to draw
	 * @return This Model
	 */
	public static Model drawBlock(Model model, Block block) {
		Optional<StaticBlockRenderer> staticRendererOp = block.getOp(StaticBlockRenderer.class);

		if (staticRendererOp.isPresent()) {
			StaticBlockRenderer staticRenderer = staticRendererOp.get();
			Cuboid boundingBox = staticRenderer.bounds.get();
			double minX = boundingBox.min.getX() - 0.5;
			double minY = boundingBox.min.getY() - 0.5;
			double minZ = boundingBox.min.getZ() - 0.5;
			double maxX = boundingBox.max.getX() - 0.5;
			double maxY = boundingBox.max.getY() - 0.5;
			double maxZ = boundingBox.max.getZ() - 0.5;

			if (staticRenderer.renderSide.apply(Direction.DOWN)) {
				Color downColor = staticRenderer.colorMultiplier.apply(Direction.DOWN);
				Face face = drawDown(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.DOWN);
				face.vertices.forEach(v -> v.color = downColor);
			}
			if (staticRenderer.renderSide.apply(Direction.UP)) {
				Color upColor = staticRenderer.colorMultiplier.apply(Direction.UP);
				Face face = drawUp(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.UP);
				face.vertices.forEach(v -> v.color = upColor);
			}
			if (staticRenderer.renderSide.apply(Direction.NORTH)) {
				Color northColor = staticRenderer.colorMultiplier.apply(Direction.NORTH);
				Face face = drawNorth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.NORTH);
				face.vertices.forEach(v -> v.color = northColor);
			}
			if (staticRenderer.renderSide.apply(Direction.SOUTH)) {
				Color southColor = staticRenderer.colorMultiplier.apply(Direction.SOUTH);
				Face face = drawSouth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.SOUTH);
				face.vertices.forEach(v -> v.color = southColor);
			}
			if (staticRenderer.renderSide.apply(Direction.WEST)) {
				Color westColor = staticRenderer.colorMultiplier.apply(Direction.WEST);
				Face face = drawWest(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.WEST);
				face.vertices.forEach(v -> v.color = westColor);
			}
			if (staticRenderer.renderSide.apply(Direction.EAST)) {
				Color eastColor = staticRenderer.colorMultiplier.apply(Direction.EAST);
				Face face = drawEast(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.texture.apply(Direction.EAST);
				face.vertices.forEach(v -> v.color = eastColor);
			}
			return model;
		}

		return null;
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face down = model.createFace();
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face up = model.createFace();
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face north = model.createFace();
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face south = model.createFace();
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face west = model.createFace();
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
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face east = model.createFace();
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
		Model model,
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
	public static Model drawCube(
		Model model,
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
	 * @param model The model to apply the textures to
	 * @param cuboid The cuboid where the moddel aplies to
	 * @param textureCoordinates The texturecoordinates to use
	 * @return The model with the textures aplied
	 */
	public static Model drawCube(Model model, Cuboid cuboid, CubeTextureCoordinates textureCoordinates) {
		return drawCube(model, cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ(), textureCoordinates);
	}

	public static Model drawCube(Model model) {
		return drawCube(model, -0.5, -0.5, -0.5, 0.5, 0.5, 0.5, StaticCubeTextureCoordinates.instance);
	}

}
