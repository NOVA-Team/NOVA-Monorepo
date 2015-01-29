package nova.wrapper.mc1710.util;

import nova.core.item.ItemStack;
import nova.wrapper.mc1710.forward.item.ItemWrapper;

/**
 * Wrap utility methods.
 * @author Calclavia
 */
public class WrapUtility {

	public static net.minecraft.item.ItemStack wrapItemStack(ItemStack itemStack) {
		return new net.minecraft.item.ItemStack(new ItemWrapper(itemStack.getItem()), itemStack.getStackSize());
	}

}
