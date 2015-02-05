package nova.core.recipes.crafting;

import nova.core.item.ItemStack;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Stan on 31/01/2015.
 */
@FunctionalInterface
public interface RecipeFunction {
	public Optional<ItemStack> doCrafting(CraftingGrid craftingGrid, Map<String, ItemStack> taggedIngredients);
}
