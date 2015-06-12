package nova.wrapper.mc18.wrapper.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import nova.core.render.RenderException;
import nova.core.render.RenderManager;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;

public class BWRenderManager extends RenderManager {

	public static ResourceLocation toResourceLocation(Texture texture) {
		return new ResourceLocation(texture.domain, texture.getPath());
	}

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
		return new Vector2D(16, 16);
	}
}
