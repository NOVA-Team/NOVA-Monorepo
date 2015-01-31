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
	protected List<Artwork> artworks = new ArrayList<>();

	/**
	 * Starts drawing, by returning an Artwork for the Artist to work on.
	 * Add vertices to this Artwork and finish it by calling endDrawing()
	 * @return new {@link Artwork}
	 */
	public Artwork startDrawing() {
		return new Artwork();
	}

	/**
	 * Finish drawing the Artwork by adding it into the list of artworks.
	 * @param artwork - The finished masterpiece.
	 */
	public void endDrawing(Artwork artwork) {
		artworks.add(artwork);
	}

	/**
	 * Draws a standard block.
	 * @return This Artist
	 */
	public Artist renderBlock(Block block) {
		//TODO: Change render size based on block bounds.
		/**
		 * Draw down
		 */
		Artwork down = startDrawing();
		down.texture = block.getTexture(Direction.UP);
		//Top-left corner
		down.drawVertex(new Vertex5(0.5, -0.5, 0.5, 0, 0));
		//Top-right corner
		down.drawVertex(new Vertex5(-0.5, -0.5, 0.5, 1, 0));
		//Bottom-right corner
		down.drawVertex(new Vertex5(-0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		down.drawVertex(new Vertex5(0.5, -0.5, -0.5, 0, 1));
		endDrawing(down);

		/**
		 * Draw up
		 */
		Artwork up = startDrawing();
		up.texture = block.getTexture(Direction.UP);
		//Bottom-left corner
		up.drawVertex(new Vertex5(0.5, 0.5, -0.5, 0, 1));
		//Bottom-right corner
		up.drawVertex(new Vertex5(-0.5, 0.5, -0.5, 1, 1));
		//Top-right corner
		up.drawVertex(new Vertex5(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		up.drawVertex(new Vertex5(0.5, 0.5, 0.5, 0, 0));
		endDrawing(up);

		/**
		 * Draw north
		 */
		Artwork north = startDrawing();
		north.texture = block.getTexture(Direction.NORTH);
		//Top-left corner
		north.drawVertex(new Vertex5(-0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		north.drawVertex(new Vertex5(0.5, 0.5, -0.5, 1, 0));
		//Bottom-right corner
		north.drawVertex(new Vertex5(0.5, -0.5, -0.5, 1, 1));
		//Bottom-left corner
		north.drawVertex(new Vertex5(-0.5, -0.5, -0.5, 0, 1));
		endDrawing(north);

		/**
		 * Draw south
		 */
		Artwork south = startDrawing();
		south.texture = block.getTexture(Direction.SOUTH);
		//Bottom-left corner
		south.drawVertex(new Vertex5(-0.5, -0.5, 0.5, 0, 1));
		//Bottom-right corner
		south.drawVertex(new Vertex5(0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		south.drawVertex(new Vertex5(0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		south.drawVertex(new Vertex5(-0.5, 0.5, 0.5, 0, 0));
		endDrawing(south);

		/**
		 * Draw west
		 */
		Artwork west = startDrawing();
		west.texture = block.getTexture(Direction.WEST);
		//Bottom-left corner
		west.drawVertex(new Vertex5(-0.5, -0.5, -0.5, 0, 1));
		//Bottom-right corner
		west.drawVertex(new Vertex5(-0.5, -0.5, 0.5, 1, 1));
		//Top-right corner
		west.drawVertex(new Vertex5(-0.5, 0.5, 0.5, 1, 0));
		//Top-left corner
		west.drawVertex(new Vertex5(-0.5, 0.5, -0.5, 0, 0));
		endDrawing(west);

		/**
		 * Draw east
		 */
		Artwork east = startDrawing();
		east.texture = block.getTexture(Direction.EAST);
		//Top-left corner
		east.drawVertex(new Vertex5(0.5, 0.5, -0.5, 0, 0));
		//Top-right corner
		east.drawVertex(new Vertex5(0.5, 0.5, 0.5, 1, 0));
		//Bottom-right corner
		east.drawVertex(new Vertex5(0.5, -0.5, 0.5, 1, 1));
		//Bottom-left corner
		east.drawVertex(new Vertex5(0.5, -0.5, -0.5, 0, 1));
		endDrawing(east);

		return this;
	}
}
