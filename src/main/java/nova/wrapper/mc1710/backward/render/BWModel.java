package nova.wrapper.mc1710.backward.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import nova.core.render.model.Model;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.Texture;
import nova.core.util.math.Vector3DUtil;
import nova.wrapper.mc1710.render.RenderUtility;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class BWModel extends Model {

	/**
	 * Completes this rendering for a block.
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
						// Brightness is defined as: skyLight << 20 | blockLight
						// << 4
						tessellator.setBrightness((int) (face.getBrightness() * (15 << 20 | 11 << 4)));
					} else {
						// Determine nearest adjacent block.
						Vector3D nearestPos = Vector3DUtil.floor(face.getCenter().add(face.normal.scalarMultiply(0.6))); // 0.6 so down rounding doesn't get us.
						Block block = blockAccess.getBlock((int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ());
						try {
							int brightness = block.getMixedBrightnessForBlock(blockAccess, (int) nearestPos.getX(), (int) nearestPos.getY(), (int) nearestPos.getZ());

							// TODO: Add Ambient Occlusion

							/*int aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, translation.getX() - 1, translation.getY(), translation.zi());
							int aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, translation.getX(), translation.getY(), translation.zi() - 1);
							int aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, translation.getX(), translation.getY(), translation.zi() + 1);
							int aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, translation.getX() + 1, translation.getY(), translation.zi());

							int brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
							int brightnessTopRight = getAoBrightness(aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
							int brightnessBottomRight = getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
							int brightnessBottomLeft = getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);*/

							tessellator.setBrightness(brightness);

						} catch (Exception e) {
							e.printStackTrace();
						}
						// TODO: Remove this
						// tessellator.setBrightness(15 << 20 | 11 << 4);
					}
					tessellator.setNormal((int) face.normal.getX(), (int) face.normal.getY(), (int) face.normal.getZ());

					if (face.texture.isPresent()) {
						Texture texture = face.texture.get();
						IIcon icon = RenderUtility.instance.getIcon(texture);
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), icon.getInterpolatedU(16 * v.uv.getX()), icon.getInterpolatedV(16 * v.uv.getY()));
						});
					} else {
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertex(v.vec.getX(), v.vec.getY(), v.vec.getZ());
						});
					}
				})
		);
	}

	public void render() {
		render(Optional.empty());
	}

	public void render(Optional<RenderManager> entityRenderManager) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setColorRGBA_F(1, 1, 1, 1);

		/**
		 * Convert textures and UV into Minecraft equivalent.
		 */
		flatten().forEach(
			model ->
			{
				model.faces.forEach(face ->
				{
					// Brightness is defined as: skyLight << 20 | blockLight <<
					// 4
					if (face.getBrightness() >= 0) {
						tessellator.setBrightness((int) (face.getBrightness() * (15 << 20 | 11 << 4)));
					} else {
						// Determine nearest adjacent block.
						tessellator.setBrightness(15 << 20 | 11 << 4);
					}

					tessellator.setNormal((int) face.normal.getX(), (int) face.normal.getY(), (int) face.normal.getZ());

					if (face.texture.isPresent()) {
						if (entityRenderManager.isPresent()) {
							if (face.texture.get() instanceof EntityTexture) {
								//We're not working on an atlas, so just do... this.
								Texture t = face.texture.get();
								entityRenderManager.get().renderEngine.bindTexture(new ResourceLocation(t.domain, "textures/entities/" + t.resource + ".png"));
							}
						}
						Texture texture = face.texture.get();
						IIcon icon = RenderUtility.instance.getIcon(texture);
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							if (icon != null) {
								tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), icon.getInterpolatedU(16 * v.uv.getX()), icon.getInterpolatedV(16 * v.uv.getY()));
							} else {
								tessellator.addVertexWithUV(v.vec.getX(), v.vec.getY(), v.vec.getZ(), v.uv.getX(), v.uv.getY());
							}
						});
					} else {
						face.vertices.forEach(v -> {
							tessellator.setColorRGBA(v.color.red(), v.color.green(), v.color.blue(), v.color.alpha());
							tessellator.addVertex(v.vec.getX(), v.vec.getY(), v.vec.getZ());
						});
					}
				});
			}
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
