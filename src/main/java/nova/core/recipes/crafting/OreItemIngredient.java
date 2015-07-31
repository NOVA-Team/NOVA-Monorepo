package nova.core.recipes.crafting;

import nova.core.item.Item;
import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class OreItemIngredient implements ItemIngredient {
	private final String name;

	public OreItemIngredient(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Optional<Collection<String>> getPossibleItemIds() {
		return Optional.of(Game.itemDictionary().get(name).stream().map(Item::getID).collect(Collectors.toList()));
	}

	@Override
	public Optional<Collection<Item>> getExampleItems() {
		List<Item> result = new ArrayList<Item>();


		return Optional.of(Game.itemDictionary().get(name));
	}

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return false;
	}

	@Override
	public boolean matches(Item item) {
		return Game.itemDictionary().get(name).contains(item);
	}

	@Override
	public Optional<String> getTag() {
		return Optional.empty();
	}

	@Override
	public Item consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return null;
	}
}
