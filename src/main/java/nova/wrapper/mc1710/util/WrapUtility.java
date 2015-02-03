package nova.wrapper.mc1710.util;

import net.minecraft.item.Item;
import nova.core.item.ItemStack;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static net.minecraft.item.ItemStack wrapItemStack(ItemStack itemStack) {
        return ItemWrapperRegistry.instance.getMCItemStack(itemStack);
    }

    public static ItemStack unwrapItemStack(net.minecraft.item.ItemStack itemStack) {
        return ItemWrapperRegistry.instance.getNovaItemStack(itemStack);
    }

    public static String getItemID(net.minecraft.item.Item item, int meta) {
        if (item.getHasSubtypes()) {
            return Item.itemRegistry.getNameForObject(item) + ":" + meta;
        } else {
            return Item.itemRegistry.getNameForObject(item);
        }
    }
}
