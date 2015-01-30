package nova.core.recipes;

import java.util.List;
import java.util.Optional;
import nova.core.item.Item;
import nova.core.item.ItemStack;

/**
 * Contains a single item ingredient. An ingredient can be a specific item,
 * an ore dictionary entry, or anything else that can match against items.
 * 
 * @author Stan Hebben
 */
public interface ItemIngredient
{
	/**
	 * Returns a list of all items that could possibly match this ingredient.
	 * Should return Optional.empty() if there is no such list.
	 * 
	 * @return possible items
	 */
	public Optional<List<Item>> getPossibleItems();
	
	/**
	 * Returns a list of example items. This list could be used to render 
	 * ingredients when displayed to users.
	 * 
	 * @return example items
	 */
	public Optional<List<Item>> getExampleItems();
	
	/**
	 * Checks if this ingredient is a subset of another ingredient. An
	 * ingredient is a subset if all items that match this ingredient also
	 * match the other ingredient.
	 * 
	 * @param ingredient
	 * @return 
	 */
	public boolean isSubsetOf(ItemIngredient ingredient);
	
	/**
	 * Checks if this ingredient matches the given item.
	 * 
	 * @param item
	 * @return 
	 */
	public boolean matches(ItemStack item);
}
