package nova.core.wrapper.mc17.wrapper.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import nova.core.render.RenderException;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

/**
 * @author Calclavia
 */
public class BWClientRenderManager extends BWRenderManager {

	//TODO: Would this break server?
	@SideOnly(Side.CLIENT)
	public static RenderItem renderItem = new RenderItem();

	static {
		renderItem.setRenderManager(RenderManager.instance);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Vector2D getDimension(Texture texture) {
		ResourceLocation loc = toResourceLocation(texture);

		try {
			ImageInputStream in = ImageIO.createImageInputStream(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream());
			Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					return new Vector2D(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} catch (Exception e) {
			throw new RenderException("Couldn't load texture " + texture.getPath(), e);
		}
		return super.getDimension(texture);
	}
}
