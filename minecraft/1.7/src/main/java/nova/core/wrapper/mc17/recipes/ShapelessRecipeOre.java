package nova.core.wrapper.mc17.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nova.core.item.Item;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * @author Stan Hebben
 */
public class ShapelessRecipeOre extends ShapelessOreRecipe {
	private final ShapelessCraftingRecipe recipe;

	public ShapelessRecipeOre(Object[] ingredients, ShapelessCraftingRecipe recipe) {
		super((ItemStack) Game.natives().toNative(recipe.getNominalOutput().get()), ingredients);

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
			return ((ItemStack) Game.natives().toNative(craftingResult.get())).copy();
		}
		return null;
	}
}
