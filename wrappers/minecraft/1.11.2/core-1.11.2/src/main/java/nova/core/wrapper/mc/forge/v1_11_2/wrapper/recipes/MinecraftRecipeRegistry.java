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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import nova.core.event.RecipeEvent;
import nova.core.item.Item;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.core.recipes.ingredient.ItemIngredient;
import nova.core.recipes.smelting.SmeltingRecipe;
import nova.core.wrapper.mc.forge.v1_11_2.util.ReflectionUtil;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.NovaCraftingRecipe;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.ShapedRecipeBasic;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.ShapedRecipeOre;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.ShapelessRecipeBasic;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.ShapelessRecipeOre;
import nova.internal.core.Game;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Stan on 1/02/2015.
 */
public class MinecraftRecipeRegistry {
	public static final MinecraftRecipeRegistry instance = new MinecraftRecipeRegistry();

	private final Map<CraftingRecipe, IRecipe> forwardWrappers = new HashMap<>();
	private final Map<IRecipe, CraftingRecipe> backwardWrappers = new HashMap<>();

	private MinecraftRecipeRegistry() {
	}

	public void registerRecipes() {
		long startTime = System.nanoTime();

		RecipeManager recipeManager = Game.recipes();

		@SuppressWarnings("unchecked")
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			CraftingRecipe converted = convert(recipe);
			if (converted != null) {
				recipeManager.addRecipe(converted);
				backwardWrappers.put(recipe, converted);
				forwardWrappers.put(converted, recipe);
			}
		}

		ReflectionUtil.setCraftingRecipeList(new RecipeListWrapper(recipes));

		Game.logger().info("Initialized recipes in {} ms", (System.nanoTime() - startTime) / 1_000_000);

		RecipeSorter.register("nova:shaped", ShapedRecipeBasic.class, Category.SHAPED, "before:forge:shapedore");
		RecipeSorter.register("nova:shaped.oredict", ShapedRecipeOre.class, Category.SHAPED, "after:nova:shaped after:minecraft:shaped before:minecraft:shapeless");

		RecipeSorter.register("nova:shapeless", ShapelessRecipeBasic.class, Category.SHAPELESS, "after:minecraft:shapeless before:forge:shapelessore");
		RecipeSorter.register("nova:shapeless.oredict", ShapelessRecipeOre.class, Category.SHAPELESS, "after:nova:shapeless after:minecraft:shapeless");

		RecipeSorter.register("nova:unknown", NovaCraftingRecipe.class, Category.UNKNOWN, "");

		recipeManager.whenRecipeAdded(CraftingRecipe.class, this::onNOVARecipeAdded);
		recipeManager.whenRecipeRemoved(CraftingRecipe.class, this::onNOVARecipeRemoved);

