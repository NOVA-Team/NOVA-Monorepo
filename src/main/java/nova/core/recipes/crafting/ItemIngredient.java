package nova.core.recipes.crafting;

import nova.core.item.Item;
import nova.core.item.ItemStack;

import java.util.Collection;
import java.util.Optional;

/**
 * Contains a single item ingredient. An ingredient can be a specific item,
 * an ore dictionary entry, or anything else that can match against items.
 *
 * @author Stan Hebben
 */
public interface ItemIngredient {
	/**
	 * Retrieves an ingredient to represent a specific item.
	 *
	 * @param itemId item ID
	 * @return ingredient
	 */
	public static ItemIngredient forItem(String itemId) {
		return new SpecificItemIngredient(itemId);
	}

	/**
	 * Retrieves an ingredient to represent a dictionary entry.
	 *
	 * @param id dictionary entry ID
	 * @return ingredient
	 */
	public static ItemIngredient forDictionary(String id) {
		return new OreItemIngredient(id);
	}

	/**
	 * Returns a list of all items that could possibly match this ingredient.
	 * Should return Optional.empty() if there is no such list.
	 *
	 * @return possible items
	 */
	public Optional<Collection<String>> getPossibleItemIds();

	/**
	 * Returns a list of example items. This list could be used to render
	 * ingredients when displayed to users.
	 *
	 * @return example items
	 */
	public Optional<Collection<Item>> getExampleItems();

	/**
	 * Checks if this ingredient is a subset of another ingredient. An
	 * ingredient is a subset if all items that match this ingredient also
	 * match the other ingredient.
	 *
	 * @param ingredient The another ingredient
	 * @return Result of the check
	 */
	public boolean isSubsetOf(ItemIngredient ingredient);

	/**
	 * Checks if this ingredient matches the given item.
	 *
	 * @param item Item to check
	 * @return Whether the item matches or not
	 */
	public boolean matches(ItemStack item);

	/**
	 * Returns the ingredient tag. Ingredients can be tagged to make them easy to read from recipe functions.
	 *
	 * @return ingredient tag
	 */
	public Optional<String> getTag();

	/**
	 * Performs any necessary actions when this ingredient is consumed due to crafting a single item. May return a
	 * stack with zero size, but cannot return null.
	 *
	 * @param original Ingredient
	 * @param craftingGrid {@link CraftingGrid} used
	 * @return Resulting {@link ItemStack}
	 */
	public ItemStack consumeOnCrafting(ItemStack original, CraftingGrid craftingGrid);
}
