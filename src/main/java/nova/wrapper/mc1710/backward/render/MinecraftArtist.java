package nova.wrapper.mc1710.backward.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import nova.core.render.Artist;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
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
				Vector3i nearestPos = a.translation.add(a.getCenter()).add(a.normal.divide(2)).round();
				Block block = blockAccess.getBlock(nearestPos.x, nearestPos.y, nearestPos.z);
				int brightness = block.getMixedBrightnessForBlock(blockAccess, nearestPos.x, nearestPos.y, nearestPos.z);
				//TODO: Add Ambient Occlusion
				/*
				int aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, translation.xi() - 1, translation.yi(), translation.zi());
				int aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, translation.xi(), translation.yi(), translation.zi() - 1);
				int aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, translation.xi(), translation.yi(), translation.zi() + 1);
				int aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, translation.xi() + 1, translation.yi(), translation.zi());
				
				int brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
				int brightnessTopRight = getAoBrightness(aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
				int brightnessBottomRight = getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
				int brightnessBottomLeft = getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);
				*/

				if (a.normal.equals(Direction.WEST.toVector())) {
					System.out.println(nearestPos + " with " + brightness);
				}

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

	public int getAoBrightness(int a, int b, int c, int d) {
		if (a == 0) {
			a = d;
		}

		if (b == 0) {
			b = d;
		}

		if (c == 0) {
			c = d;
		}

		return a + b + c + d >> 2 & 16711935;
	}
}
