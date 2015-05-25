package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Stan on 3/02/2015.
 */
public final class MinecraftItemMapping {
    public final Item item;
    public final int meta;

    public MinecraftItemMapping(Item item, int meta) {
        this.item = item;
        this.meta = item.getHasSubtypes() ? meta : 0;
    }

    public MinecraftItemMapping(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.meta = itemStack.getHasSubtypes() ? itemStack.getItemDamage() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MinecraftItemMapping that = (MinecraftItemMapping) o;

        if (meta != that.meta) return false;
        if (!item.equals(that.item)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + meta;
        return result;
    }

    @Override
    public String toString() {
        if (item.getHasSubtypes()) {
            return Item.itemRegistry.getNameForObject(item) + ":" + meta;
        } else {
            return Item.itemRegistry.getNameForObject(item);
        }
    }
}
