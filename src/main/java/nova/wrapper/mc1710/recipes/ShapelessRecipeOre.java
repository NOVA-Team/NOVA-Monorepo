package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;
import nova.wrapper.mc1710.util.WrapUtility;

/**
 *
 * @author Stan Hebben
 */
public class ShapelessRecipeOre extends ShapelessOreRecipe {
	private final ShapelessCraftingRecipe recipe;
	
	public ShapelessRecipeOre(Object[] ingredients, ShapelessCraftingRecipe recipe) {
		super(WrapUtility.wrapItemStack(recipe.getNominalOutput()), ingredients);
		
		this.recipe = recipe;
	}
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return WrapUtility.wrapItemStack(recipe.getCraftingResult(MCCraftingGrid.get(inventory))).copy();
	}
}
