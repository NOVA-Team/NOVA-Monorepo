package nova.core.render;

import nova.core.block.Block;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist is capable of drawing different artworks (shapes).
 * @author Calclavia
 */
public abstract class Artist {
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
		 * Draw the side faces of a cube.
		 */
		for (Direction dir : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST }) {
			Artwork artwork = startDrawing();
			artwork.texture = block.getTexture(dir);
			Vector3d dirVec = dir.toVector().toDouble().multiply(0.5);
			//Draw a square clockwise
			//Top-left corner
			artwork.drawVertex(new Vertex5(dirVec.x != 0 ? dirVec.x : -0.5, 0.5, dirVec.z != 0 ? dirVec.z : -0.5, 0, 0));
			//Top-right corner
			artwork.drawVertex(new Vertex5(dirVec.x != 0 ? dirVec.x : 0.5, 0.5, dirVec.z != 0 ? dirVec.z : 0.5, 1, 0));
			//Bottom-right corner
			artwork.drawVertex(new Vertex5(dirVec.x != 0 ? dirVec.x : -0.5, -0.5, dirVec.z != 0 ? dirVec.z : 0.5, 1, 1));
			//Bottom-left corner
			artwork.drawVertex(new Vertex5(dirVec.x != 0 ? dirVec.x : -0.5, -0.5, dirVec.z != 0 ? dirVec.z : -0.5, 0, 1));
			endDrawing(artwork);
		}

		/**
		 * Draw top/bottom faces
		 */
		for (Direction dir : new Direction[] { Direction.UP, Direction.DOWN }) {
			Artwork artwork = startDrawing();
			artwork.texture = block.getTexture(dir);
			Vector3d dirVec = dir.toVector().toDouble().multiply(0.5);
			//Draw a square clockwise
			//Top-left corner
			artwork.drawVertex(new Vertex5(0.5, dirVec.y, 0.5, 0, 0));
			//Top-right corner
			artwork.drawVertex(new Vertex5(-0.5, dirVec.y, 0.5, 1, 0));
			//Bottom-right corner
			artwork.drawVertex(new Vertex5(-0.5, dirVec.y, -0.5, 1, 1));
			//Bottom-left corner
			artwork.drawVertex(new Vertex5(0.5, dirVec.y, -0.5, 0, 1));
			endDrawing(artwork);
		}
		return this;
	}
}
