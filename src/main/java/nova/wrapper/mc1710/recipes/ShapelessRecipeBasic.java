/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import nova.internal.Game;
import nova.core.item.Item;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Stan
 */
public class ShapelessRecipeBasic extends ShapelessRecipes {
	private final ShapelessCraftingRecipe recipe;

	public ShapelessRecipeBasic(ItemStack[] ingredients, ShapelessCraftingRecipe recipe) {
		super(recipe.getNominalOutput().isPresent() ? Game.natives().toNative(recipe.getNominalOutput().get()) : null, Arrays.asList(ingredients));

		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		Optional<Item> craftingResult = recipe.getCraftingResult(MCCraftingGrid.get(inventory));
		if (craftingResult.isPresent()) {
			return Game.natives().toNative(craftingResult.get());
		}
		return null;
	}
}
