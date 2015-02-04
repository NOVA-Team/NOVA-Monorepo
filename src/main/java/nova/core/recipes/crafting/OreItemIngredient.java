package nova.core.recipes.crafting;

import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.item.ItemStack;

import java.util.*;

/**
 * Created by Stan on 3/02/2015.
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
        return Optional.of(Game.instance.get().oreDictionary.get(name));
    }

    @Override
    public Optional<Collection<Item>> getExampleItems() {
        Game game = Game.instance.get();

        List<Item> result = new ArrayList<Item>();
        for (String itemId : game.oreDictionary.get(name)) {
            result.add(game.itemManager.getItem(itemId).get());
        }
        return Optional.of(result);
    }

    @Override
    public boolean isSubsetOf(ItemIngredient ingredient) {
        return false;
    }

    @Override
    public boolean matches(ItemStack item) {
        return false;
    }

    @Override
    public Optional<String> getTag() {
        return Optional.empty();
    }

    @Override
    public ItemStack consumeOnCrafting(ItemStack original, CraftingGrid craftingGrid) {
        return null;
    }
}
