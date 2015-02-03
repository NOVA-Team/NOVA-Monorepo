package nova.wrapper.mc1710.recipes;

import nova.core.item.Item;
import nova.core.recipes.crafting.SpecificItemIngredient;
import nova.wrapper.mc1710.util.WrapUtility;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by Stan on 3/02/2015.
 */
public class MinecraftItemIngredient extends SpecificItemIngredient {
    private final Item item;

    public MinecraftItemIngredient(net.minecraft.item.ItemStack itemStack) {
        super(WrapUtility.unwrapItemStack(itemStack).getItem().getID());

        this.item = WrapUtility.unwrapItemStack(itemStack).getItem();
    }

    @Override
    public Optional<Collection<Item>> getExampleItems() {
        return Optional.of(Collections.singleton(item));
    }
}
