package nova.core.game;

import nova.core.block.BlockManager;
import nova.core.component.ComponentManager;
import nova.core.entity.EntityManager;
import nova.core.event.EventManager;
import nova.core.fluid.FluidManager;
import nova.core.gui.KeyManager;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiManager;
import nova.core.item.ItemDictionary;
import nova.core.item.ItemManager;
import nova.core.nativewrapper.NativeManager;
import nova.core.network.NetworkManager;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipeManager;
import nova.core.render.RenderManager;
import nova.core.util.LanguageManager;
import nova.core.util.SaveManager;
import nova.core.world.WorldManager;
import nova.internal.tick.UpdateTicker;
import org.slf4j.Logger;

public class Game {
	public static Game instance;

	public final Logger logger;

	public final ClientManager clientManager;
	public final BlockManager blockManager;
	public final EntityManager entityManager;
	public final ItemManager itemManager;
	public final FluidManager fluidManager;
	public final WorldManager worldManager;
	public final RenderManager renderManager;
	public final RecipeManager recipeManager;
	public final CraftingRecipeManager craftingRecipeManager;
	public final ItemDictionary itemDictionary;
	public final EventManager eventManager;
	public final NetworkManager networkManager;
	public final SaveManager saveManager;
	public final LanguageManager languageManager;
	public final KeyManager keyManager;
	public final ComponentManager componentManager;
	public final NativeManager nativeManager;

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 */
	public final UpdateTicker.SynchronizedTicker syncTicker;
	/**
	 * The thread ticker that runs on NOVA's thread.
	 */
	public final UpdateTicker.ThreadTicker threadTicker;

	// TODO Move somewhere else
	public final GuiComponentFactory guiComponentFactory;
	public final GuiManager guiFactory;

	private Game(
		Logger logger,
		ClientManager clientManager,
		BlockManager blockManager,
		EntityManager entityManager,
		ItemManager itemManager,
		FluidManager fluidManager,
		WorldManager worldManager,
		RenderManager renderManager,
		RecipeManager recipeManager,
		CraftingRecipeManager craftingRecipeManager,
		ItemDictionary itemDictionary,
		EventManager eventManager,
		NetworkManager networkManager,
		SaveManager saveManager,
		LanguageManager languageManager,
		KeyManager keyManager,
		NativeManager nativeManager,
		ComponentManager componentManager,
		UpdateTicker.SynchronizedTicker syncTicker,
		UpdateTicker.ThreadTicker threadTicker,
		GuiComponentFactory guiComponentFactory, GuiManager guiFactory) {

		this.logger = logger;

		this.clientManager = clientManager;
		this.blockManager = blockManager;
		this.entityManager = entityManager;
		this.itemManager = itemManager;
		this.fluidManager = fluidManager;
		this.worldManager = worldManager;
		this.renderManager = renderManager;
		this.recipeManager = recipeManager;
		this.craftingRecipeManager = craftingRecipeManager;
		this.itemDictionary = itemDictionary;
		this.eventManager = eventManager;
		this.networkManager = networkManager;
		this.saveManager = saveManager;
		this.languageManager = languageManager;
		this.keyManager = keyManager;
		this.nativeManager = nativeManager;
		this.componentManager = componentManager;

		this.syncTicker = syncTicker;
		this.threadTicker = threadTicker;

		this.guiComponentFactory = guiComponentFactory;
		this.guiFactory = guiFactory;

		logger.info("Game instance created.");
	}

	public static Game getInstance() {
		return instance;
	}

	public Logger getLogger() {
		return logger;
	}

	public ClientManager getClientManager() {
		return clientManager;
	}

	public BlockManager getBlockManager() {
		return blockManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public FluidManager getFluidManager() {
		return fluidManager;
	}

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public RenderManager getRenderManager() {
		return renderManager;
	}

	public RecipeManager getRecipeManager() {
		return recipeManager;
	}

	public CraftingRecipeManager getCraftingRecipeManager() {
		return craftingRecipeManager;
	}

	public ItemDictionary getItemDictionary() {
		return itemDictionary;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public SaveManager getSaveManager() {
		return saveManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public ComponentManager getComponentManager() {
		return componentManager;
	}

	public NativeManager getNativeManager() {
		return nativeManager;
	}

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 */
	public UpdateTicker.SynchronizedTicker getSyncTicker() {
		return syncTicker;
	}

	/**
	 * The thread ticker that runs on NOVA's thread.
	 */
	public final UpdateTicker.ThreadTicker getThreadTicker() {
		return threadTicker;
	}
}
