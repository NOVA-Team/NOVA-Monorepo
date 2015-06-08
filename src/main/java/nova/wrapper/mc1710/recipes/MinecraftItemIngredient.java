package nova.wrapper.mc1710.recipes;

import nova.internal.core.Game;
import nova.core.item.Item;
import nova.core.recipes.crafting.SpecificItemIngredient;

/**
 * @author Stan Hebben
 */
public class MinecraftItemIngredient extends SpecificItemIngredient {
	public MinecraftItemIngredient(net.minecraft.item.ItemStack itemStack) {
		super(((Item) Game.natives().toNova(itemStack)).getID());
	}
}
