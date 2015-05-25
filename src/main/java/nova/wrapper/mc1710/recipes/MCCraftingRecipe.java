package nova.wrapper.mc1710.recipes;

import net.minecraft.item.crafting.IRecipe;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.wrapper.mc1710.util.WrapUtility;

import java.util.Optional;

/**
 * @author Stan Hebben
 */
public class MCCraftingRecipe implements CraftingRecipe {
    private final IRecipe recipe;

    public MCCraftingRecipe(IRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean matches(CraftingGrid craftingGrid) {
        // TODO: supply world somehow?
        return recipe.matches(new NovaCraftingGrid(craftingGrid), null);
    }

    @Override
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid) {
		return Game.instance.nativeManager.toNova(recipe.getCraftingResult(new NovaCraftingGrid(craftingGrid)));
	}

    @Override
    public void consumeItems(CraftingGrid craftingGrid) {
        // not supported
    }

    @Override
	public Optional<Item> getNominalOutput() {
		return Game.instance.nativeManager.toNova(recipe.getRecipeOutput());
	}
}
