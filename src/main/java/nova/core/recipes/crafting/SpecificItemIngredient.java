package nova.core.recipes.crafting;

import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.exception.NovaException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Specifies an ingredient identifying a single kind of item.
 * @author Stan Hebben
 */
public class SpecificItemIngredient implements ItemIngredient {
	private final String itemId;

	public SpecificItemIngredient(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}

	@Override
	public Optional<Collection<String>> getPossibleItemIds() {
		return Optional.of(Collections.singleton(itemId));
	}

	@Override
	public Optional<Collection<Item>> getExampleItems() {
		return Optional.of(Collections.singleton(getItem(itemId)));
	}

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return ingredient.matches(getItem(itemId));
	}

	@Override
	public boolean matches(Item item) {
		return item.getID().equals(itemId);
	}

	@Override
	public Optional<String> getTag() {
		return Optional.empty();
	}

	@Override
	public Item consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return original;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SpecificItemIngredient that = (SpecificItemIngredient) o;

		if (!itemId.equals(that.itemId)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return itemId.hashCode();
	}

	private Item getItem(String itemId) {
		Optional<ItemFactory> itemFactory = Game.instance().itemManager().getItem(itemId);

		if (itemFactory.isPresent()) {
			return itemFactory.get().makeItem();
		}

		throw new NovaException("Missing item: " + itemId);
	}
}
