package nova.wrapper.mc1710.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import nova.core.item.ItemStack;
import nova.core.player.Player;
import nova.wrapper.mc1710.item.ItemWrapperRegistry;

import java.util.Optional;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static net.minecraft.item.ItemStack wrapItemStack(ItemStack itemStack) {
        return ItemWrapperRegistry.instance.getMCItemStack(itemStack);
    }

    public static net.minecraft.item.ItemStack wrapItemStack(Optional<ItemStack> itemStack) {
        if (itemStack.isPresent())
            return ItemWrapperRegistry.instance.getMCItemStack(itemStack.get());
        else
            return null;
    }

    public static Optional<ItemStack> unwrapItemStack(net.minecraft.item.ItemStack itemStack) {
        if (itemStack == null)
            return Optional.empty();
        else
            return Optional.of(ItemWrapperRegistry.instance.getNovaItemStack(itemStack));
    }

    public static Optional<Player> getNovaPlayer(EntityPlayer player) {
        // TODO: implement
        return Optional.empty();
    }

    public EntityPlayer getMCPlayer(Optional<Player> player) {
        return null;
    }

    public static String getItemID(net.minecraft.item.Item item, int meta) {
        if (item.getHasSubtypes()) {
            return Item.itemRegistry.getNameForObject(item) + ":" + meta;
        } else {
            return Item.itemRegistry.getNameForObject(item);
        }
    }
}
