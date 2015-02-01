package nova.core.render;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Calclavia
 */
public class RenderManager {

	public final Set<BlockTexture> blockTextures = new HashSet<>();
	public final Set<ItemTexture> itemTextures = new HashSet<>();

	public ItemTexture registerTexture(ItemTexture texture) {
		itemTextures.add(texture);
		return texture;
	}
	public BlockTexture registerTexture(BlockTexture texture) {
		blockTextures.add(texture);
		return texture;
	}
}
