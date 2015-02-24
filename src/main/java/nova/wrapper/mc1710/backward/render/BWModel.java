package nova.wrapper.mc1710.backward.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import nova.core.block.BlockAccess;
import nova.core.render.model.Model;
import nova.wrapper.mc1710.render.RenderUtility;

/**
 * @author Calclavia
 */
public class BWModel extends Model {

	/**
	 * Completes this rendering for a block.
	 *
	 * @param blockAccess {@link BlockAccess}
	 * @param translation Translation
	 */
	public void renderWorld(IBlockAccess blockAccess) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		/**
		 * Convert textures and UV into Minecraft equivalent. 
		 */
		flatten().forEach(model ->
				model.faces.forEach(face ->
				{
					if (face.getBrightness() >= 0) {
						//Brightness is defined as: skyLight << 20 | blockLight << 4
						tessellator.setBrightness((int) face.getBrightness());
					} else {
						//Determine nearest adjacent block.
						/*Vector3i nearestPos = face.getCenter().add(face.normal.divide(2)).round();
						Block block = blockAccess.getBlock(nearestPos.x, nearestPos.y, nearestPos.z);
						try {
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
						*
							tessellator.setBrightness(brightness);

						} catch (Exception e) {
							e.printStackTrace();
						}*/
						//TODO: Remove this
						tessellator.setBrightness(15 << 20 | 11 << 4);
					}
					tessellator.setNormal(face.normal.xf(), face.normal.yf(), face.normal.zf());

					if (face.texture.isPresent()) {
						IIcon icon = RenderUtility.instance.getIcon(face.texture.get());
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertexWithUV(v.vec.x, v.vec.y, v.vec.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y));
						});
					} else {
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z);
						});
					}
				})
		);
	}

	public void render() {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		/**
		 * Convert textures and UV into Minecraft equivalent.
		 */
		flatten().forEach(model ->
				model.faces.forEach(face ->
				{
					//Brightness is defined as: skyLight << 20 | blockLight << 4
					if (face.getBrightness() >= 0) {
						tessellator.setBrightness((int) face.getBrightness());
					} else {
						//Determine nearest adjacent block.
						tessellator.setBrightness(15 << 20 | 11 << 4);
					}

					tessellator.setNormal(face.normal.xf(), face.normal.yf(), face.normal.zf());

					if (face.texture.isPresent()) {
						IIcon icon = RenderUtility.instance.getIcon(face.texture.get());
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertexWithUV(v.vec.x, v.vec.y, v.vec.z, icon.getInterpolatedU(16 * v.uv.x), icon.getInterpolatedV(16 * v.uv.y));
						});
					} else {
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertex(v.vec.x, v.vec.y, v.vec.z);
						});
					}
				})
		);
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
