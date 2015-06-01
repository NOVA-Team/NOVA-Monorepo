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

	private static Game instance;

	private final Logger logger;

	private final ClientManager clientManager;
	private final BlockManager blockManager;
	private final EntityManager entityManager;
	private final ItemManager itemManager;
	private final FluidManager fluidManager;
	private final WorldManager worldManager;
	private final RenderManager renderManager;
	private final RecipeManager recipeManager;
	private final CraftingRecipeManager craftingRecipeManager;
	private final ItemDictionary itemDictionary;
	private final GlobalEvents eventManager;
	private final NetworkManager networkManager;
	private final SaveManager saveManager;
	private final LanguageManager languageManager;
	private final KeyManager keyManager;
	private final ComponentManager componentManager;
	private final NativeManager nativeManager;

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 *
	 * This is @deprecated, use syncTicker() instead.
	 */
	private final UpdateTicker.SynchronizedTicker syncTicker;

	/**
	 * The thread ticker that runs on NOVA's thread.
	 *
	 * This is @deprecated, use threadTicker() instead.
	 */
	private final UpdateTicker.ThreadTicker threadTicker;

	private final GuiComponentFactory guiComponentFactory;
	private final GuiManager guiFactory;

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

	public static void inject(Game game) {
		Game.instance = game;
	}

	public static Logger logger() {
		return instance.logger;
	}

	public static ClientManager clientManager() {
		return instance.clientManager;
	}

	public static BlockManager blockManager() {
		return instance.blockManager;
	}

	public static EntityManager entityManager() {
		return instance.entityManager;
	}

	public static ItemManager itemManager() {
		return instance.itemManager;
	}

	public static FluidManager fluidManager() {
		return instance.fluidManager;
	}

	public static WorldManager worldManager() {
		return instance.worldManager;
	}

	public static RenderManager renderManager() {
		return instance.renderManager;
	}

	public static RecipeManager recipeManager() {
		return instance.recipeManager;
	}

	public static CraftingRecipeManager craftingRecipeManager() {
		return instance.craftingRecipeManager;
	}

	public static ItemDictionary itemDictionary() {
		return instance.itemDictionary;
	}

	public static GlobalEvents eventManager() {
		return instance.eventManager;
	}

	public static NetworkManager networkManager() {
		return instance.networkManager;
	}

	public static SaveManager saveManager() {
		return instance.saveManager;
	}

	public static LanguageManager languageManager() {
		return instance.languageManager;
	}

	public static KeyManager keyManager() {
		return instance.keyManager;
	}

	public static ComponentManager componentManager() {
		return instance.componentManager;
	}

	public static NativeManager nativeManager() {
		return instance.nativeManager;
	}

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 */
	public static UpdateTicker.SynchronizedTicker syncTicker() {
		return instance.syncTicker;
	}

	/**
	 * The thread ticker that runs on NOVA's thread.
	 */
	public static UpdateTicker.ThreadTicker threadTicker() {
		return instance.threadTicker;
	}

	public static GuiComponentFactory guiComponentFactory() {
		return instance.guiComponentFactory;
	}

	public static GuiManager guiFactory() {
		return instance.guiFactory;
	}
}
