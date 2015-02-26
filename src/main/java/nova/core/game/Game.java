package nova.core.game;

import nova.core.block.BlockManager;
import nova.core.entity.EntityManager;
import nova.core.event.EventManager;
import nova.core.fluid.FluidManager;
import nova.core.gui.KeyManager;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiFactory;
import nova.core.item.ItemDictionary;
import nova.core.item.ItemManager;
import nova.core.network.NetworkManager;
import nova.core.nativewrapper.NativeManager;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipeManager;
import nova.core.render.RenderManager;
import nova.core.util.LanguageManager;
import nova.core.util.SaveManager;
import nova.core.world.WorldManager;
import nova.internal.tick.UpdateTicker;

import org.slf4j.Logger;

public class Game {

	/**
	 * Use only when injection is not a solution. For example when performance
	 * is a concern. Treat as -100 style points. Must be initialized by code
	 * handling launch and dependency injection entry point.
	 */
	public static Game instance;

	public final Logger logger;

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
	public final GuiFactory guiFactory;

	private Game(
		Logger logger,
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
		UpdateTicker.SynchronizedTicker syncTicker,
		UpdateTicker.ThreadTicker threadTicker,
 GuiComponentFactory guiComponentFactory, GuiFactory guiFactory) {

		this.logger = logger;

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

		this.syncTicker = syncTicker;
		this.threadTicker = threadTicker;

		this.guiComponentFactory = guiComponentFactory;
		this.guiFactory = guiFactory;

		logger.info("Game instance created.");
	}
}
