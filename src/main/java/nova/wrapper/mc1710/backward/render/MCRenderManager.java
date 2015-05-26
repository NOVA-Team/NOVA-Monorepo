package nova.wrapper.mc1710.backward.render;

import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import nova.core.render.RenderManager;
import nova.core.render.texture.Texture;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.vector.Vector2i;

public class MCRenderManager extends RenderManager {

	@Override
	public Vector2i getDimension(Texture texture) {
		ResourceLocation loc = toResourceLocation(texture);

		try {
			ImageInputStream in = ImageIO.createImageInputStream(Minecraft.getMinecraft().mcResourceManager.getResource(loc).getInputStream());
			Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					return new Vector2i(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} catch (Exception e) {
			throw new NovaException("Couldn't load texture " + texture.getResource(), e);
		}
		throw new NullPointerException();
	}

	public static ResourceLocation toResourceLocation(Texture texture) {
		return new ResourceLocation(texture.domain, texture.getPath());
	}
}
