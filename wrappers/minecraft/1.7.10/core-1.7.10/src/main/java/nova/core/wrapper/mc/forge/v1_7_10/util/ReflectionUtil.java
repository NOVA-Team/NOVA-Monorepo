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

package nova.core.wrapper.mc.forge.v1_7_10.util;

import com.google.common.collect.BiMap;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import nova.internal.core.Game;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Common class for all runtime hacks (stuff requiring reflection). It is not
 * unexpected to have it break withPriority new minecraft versions and may need regular
 * adjustment - as such, those have been collected here.
 * @author Stan Hebben
 */
@SuppressWarnings("unchecked")
public class ReflectionUtil {
	private static final Field NBTTAGLIST_TAGLIST;
	private static final Field OREDICTIONARY_IDTOSTACK;
	private static final Field OREDICTIONARY_IDTOSTACKUN;
	private static final Field MINECRAFTSERVER_ANVILFILE;
	private static final Field SHAPEDORERECIPE_WIDTH;
	private static final Field INVENTORYCRAFTING_EVENTHANDLER;
	private static final Field SLOTCRAFTING_PLAYER;

	private static final Field ENTITYREGISTRY_CLASSREGISTRATIONS;
	private static final Field SEEDENTRY_SEED;
	private static final Constructor<? extends WeightedRandom.Item> SEEDENTRY_CONSTRUCTOR;

	static {
		NBTTAGLIST_TAGLIST = getField(NBTTagList.class, ObfuscationConstants.NBTTAGLIST_TAGLIST);
		OREDICTIONARY_IDTOSTACK = getField(OreDictionary.class, ObfuscationConstants.OREDICTIONARY_IDTOSTACK);
		OREDICTIONARY_IDTOSTACKUN = getField(OreDictionary.class, ObfuscationConstants.OREDICTIONARY_IDTOSTACKUN);
		MINECRAFTSERVER_ANVILFILE = getField(MinecraftServer.class, ObfuscationConstants.MINECRAFTSERVER_ANVILFILE);
		SHAPEDORERECIPE_WIDTH = getField(ShapedOreRecipe.class, new String[] { "width" });
		INVENTORYCRAFTING_EVENTHANDLER = getField(InventoryCrafting.class, ObfuscationConstants.INVENTORYCRAFTING_EVENTHANDLER);
		SLOTCRAFTING_PLAYER = getField(SlotCrafting.class, ObfuscationConstants.SLOTCRAFTING_PLAYER);

		ENTITYREGISTRY_CLASSREGISTRATIONS = getField(EntityRegistry.class, new String[] { "entityClassRegistrations" });

		Class<? extends WeightedRandom.Item> forgeSeedEntry = null;
		try {
			forgeSeedEntry = (Class<? extends WeightedRandom.Item>) Class.forName("net.minecraftforge.common.ForgeHooks$SeedEntry");
		} catch (ClassNotFoundException ex) {
		}

		SEEDENTRY_SEED = getField(forgeSeedEntry, "seed");

		Constructor<? extends WeightedRandom.Item> seedEntryConstructor = null;

		try {
			seedEntryConstructor = forgeSeedEntry.getConstructor(ItemStack.class, int.class);
			seedEntryConstructor.setAccessible(true);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
		}

		SEEDENTRY_CONSTRUCTOR = seedEntryConstructor;
	}

	private ReflectionUtil() {
	}

	public static BiMap<Class<? extends Entity>, EntityRegistry.EntityRegistration> getEntityClassRegistrations() {
		try {
			return (BiMap<Class<? extends Entity>, EntityRegistry.EntityRegistration>) ENTITYREGISTRY_CLASSREGISTRATIONS.get(EntityRegistry.instance());
		} catch (IllegalArgumentException ex) {
			return null;
		} catch (IllegalAccessException ex) {
			return null;
		}
	}

