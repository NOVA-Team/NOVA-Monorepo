package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import nova.core.render.Artist;

/**
 * @author Calclavia
 */
public class MinecraftArtist extends Artist {

	/**
	 * Completes this rendering masterpiece.
	 * @param tesselator - The Minecraft tesselator.
	 */
	public void complete(Tessellator tesselator) {
		tesselator.draw();
		tesselator.startDrawingQuads();
		canvases.forEach(c -> c.vertices.forEach(v -> tesselator.addVertex(v.x, v.y, v.z)));
	}
}
