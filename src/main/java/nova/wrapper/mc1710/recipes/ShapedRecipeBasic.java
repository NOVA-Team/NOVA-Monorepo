package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import nova.core.game.Game;
import nova.core.recipes.crafting.ShapedCraftingRecipe;

import java.util.Optional;

/**
 * @author Stan Hebben
 */
public class ShapedRecipeBasic extends ShapedRecipes {
	private final ShapedCraftingRecipe recipe;

	public ShapedRecipeBasic(ItemStack[] basicInputs, ShapedCraftingRecipe recipe) {
		super(recipe.getWidth(), recipe.getHeight(), basicInputs, recipe.getNominalOutput().isPresent() ? Game.natives().toNative(recipe.getNominalOutput().get()) : null);

		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		Optional<nova.core.item.Item> result = recipe.getCraftingResult(MCCraftingGrid.get(inventory));
		if (result.isPresent()) {
			return Game.natives().toNative(result.get());
		}
		return null;
	}
}
