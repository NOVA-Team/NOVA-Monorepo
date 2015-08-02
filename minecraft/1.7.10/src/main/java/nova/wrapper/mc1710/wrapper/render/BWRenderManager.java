package nova.wrapper.mc1710.wrapper.render;

import net.minecraft.util.ResourceLocation;
import nova.core.render.RenderManager;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class BWRenderManager extends RenderManager {

	public static ResourceLocation toResourceLocation(Texture texture) {
		return new ResourceLocation(texture.domain, texture.getPath());
	}

	@Override
	public Vector2D getDimension(Texture texture) {
		return new Vector2D(16, 16);
	}
}
