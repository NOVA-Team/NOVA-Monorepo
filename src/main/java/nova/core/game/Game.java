package nova.core.game;

import nova.core.block.BlockManager;
import nova.core.component.ComponentManager;
import nova.core.entity.EntityManager;
import nova.core.event.GlobalEvents;
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
	@Deprecated
	public static Game instance;

	@Deprecated
	public final Logger logger;

	@Deprecated
	public final ClientManager clientManager;
	@Deprecated
	public final BlockManager blockManager;
	@Deprecated
	public final EntityManager entityManager;
	@Deprecated
	public final ItemManager itemManager;
	@Deprecated
	public final FluidManager fluidManager;
	@Deprecated
	public final WorldManager worldManager;
	@Deprecated
	public final RenderManager renderManager;
	@Deprecated
	public final RecipeManager recipeManager;
	@Deprecated
	public final CraftingRecipeManager craftingRecipeManager;
	@Deprecated
	public final ItemDictionary itemDictionary;
	@Deprecated
	public final GlobalEvents eventManager;
	@Deprecated
	public final NetworkManager networkManager;
	@Deprecated
	public final SaveManager saveManager;
	@Deprecated
	public final LanguageManager languageManager;
	@Deprecated
	public final KeyManager keyManager;
	@Deprecated
	public final ComponentManager componentManager;
	@Deprecated
	public final NativeManager nativeManager;

	@Deprecated
	/**
	 * The synchronized ticker that uses the same thread as the game.
	 *
	 * This is @deprecated, use syncTicker() instead.
	 */
	public final UpdateTicker.SynchronizedTicker syncTicker;

	@Deprecated
	/**
	 * The thread ticker that runs on NOVA's thread.
	 *
	 * This is @deprecated, use threadTicker() instead.
	 */
	public final UpdateTicker.ThreadTicker threadTicker;

	// TODO Move somewhere else
	@Deprecated
	public final GuiComponentFactory guiComponentFactory;
	@Deprecated
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
		GlobalEvents eventManager,
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

	public static Game instance() {
		return instance;
	}

	public static void inject(Game game) {
		Game.instance = game;
	}

	public Logger logger() {
		return logger;
	}

	public ClientManager clientManager() {
		return clientManager;
	}

	public BlockManager blockManager() {
		return blockManager;
	}

	public EntityManager entityManager() {
		return entityManager;
	}

	public ItemManager itemManager() {
		return itemManager;
	}

	public FluidManager fluidManager() {
		return fluidManager;
	}

	public WorldManager worldManager() {
		return worldManager;
	}

	public RenderManager renderManager() {
		return renderManager;
	}

	public RecipeManager recipeManager() {
		return recipeManager;
	}

	public CraftingRecipeManager craftingRecipeManager() {
		return craftingRecipeManager;
	}

	public ItemDictionary itemDictionary() {
		return itemDictionary;
	}

	public GlobalEvents eventManager() {
		return eventManager;
	}

	public NetworkManager networkManager() {
		return networkManager;
	}

	public SaveManager saveManager() {
		return saveManager;
	}

	public LanguageManager languageManager() {
		return languageManager;
	}

	public KeyManager keyManager() {
		return keyManager;
	}

	public ComponentManager componentManager() {
		return componentManager;
	}

	public NativeManager nativeManager() {
		return nativeManager;
	}

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 */
	public UpdateTicker.SynchronizedTicker syncTicker() {
		return syncTicker;
	}

	/**
	 * The thread ticker that runs on NOVA's thread.
	 */
	public final UpdateTicker.ThreadTicker threadTicker() {
		return threadTicker;
	}

	public final GuiComponentFactory guiComponentFactory() {
		return guiComponentFactory;
	}

	public final GuiManager guiFactory() {
		return guiFactory;
	}
}
