package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import nova.core.render.Artist;
import nova.core.util.transform.Vector3d;
import nova.wrapper.mc1710.util.RenderUtility;

/**
 * @author Calclavia
 */
public class MinecraftArtist extends Artist {

	/**
	 * Completes this rendering for a block.
	 * @param translation Translation
	 */
	public void renderWorld(IBlockAccess blockAccess, Vector3d translation) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		//Apply transformation: rotate, scale, translate.
		artworks.forEach(a -> a.translation = a.rotation.rotate(a.translation).multiply(a.scale).add(translation));

		/**
		 * Convert textures and UV into Minecraft equivalent. 
		 */
		artworks.forEach(a ->
		{
			//Brightness is defined as: skyLight << 20 | blockLight << 4
			if (a.getBrightness() >= 0) {
				tessellator.setBrightness((int) a.getBrightness());
			} else {
				//Determine nearest adjacent block.
				//Vector3i nearestPos = a.translation.add(a.getCenter()).add(a.normal.divide(2)).toInt();
				//int brightness = blockAccess.getBlock(nearestPos.x, nearestPos.y, nearestPos.z).getMixedBrightnessForBlock(blockAccess, nearestPos.x, nearestPos.y, nearestPos.z);
				int brightness = blockAccess.getBlock(translation.xi(), translation.yi(), translation.zi()).getMixedBrightnessForBlock(blockAccess, translation.xi(), translation.yi(), translation.zi());
				tessellator.setBrightness(brightness);
			}

			tessellator.setNormal(a.normal.xf(), a.normal.yf(), a.normal.zf());

			if (a.texture.isPresent()) {
				IIcon icon = RenderUtility.instance.getIcon(a.texture.get());
				a.vertices.forEach(v -> tessellator.addVertexWithUV(v.vec.x + a.translation.x, v.vec.y + a.translation.y, v.vec.z + a.translation.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y)));
			} else {
				a.vertices.forEach(v -> tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z));
			}
		});
	}

	public void renderItem() {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		//Apply transformation: rotate, scale, translate.
		artworks.forEach(a -> a.translation = a.rotation.rotate(a.translation).multiply(a.scale));

		/**
		 * Convert textures and UV into Minecraft equivalent.
		 */
		artworks.forEach(a ->
		{
			//Brightness is defined as: skyLight << 20 | blockLight << 4
			if (a.getBrightness() >= 0) {
				tessellator.setBrightness((int) a.getBrightness());
			} else {
				//Determine nearest adjacent block.
				tessellator.setBrightness(15 << 20 | 11 << 4);
			}

			tessellator.setNormal(a.normal.xf(), a.normal.yf(), a.normal.zf());

			if (a.texture.isPresent()) {
				IIcon icon = RenderUtility.instance.getIcon(a.texture.get());
				a.vertices.forEach(v -> tessellator.addVertexWithUV(v.vec.x + a.translation.x, v.vec.y + a.translation.y, v.vec.z + a.translation.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y)));
			} else {
				a.vertices.forEach(v -> tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z));
			}
		});
	}
}
