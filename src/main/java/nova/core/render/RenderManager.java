package nova.core.render;

import nova.core.render.model.ModelProvider;
import nova.core.render.texture.BlockTexture;
import nova.core.render.texture.EntityTexture;
import nova.core.render.texture.ItemTexture;
import nova.core.render.texture.Texture;
import nova.core.util.Registry;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Optional;

/**
 * @author Calclavia
 */
public abstract class RenderManager {

	public final Registry<BlockTexture> blockTextures = new Registry<>();
	public final Registry<ItemTexture> itemTextures = new Registry<>();
	public final Registry<EntityTexture> entityTextures = new Registry<>();
	public final Registry<ModelProvider> modelProviders = new Registry<>();

	public ItemTexture registerTexture(ItemTexture texture) {
		Optional<ItemTexture> itemTexture = itemTextures.get(texture.getID());
		if (itemTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return itemTexture.get();
		}
		itemTextures.register(texture);
		return texture;
	}

	public BlockTexture registerTexture(BlockTexture texture) {
		Optional<BlockTexture> blockTexture = blockTextures.get(texture.getID());
		if (blockTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return blockTexture.get();
		}
		blockTextures.register(texture);
		return texture;
	}

	public EntityTexture registerTexture(EntityTexture texture) {
		Optional<EntityTexture> entityTexture = entityTextures.get(texture.getID());
		if (entityTexture.isPresent()) {
			Game.logger().error("Attempt to register the same texture twice: " + texture);
			return entityTexture.get();
		}
		entityTextures.register(texture);
		return texture;
	}

	public ModelProvider registerModel(ModelProvider modelProvider) {
		Optional<ModelProvider> modelProviderCheck = modelProviders.get(modelProvider.getID());
		if (modelProviderCheck.isPresent()) {
			Game.logger().error("Attempt to register the same model twice: " + modelProvider);
			return modelProviderCheck.get();
		}
		modelProviders.register(modelProvider);
		return modelProvider;
	}

	@Deprecated
	public abstract Vector2D getDimension(Texture texture);
}
