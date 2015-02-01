package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import nova.core.block.BlockAccess;
import nova.core.render.Artist;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.util.RenderUtility;

/**
 * @author Calclavia
 */
public class MinecraftArtist extends Artist {

	//TODO: These are hacked values. It's VERY BAD to use these.
	public IBlockAccess accessHack;

	/**
	 * Completes this rendering masterpiece.
	 */
	public void complete(Vector3d translation) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		artworks.forEach(a -> a.translation = a.rotation.rotate(a.translation).add(translation));

		/**
		 * Convert textures and UV into Minecraft equivalent. 
		 */
		artworks.forEach(a ->
		{
			tessellator.setBrightness((int) a.brightness);
			tessellator.setNormal(a.normal.xf(), a.normal.yf(), a.normal.zf());
			if (a.texture.isPresent()) {
				IIcon icon = RenderUtility.instance.getIcon(a.texture.get());
				a.vertices.forEach(v -> tessellator.addVertexWithUV(v.vec.x + a.translation.x, v.vec.y + a.translation.y, v.vec.z + a.translation.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y)));
			} else {
				a.vertices.forEach(v -> tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z));
			}
		});
	}

	@Override
	protected double getBrightness(BlockAccess access, Vector3i position) {
		if (accessHack != null) {
			//			return accessHack.getBlock(position.x, position.y, position.z).getMixedBrightnessForBlock(accessHack, position.x, position.y, position.z);
		}
		return 1;
	}
}
