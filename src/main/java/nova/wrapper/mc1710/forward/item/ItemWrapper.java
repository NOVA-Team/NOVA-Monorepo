package nova.wrapper.mc1710.forward.item;

import nova.core.item.Item;

/**
 * @author Calclavia
 */
public class ItemWrapper extends net.minecraft.item.Item {

	public final Item item;

	public ItemWrapper(Item item) {
		this.item = item;
	}
}
