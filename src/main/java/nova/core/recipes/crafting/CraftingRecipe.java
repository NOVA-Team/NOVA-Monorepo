package nova.core.recipes.crafting;

import nova.core.item.Item;
import nova.core.recipes.Recipe;

import java.util.Collection;
import java.util.Optional;

/**
 * Contains a single crafting recipe, no matter its crafting logic.
 *
 * @author Stan Hebben
 */
public interface CraftingRecipe extends Recipe {
	/**
	 * Checks if this crafting recipe matches the content of the given crafting
	 * grid.
	 *
	 * @param craftingGrid crafting grid to read from
	 * @return true if a crafting operation would return a valid result
	 */
	public boolean matches(CraftingGrid craftingGrid);

	/**
	 * Calculates the crafting result for the given crafting grid. Does not
	 * modify the contents of the crafting grid.
	 *
	 * @param craftingGrid crafting grid
	 * @return crafting result, empty if the recipe doesn't match
	 */
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid);

	/**
	 * Gets a nominal (example) output for this recipe. Used in recipe display.
	 *
	 * @return example output
	 */
	public Optional<Item> getNominalOutput();

	/**
	 * Consumes items for the crafting of a single item. Removes items and
	 * applies any necessary modifications on the stacks in the given crafting
	 * grid, and gives back any items if necessary.
	 *
	 * @param craftingGrid crafting grid to modify
	 */
	public void consumeItems(CraftingGrid craftingGrid);

	/**
	 * Gets the possible items for this recipe in the first non-empty recipe
	 * slot. Used to index recipes and can return empty if the recipe is
	 * calculated dynamically.
	 *
	 * @return The items
	 */
	public default Optional<Collection<String>> getPossibleItemsInFirstSlot() {
		return Optional.empty();
	}
}
