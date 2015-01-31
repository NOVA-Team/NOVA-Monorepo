package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import nova.core.render.Artist;

/**
 * @author Calclavia
 */
public class MinecraftArtist extends Artist {

	/**
	 * Completes this rendering masterpiece.
	 */
	public void complete() {
		Tessellator tesselator = Tessellator.instance;
		artworks.stream()
			.flatMap(a -> a.vertices.stream())
			.forEach(v -> tesselator.addVertexWithUV(v.vec.x, v.vec.y, v.vec.z, v.uv.x, v.uv.y));
	}
}