		recipeManager.whenRecipeAdded(SmeltingRecipe.class, this::onNOVASmeltingAdded);
		recipeManager.whenRecipeRemoved(SmeltingRecipe.class, this::onNOVASmeltingRemoved);
	}

	private CraftingRecipe convert(IRecipe recipe) {
		return RecipeConverter.instance().toNova(recipe);
	}

	private IRecipe convert(CraftingRecipe recipe) {
		return RecipeConverter.instance().toNative(recipe);
	}

	@SuppressWarnings("unchecked")
	private void onNOVARecipeAdded(RecipeEvent.Add<CraftingRecipe> evt) {
		CraftingRecipe recipe = evt.recipe;
		if (forwardWrappers.containsKey(recipe)) {
			return;
		}

		IRecipe minecraftRecipe = convert(recipe);

		backwardWrappers.put(minecraftRecipe, recipe);
		forwardWrappers.put(recipe, minecraftRecipe);

		CraftingManager.getInstance().getRecipeList().add(minecraftRecipe);
	}

	private void onNOVARecipeRemoved(RecipeEvent.Remove<CraftingRecipe> evt) {
		IRecipe minecraftRecipe = forwardWrappers.get(evt.recipe);

		forwardWrappers.remove(evt.recipe);
		backwardWrappers.remove(minecraftRecipe);

		CraftingManager.getInstance().getRecipeList().remove(minecraftRecipe);
	}

	private void onMinecraftRecipeAdded(IRecipe recipe) {
		if (backwardWrappers.containsKey(recipe)) {
			return;
		}

		CraftingRecipe novaRecipe = convert(recipe);

		backwardWrappers.put(recipe, novaRecipe);
		forwardWrappers.put(novaRecipe, recipe);

		Game.recipes().addRecipe(novaRecipe);
	}

	private void onMinecraftRecipeRemoved(IRecipe recipe) {
		CraftingRecipe novaRecipe = backwardWrappers.get(recipe);

		forwardWrappers.remove(novaRecipe);
		backwardWrappers.remove(recipe);

		Game.recipes().removeRecipe(novaRecipe);
	}

	private void onNOVASmeltingAdded(RecipeEvent.Add<SmeltingRecipe> evt) {
		SmeltingRecipe recipe = evt.recipe;

		Collection<Item> inputs = recipe.getInput().map(ItemIngredient::getExampleItems).orElse(Collections.emptyList());

		final Optional<ItemStack> output = recipe.getExampleOutput().map(ItemConverter.instance()::toNative);
		if (!output.isPresent())
			return;

		inputs.stream().map(ItemConverter.instance()::toNative).forEach(input -> FurnaceRecipes.instance().addSmeltingRecipe(input, output.get(), 0));
	}

	private void onNOVASmeltingRemoved(RecipeEvent.Remove<SmeltingRecipe> evt) {
		SmeltingRecipe recipe = evt.recipe;

		Collection<Item> inputs = recipe.getInput().map(ItemIngredient::getExampleItems).orElse(Collections.emptyList());
		@SuppressWarnings("unchecked")
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		inputs.stream().map(ItemConverter.instance()::toNative).forEach(input -> smeltingList.remove(input));
	}

	private class RecipeListWrapper extends AbstractList<IRecipe> {
		private final List<IRecipe> original;

		public RecipeListWrapper(List<IRecipe> original) {
			this.original = original;
		}

		@Override
		public int size() {
			return original.size();
		}

		@Override
		public boolean isEmpty() {
			return original.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return original.contains(o);
		}

		@Override
		public Iterator<IRecipe> iterator() {
			return original.iterator();
		}

		@Override
		public void forEach(Consumer<? super IRecipe> action) {
			original.forEach(action);
		}

		@Override
		public Object[] toArray() {
			return original.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return original.toArray(a);
		}

		@Override
		public boolean add(IRecipe iRecipe) {
			boolean result = original.add(iRecipe);
			if (result && !backwardWrappers.containsKey(iRecipe)) {
				onMinecraftRecipeAdded(iRecipe);
			}

			return result;
		}

		@Override
		public boolean remove(Object o) {
			if (!backwardWrappers.containsKey(o)) {
				return false;
			}

			boolean result = original.remove(o);
			if (result) {
				onMinecraftRecipeRemoved((IRecipe) o);
			}

			return result;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return original.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends IRecipe> c) {
			boolean result = false;
			for (IRecipe recipe : c) {
				result |= add(recipe);
			}
			return result;
		}

		@Override
		public boolean addAll(int index, Collection<? extends IRecipe> c) {
			for (IRecipe recipe : c) {
				add(index, recipe);
				index++;
			}

			return !c.isEmpty();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean result = false;
			for (Object o : c) {
				result |= c.remove(o);
			}
			return result;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		@Override
		public void sort(Comparator<? super IRecipe> c) {
			original.sort(c);
		}

		@Override
		public void clear() {
			// one would have to be a mad scientist to use this operation... but ok

			for (IRecipe recipe : original) {
				onMinecraftRecipeRemoved(recipe);
			}

			original.clear();
		}

		@Override
		public IRecipe get(int index) {
			return original.get(index);
		}

		@Override
		public IRecipe set(int index, IRecipe element) {
			IRecipe current = original.get(index);
			onMinecraftRecipeRemoved(current);

			original.set(index, element);
			onMinecraftRecipeAdded(element);

			return current;
		}

		@Override
		public void add(int index, IRecipe element) {
			original.add(index, element);
			onMinecraftRecipeAdded(element);
		}

		@Override
		public IRecipe remove(int index) {
			IRecipe current = original.remove(index);
			onMinecraftRecipeRemoved(current);
			return current;
		}

		@Override
		public int indexOf(Object o) {
			return original.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return original.lastIndexOf(o);
		}

		@Override
		public ListIterator<IRecipe> listIterator() {
			return original.listIterator();
		}

		@Override
		public ListIterator<IRecipe> listIterator(int index) {
			return original.listIterator(index);
		}

		@Override
		public List<IRecipe> subList(int fromIndex, int toIndex) {
			return new RecipeListWrapper(original.subList(fromIndex, toIndex));
		}

		@Override
		public Spliterator<IRecipe> spliterator() {
			return original.spliterator();
		}

		@Override
		public Stream<IRecipe> stream() {
			return original.stream();
		}

		@Override
		public Stream<IRecipe> parallelStream() {
			return original.parallelStream();
		}
	}
}