	public static List<NBTBase> getTagList(NBTTagList list) {
		if (NBTTAGLIST_TAGLIST == null) {
			return null;
		}
		try {
			return (List) NBTTAGLIST_TAGLIST.get(list);
		} catch (IllegalArgumentException ex) {
			return null;
		} catch (IllegalAccessException ex) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static List getSeeds() {
		return getPrivateStaticObject(ForgeHooks.class, "seedList");
	}

	public static Map<String, ChestGenHooks> getChestLoot() {
		return getPrivateStaticObject(ChestGenHooks.class, "chestInfo");
	}

	public static Map<String, String> getTranslations() {
		return getPrivateObject(
			getPrivateStaticObject(StatCollector.class, "localizedName", "field_74839_a"),
			"languageList",
			"field_74816_c");
	}

	public static List<ArrayList<ItemStack>> getOreIdStacks() {
		try {
			return (List<ArrayList<ItemStack>>) OREDICTIONARY_IDTOSTACK.get(null);
		} catch (IllegalAccessException ex) {
			Game.logger().error("ERROR - could not load ore dictionary stacks!");
			return null;
		}
	}

	public static List<ArrayList<ItemStack>> getOreIdStacksUn() {
		try {
			return (List<ArrayList<ItemStack>>) OREDICTIONARY_IDTOSTACKUN.get(null);
		} catch (IllegalAccessException ex) {
			Game.logger().error("ERROR - could not load ore dictionary stacks!");
			return null;
		}
	}

	public static File getAnvilFile(MinecraftServer server) {
		try {
			return (File) MINECRAFTSERVER_ANVILFILE.get(server);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not load anvil file!");
			return null;
		}
	}

	public static File getWorldDirectory(MinecraftServer server) {
		if (server.isDedicatedServer()) {
			return server.getFile("world");
		} else {
			File worldsDir = getAnvilFile(server);
			if (worldsDir == null) {
				return null;
			}

			return new File(worldsDir, server.getFolderName());
		}
	}

	public static int getShapedOreRecipeWidth(ShapedOreRecipe recipe) {
		try {
			return SHAPEDORERECIPE_WIDTH.getInt(recipe);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not load shaped ore recipe width!");
			return 3;
		}
	}

	public static Container getCraftingContainer(InventoryCrafting inventory) {
		try {
			return (Container) INVENTORYCRAFTING_EVENTHANDLER.get(inventory);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not get inventory eventhandler");
			return null;
		}
	}

	public static EntityPlayer getCraftingSlotPlayer(SlotCrafting slot) {
		try {
			return (EntityPlayer) SLOTCRAFTING_PLAYER.get(slot);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not get inventory eventhandler");
			return null;
		}
	}

	public static StringTranslate getStringTranslateInstance() {
		try {
			Field field = getField(StringTranslate.class, ObfuscationConstants.STRINGTRANSLATE_INSTANCE);
			return (StringTranslate) field.get(null);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not get string translator");
			return null;
		}
	}

	public static ItemStack getSeedEntrySeed(Object entry) {
		try {
			return (ItemStack) SEEDENTRY_SEED.get(entry);
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not get SeedEntry seed");
			return null;
		}
	}

	public static void setCraftingRecipeList(List<IRecipe> craftingRecipeList) {
		if (!setPrivateObject(
			CraftingManager.getInstance(),
			craftingRecipeList,
			ObfuscationConstants.CRAFTINGMANAGER_RECIPES)) {
			Game.logger().error("could not set crafting recipe list");
		}
	}

	/*public static WeightedRandom.Item constructSeedEntry(WeightedItemStack stack) {
		try {
			return SEEDENTRY_CONSTRUCTOR.newInstance(MineTweakerMC.getItemStack(stack.getStack()), (int) stack.getChance());
		} catch (InstantiationException ex) {
			Game.logger().error("could not construct SeedEntry");
		} catch (IllegalAccessException ex) {
			Game.logger().error("could not construct SeedEntry");
		} catch (IllegalArgumentException ex) {
			Game.logger().error("could not construct SeedEntry");
		} catch (InvocationTargetException ex) {
			Game.logger().error("could not construct SeedEntry");
		}

		return null;
	}*/

	public static <T> T getPrivateStaticObject(Class<?> cls, String... names) {
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return (T) field.get(null);
			} catch (NoSuchFieldException ex) {

			} catch (SecurityException ex) {

			} catch (IllegalAccessException ex) {

			}
		}

		return null;
	}

	public static <T> T getPrivateObject(Object object, String... names) {
		Class<?> cls = object.getClass();
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return (T) field.get(object);
			} catch (Exception ex) {

			}
		}

		return null;
	}

	public static boolean setPrivateObject(Object object, Object value, String... names) {
		Class<?> cls = object.getClass();
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				field.set(object, value);
				return true;
			} catch (Exception ex) {

			}
		}

		return false;
	}

	// #######################
	// ### Private Methods ###
	// #######################

	private static Field getField(Class<?> cls, String... names) {
		for (String name : names) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException ex) {
			} catch (SecurityException ex) {
			}
		}

		return null;
	}
}
