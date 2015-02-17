package nova.wrapper.mc1710.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;
import nova.core.util.components.Storable;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class MCItem extends Item implements Storable {
    private final net.minecraft.item.Item item;
    private final int meta;
    private final NBTTagCompound tag;

    private final String id;

    public MCItem(ItemStack itemStack) {
        this(itemStack.getItem(), itemStack.getHasSubtypes() ? itemStack.getItemDamage() : 0, itemStack.getTagCompound());
    }

    public MCItem(net.minecraft.item.Item item, int meta, NBTTagCompound tag) {
        this.item = item;
        this.meta = meta;
        this.tag = tag;

        id = net.minecraft.item.Item.itemRegistry.getNameForObject(item) + (item.getHasSubtypes() ? ":" + meta : "");
    }

    public net.minecraft.item.Item getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }

    public NBTTagCompound getTag() {
        return tag;
    }

    public net.minecraft.item.ItemStack makeItemStack(int stackSize) {
        ItemStack result = new ItemStack(item, stackSize, meta);
        if (tag != null)
            result.setTagCompound(tag);
        return result;
    }

    @Override
    public String getID() {
        return id;
    }
}
