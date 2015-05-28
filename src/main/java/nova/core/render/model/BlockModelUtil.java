package nova.core.render.model;

import nova.core.block.Block;
import nova.core.block.component.StaticBlockRenderer;
import nova.core.component.misc.Collider;
import nova.core.render.Color;
import nova.core.util.Direction;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.shape.Cuboid;

import java.util.Optional;

/**
 * Block model helper methods
 * @author Calclavia
 */
public class BlockModelUtil {

	/**
	 * Draws a standard block.
	 * @param block Block to draw
	 * @return This Model
	 */
	public static Model drawBlock(Model model, Block block) {
		Optional<Collider> collider = block.getOp(Collider.class);
		Optional<StaticBlockRenderer> staticRenderer = block.getOp(StaticBlockRenderer.class);

		if (collider.isPresent() && staticRenderer.isPresent()) {
			Cuboid boundingBox = collider.get().boundingBox;
			double minX = boundingBox.min.x - 0.5;
			double minY = boundingBox.min.y - 0.5;
			double minZ = boundingBox.min.z - 0.5;
			double maxX = boundingBox.max.x - 0.5;
			double maxY = boundingBox.max.y - 0.5;
			double maxZ = boundingBox.max.z - 0.5;

			if (staticRenderer.get().renderSide.apply(Direction.DOWN)) {
				Color downColor = staticRenderer.get().colorMultiplier.apply(Direction.DOWN);
				Face face = drawDown(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.DOWN);
				face.vertices.forEach(v -> v.setColor(downColor));
			}
			if (staticRenderer.get().renderSide.apply(Direction.UP)) {
				Color upColor = staticRenderer.get().colorMultiplier.apply(Direction.UP);
				Face face = drawUp(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.UP);
				face.vertices.forEach(v -> v.setColor(upColor));
			}
			if (staticRenderer.get().renderSide.apply(Direction.NORTH)) {
				Color northColor = staticRenderer.get().colorMultiplier.apply(Direction.NORTH);
				Face face = drawNorth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.NORTH);
				face.vertices.forEach(v -> v.setColor(northColor));
			}
			if (staticRenderer.get().renderSide.apply(Direction.SOUTH)) {
				Color southColor = staticRenderer.get().colorMultiplier.apply(Direction.SOUTH);
				Face face = drawSouth(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.SOUTH);
				face.vertices.forEach(v -> v.setColor(southColor));
			}
			if (staticRenderer.get().renderSide.apply(Direction.WEST)) {
				Color westColor = staticRenderer.get().colorMultiplier.apply(Direction.WEST);
				Face face = drawWest(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.WEST);
				face.vertices.forEach(v -> v.setColor(westColor));
			}
			if (staticRenderer.get().renderSide.apply(Direction.EAST)) {
				Color eastColor = staticRenderer.get().colorMultiplier.apply(Direction.EAST);
				Face face = drawEast(model, minX, minY, minZ, maxX, maxY, maxZ, StaticCubeTextureCoordinates.instance);
				face.texture = staticRenderer.get().texture.apply(Direction.EAST);
				face.vertices.forEach(v -> v.setColor(eastColor));
			}
			return model;
		}

		return null;
	}

	public static Face drawDown(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face down = model.createFace();
		down.normal = Direction.DOWN.toVector().toDouble();
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

	public static Face drawUp(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face up = model.createFace();
		up.normal = Direction.UP.toVector().toDouble();
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

	public static Face drawNorth(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face north = model.createFace();
		north.normal = Direction.NORTH.toVector().toDouble();
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

	public static Face drawSouth(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face south = model.createFace();
		south.normal = Direction.SOUTH.toVector().toDouble();
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

	public static Face drawWest(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face west = model.createFace();
		west.normal = Direction.WEST.toVector().toDouble();
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

	public static Face drawEast(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {

		Face east = model.createFace();
		east.normal = Direction.EAST.toVector().toDouble();
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

		throw new NovaException("Invalid direction!");
	}

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

	public static Model drawCube(Model model, Cuboid cuboid, CubeTextureCoordinates textureCoordinates) {
		return drawCube(model, cuboid.min.x, cuboid.min.y, cuboid.min.z, cuboid.max.x, cuboid.max.y, cuboid.max.z, textureCoordinates);
	}

	public static Model drawCube(Model model) {
		return drawCube(model, -0.5, -0.5, -0.5, 0.5, 0.5, 0.5, StaticCubeTextureCoordinates.instance);
	}

}
