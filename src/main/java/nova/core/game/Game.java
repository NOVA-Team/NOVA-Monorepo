package nova.core.game;

import nova.core.block.BlockManager;
import nova.core.entity.EntityManager;
import nova.core.event.EventManager;
import nova.core.fluid.FluidManager;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiFactory;
import nova.core.item.ItemDictionary;
import nova.core.item.ItemManager;
import nova.core.network.NetworkManager;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipeManager;
import nova.core.render.RenderManager;
import nova.core.world.WorldManager;

import java.util.Optional;

public class Game {

	/**
	 * Use only when injection is not a solution. For example when performance
	 * is a concern. Treat as -100 style points. Must be initialized by code
	 * handling launch and dependency injection entry point.
	 */
	public static Optional<Game> instance = Optional.empty();

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

	// TODO Move somewhere else, also... Optional inconvenient here, it has to exist as required.
	public final Optional<GuiComponentFactory> guiComponentFactory;
	public final Optional<GuiFactory> guiFactory;

	private Game(
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
		Optional<GuiComponentFactory> guiComponentFactory,
		Optional<GuiFactory> guiFactory) {

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

		this.guiComponentFactory = guiComponentFactory;
		this.guiFactory = guiFactory;
	}
}
