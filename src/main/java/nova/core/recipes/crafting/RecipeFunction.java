package nova.core.recipes.crafting;

import nova.core.item.Item;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Stan on 31/01/2015.
 */
@FunctionalInterface
public interface RecipeFunction {
	Optional<Item> doCrafting(CraftingGrid craftingGrid, Map<String, Item> taggedIngredients);
}
