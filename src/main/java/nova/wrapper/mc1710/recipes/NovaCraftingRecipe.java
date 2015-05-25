package nova.wrapper.mc1710.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import nova.core.game.Game;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.wrapper.mc1710.util.WrapUtility;

public class NovaCraftingRecipe implements IRecipe {
    private final CraftingRecipe recipe;

    public NovaCraftingRecipe(CraftingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return recipe.matches(MCCraftingGrid.get(inventoryCrafting));
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		return Game.instance.nativeManager.toNative(recipe.getCraftingResult(MCCraftingGrid.get(inventoryCrafting)));
	}

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
		return Game.instance.nativeManager.toNative(recipe.getNominalOutput());
	}
}
