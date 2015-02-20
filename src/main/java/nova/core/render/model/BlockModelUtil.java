package nova.core.render.model;

import nova.core.block.Block;
import nova.core.render.Color;
import nova.core.util.Direction;

/**
 * Block model helper methods
 *
 * @author Calclavia
 */
public class BlockModelUtil {

	/**
	 * Draws a standard block.
	 *
	 * @param block Block to draw
	 * @return This Model
	 */
	public static Model drawBlock(Model model, Block block) {
		//TODO: Change render size based on block bounds and check sides
		/**
		 * Draw down
		 */
		Face down = model.createFace();
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
		model.drawFace(down);

		/**
		 * Draw up
		 */
		Face up = model.createFace();
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
		model.drawFace(up);

		/**
		 * Draw north
		 */
		Face north = model.createFace();
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
		model.drawFace(north);

		/**
		 * Draw south
		 */
		Face south = model.createFace();
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
		model.drawFace(south);

		/**
		 * Draw west
		 */
		Face west = model.createFace();
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
		model.drawFace(west);

		/**
		 * Draw east
		 */
		Face east = model.createFace();
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
		model.drawFace(east);
		return model;
	}

	public static Model drawCube(
		Model model,
		double minX, double minY, double minZ,
		double maxX, double maxY, double maxZ,
		CubeTextureCoordinates textureCoordinates) {
		/**
		 * Draw down
		 */
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

		/**
		 * Draw up
		 */
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

		/**
		 * Draw north
		 */
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

		/**
		 * Draw south
		 */
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

		/**
		 * Draw west
		 */
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

		/**
		 * Draw east
		 */
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
		return model;
	}

	public Model drawCube(Model model) {
		return drawCube(model, -0.5, -0.5, -0.5, 0.5, 0.5, 0.5, StaticCubeTextureCoordinates.INSTANCE);
	}

}
