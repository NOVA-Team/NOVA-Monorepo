package nova.core.recipes.crafting;

import nova.core.game.Game;
import nova.core.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
		return Optional.of(Game.instance().itemDictionary().get(name));
	}

	@Override
	public Optional<Collection<Item>> getExampleItems() {
		Game game = Game.instance();

		List<Item> result = new ArrayList<Item>();

		game.itemDictionary().get(name)
				.forEach(itemId -> result.add(game.itemManager().get(itemId).get()));

		return Optional.of(result);
	}

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return false;
	}

	@Override
	public boolean matches(Item item) {
		return Game.instance().itemDictionary().get(name).contains(item.getID());
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
