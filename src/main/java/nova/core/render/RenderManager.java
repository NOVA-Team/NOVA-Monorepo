package nova.core.render;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Calclavia
 */
public class RenderManager {

	public final Set<BlockTexture> textures = new HashSet<>();

	public BlockTexture registerTexture(BlockTexture texture) {
		textures.add(texture);
		return texture;
	}
}
