package nova.core.render;

import nova.core.block.Block;
import nova.core.util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist is capable of drawing different artworks (shapes).
 * @author Calclavia
 */
public abstract class Artist {
	/**
	 * A list of all the shapes drawn.
	 */
	protected List<Face> artworks = new ArrayList<>();

	/**
	 * Starts drawing, by returning an Artwork for the Artist to work on.
	 * Add vertices to this Artwork and finish it by calling drawShape()
	 * @return new {@link Face}
	 */
	public Face createShape() {
		return new Face();
	}

	/**
	 * Finish drawing the Artwork by adding it into the list of artworks.
	 * @param artwork - The finished masterpiece.
	 */
	public void drawShape(Face artwork) {
		artworks.add(artwork);
	}

	/**
	 * Draws a standard block.
	 * @param block Block to draw
	 * @return This Artist
	 */
	public Artist renderBlock(Block block) {
		//TODO: Change render size based on block bounds.
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
}
