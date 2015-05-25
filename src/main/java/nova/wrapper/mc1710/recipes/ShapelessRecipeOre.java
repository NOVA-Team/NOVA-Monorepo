package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nova.core.game.Game;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;

/**
 * @author Stan Hebben
 */
public class ShapelessRecipeOre extends ShapelessOreRecipe {
	private final ShapelessCraftingRecipe recipe;

	public ShapelessRecipeOre(Object[] ingredients, ShapelessCraftingRecipe recipe) {
		super((ItemStack) Game.instance.nativeManager.toNative(recipe.getNominalOutput().get()), ingredients);

		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return ((ItemStack) Game.instance.nativeManager.toNative(recipe.getCraftingResult(MCCraftingGrid.get(inventory)))).copy();
	}
}
