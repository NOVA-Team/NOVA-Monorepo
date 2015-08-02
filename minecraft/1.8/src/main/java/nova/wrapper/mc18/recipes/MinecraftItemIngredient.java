package nova.wrapper.mc18.recipes;

import nova.core.item.Item;
import nova.core.recipes.crafting.SpecificItemIngredient;
import nova.internal.core.Game;

/**
 * @author Stan Hebben
 */
public class MinecraftItemIngredient extends SpecificItemIngredient {
	public MinecraftItemIngredient(net.minecraft.item.ItemStack itemStack) {
		super(((Item) Game.natives().toNova(itemStack)).getID());
	}
}
