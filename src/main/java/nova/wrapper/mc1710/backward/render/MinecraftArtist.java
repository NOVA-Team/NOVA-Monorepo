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
		//canvases.forEach(c -> c.vertices.forEach(v -> tesselator.addVertexWithUV(v.vec.x, v.vec.y, v.vec.z, v.uv.x, v.uv.y)));
	}
}
