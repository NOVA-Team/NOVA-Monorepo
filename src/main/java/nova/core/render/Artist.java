package nova.core.render;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist is capable of drawing different artworks (shapes).
 *
 * @author Calclavia
 */
public abstract class Artist {
	protected List<Artwork> artworks = new ArrayList<>();

	/**
	 * Starts drawing, by returning an Artwork for the Artist to work on.
	 * Add vertices to this Artwork and finish it by calling endDrawing()
	 *
	 * @return new {@link Artwork}
	 */
	public Artwork startDrawing() {
		return new Artwork();
	}

	/**
	 * Finish drawing the Artwork by adding it into the list of artworks.
	 *
	 * @param artwork - The finished masterpiece.
	 */
	public void endDrawing(Artwork artwork) {
		artworks.add(artwork);
	}

	/**
	 * Draws a 1x1x1 cube
	 *
	 * @return This Artist
	 */
	public Artist drawCube() {

		/**
		 * Draw 6 faces for a cube.

		 for(Direction dir : Direction.DIRECTIONS) {
		 Artwork artwork = startDrawing();
		 artwork.normal = dir.toVector();
		 artwork.drawQuad(new Vertex5(-0.5, -0.5, -0.5, 0, 0));
		 endDrawing(artwork);
		 }

		 drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(-0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(-0.5, 0.5, -0.5));
		 drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, 0.5), new Vector3d(-0.5, -0.5, 0.5));
		 drawQuad(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(-0.5, 0.5, -0.5));
		 drawQuad(new Vector3d(0.5, -0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(0.5, -0.5, 0.5));
		 drawQuad(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, -0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(-0.5, 0.5, 0.5));
		 drawQuad(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0.5, 0.5, 0.5), new Vector3d(0.5, -0.5, 0.5));
		 */
		return this;
	}
}
