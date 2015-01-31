package nova.core.render;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Calclavia
 */
public class RenderManager {

	public final Set<BlockTexture> blockTextures = new HashSet<>();

	public BlockTexture registerTexture(BlockTexture texture) {
		blockTextures.add(texture);
		return texture;
	}
}
