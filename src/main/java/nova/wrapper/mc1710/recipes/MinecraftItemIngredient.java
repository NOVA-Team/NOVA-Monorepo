package nova.wrapper.mc1710.recipes;

import nova.core.recipes.crafting.SpecificItemIngredient;
import nova.wrapper.mc1710.util.WrapUtility;

/**
 * @author Stan Hebben
 */
public class MinecraftItemIngredient extends SpecificItemIngredient {
    public MinecraftItemIngredient(net.minecraft.item.ItemStack itemStack) {
		super(WrapUtility.unwrapItemStack(itemStack).get().getID());
	}
}
