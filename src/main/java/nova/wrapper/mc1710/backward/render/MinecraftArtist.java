package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import nova.core.render.Artist;
import nova.wrapper.mc1710.util.RenderUtility;

/**
 * @author Calclavia
 */
public class MinecraftArtist extends Artist {

	/**
	 * Completes this rendering masterpiece.
	 */
	public void complete() {
		Tessellator tessellator = Tessellator.instance;

		/**
		 * Convert textures and UV into Minecraft equivalent. 
		 */
		artworks.forEach(a ->
		{
			if (a.texture.isPresent()) {
				IIcon icon = RenderUtility.instance.getIcon(a.texture.get());
				a.vertices.forEach(v -> tessellator.addVertexWithUV(v.vec.x, v.vec.y, v.vec.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y)));
			} else {
				a.vertices.forEach(v -> tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z));
			}
		});
	}
}
