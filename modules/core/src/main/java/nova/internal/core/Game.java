/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.internal.core;

import nova.core.block.BlockManager;
import nova.core.component.ComponentManager;
import nova.core.component.fluid.FluidManager;
import nova.core.entity.EntityManager;
import nova.core.event.bus.GlobalEvents;
import nova.core.game.ClientManager;
import nova.core.game.GameInfo;
import nova.core.game.InputManager;
import nova.core.item.ItemDictionary;
import nova.core.item.ItemManager;
import nova.core.nativewrapper.NativeManager;
import nova.core.network.NetworkManager;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipeManager;
import nova.core.render.RenderManager;
import nova.core.language.LanguageManager;
import nova.core.util.registry.RetentionManager;
import nova.core.world.WorldManager;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.di.LoggerModule;
import nova.internal.core.tick.UpdateTicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jbee.inject.Injector;

import java.util.Optional;

public class Game {

	private static Game instance;
	private static Injector injector;

	private final Logger logger;

	private final GameInfo gameInfo;
	private final ClientManager clientManager;
	private final BlockManager blockManager;
	private final EntityManager entityManager;
	private final ItemManager itemManager;
	private final WorldManager worldManager;
	private final RenderManager renderManager;
	private final RecipeManager recipeManager;
	private final CraftingRecipeManager craftingRecipeManager;
	private final ItemDictionary itemDictionary;
	private final GlobalEvents eventManager;
	private final NetworkManager networkManager;
	private final RetentionManager retentionManager;
	private final FluidManager fluidManager;
	private final LanguageManager languageManager;
	private final InputManager inputManager;
	private final ComponentManager componentManager;
	private final NativeManager nativeManager;

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 */
	private final UpdateTicker.SynchronizedTicker syncTicker;

	/**
	 * The thread ticker that runs on NOVA's thread.
	 */
	private final UpdateTicker.ThreadTicker threadTicker;

	private Game(
		Logger logger,
		GameInfo gameInfo,
		ClientManager clientManager,
		BlockManager blockManager,
		EntityManager entityManager,
		ItemManager itemManager,
		WorldManager worldManager,
		RenderManager renderManager,
		RecipeManager recipeManager,
		CraftingRecipeManager craftingRecipeManager,
		ItemDictionary itemDictionary,
		GlobalEvents eventManager,
		FluidManager fluidManager,
		NetworkManager networkManager,
		RetentionManager retentionManager,
		LanguageManager languageManager,
		InputManager inputManager,
		NativeManager nativeManager,
		ComponentManager componentManager,
		UpdateTicker.SynchronizedTicker syncTicker,
		UpdateTicker.ThreadTicker threadTicker) {

		this.logger = logger;

		this.gameInfo = gameInfo;
		this.clientManager = clientManager;
		this.blockManager = blockManager;
		this.entityManager = entityManager;
		this.itemManager = itemManager;
		this.worldManager = worldManager;
		this.renderManager = renderManager;
		this.recipeManager = recipeManager;
		this.craftingRecipeManager = craftingRecipeManager;
		this.itemDictionary = itemDictionary;
		this.eventManager = eventManager;
		this.networkManager = networkManager;
		this.fluidManager = fluidManager;
		this.retentionManager = retentionManager;
		this.languageManager = languageManager;
		this.inputManager = inputManager;
		this.nativeManager = nativeManager;
		this.componentManager = componentManager;

		this.syncTicker = syncTicker;
		this.threadTicker = threadTicker;

		logger.info("Game instance created.");
	}

	public static void inject(DependencyInjectionEntryPoint diep) {
		instance = diep.init();
		injector = diep.getInjector().get();
	}

	public static GameInfo info() {
		return instance.gameInfo;
	}

	public static Injector injector() {
		return injector;
	}

	public static Logger logger() {
		return Optional.ofNullable(instance).map(instance -> instance.logger).orElseGet(() -> LoggerFactory.getLogger(LoggerModule.getDefaultName()));
	}

	public static ClientManager clientManager() {
		return instance.clientManager;
	}

	public static BlockManager blocks() {
		return instance.blockManager;
	}

	public static EntityManager entities() {
		return instance.entityManager;
	}

	public static ItemManager items() {
		return instance.itemManager;
	}

	public static WorldManager worlds() {
		return instance.worldManager;
	}

	public static RenderManager render() {
		return instance.renderManager;
	}

	public static RecipeManager recipes() {
		return instance.recipeManager;
	}

	public static CraftingRecipeManager craftingRecipes() {
		return instance.craftingRecipeManager;
	}

	public static ItemDictionary itemDictionary() {
		return instance.itemDictionary;
	}

	public static GlobalEvents events() {
		return instance.eventManager;
	}

	public static NetworkManager network() {
		return instance.networkManager;
	}

	public static RetentionManager retention() {
		return instance.retentionManager;
	}

	public static LanguageManager language() {
		return instance.languageManager;
	}

	public static InputManager input() {
		return instance.inputManager;
	}

	public static ComponentManager components() {
		return instance.componentManager;
	}

	public static NativeManager natives() {
		return instance.nativeManager;
	}

	public static FluidManager fluids() {
		return instance.fluidManager;
	}

	/**
	 * The synchronized ticker that uses the same thread as the game.
	 *
	 * @return The synchronous game ticker, which is synchronized
	 * with the game’s tick speed.
	 * <p>
	 * For Minecraft, this is 20hz.
	 */
	public static UpdateTicker.SynchronizedTicker syncTicker() {
		return instance.syncTicker;
	}

	/**
	 * The thread ticker that runs on NOVA's thread.
	 *
	 * @return The asynchronous NOVA ticker,
	 * which runs as fast as possible.
	 */
	public static UpdateTicker.ThreadTicker threadTicker() {
		return instance.threadTicker;
	}
}
