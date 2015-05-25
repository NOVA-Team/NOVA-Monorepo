/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nova.wrapper.mc1710.recipes;

import java.util.Arrays;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;
import nova.wrapper.mc1710.util.WrapUtility;

/**
 *
 * @author Stan
 */
public class ShapelessRecipeBasic extends ShapelessRecipes {
	private final ShapelessCraftingRecipe recipe;
	
	public ShapelessRecipeBasic(ItemStack[] ingredients, ShapelessCraftingRecipe recipe) {
		super(WrapUtility.wrapItemStack(recipe.getNominalOutput()), Arrays.asList(ingredients));
		
		this.recipe = recipe;
	}
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return WrapUtility.wrapItemStack(recipe.getCraftingResult(MCCraftingGrid.get(inventory)));
	}
}
