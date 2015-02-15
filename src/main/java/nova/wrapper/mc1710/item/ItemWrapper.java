package nova.wrapper.mc1710.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import nova.core.item.ItemFactory;

/**
 * @author Calclavia
 */
public class ItemWrapper extends net.minecraft.item.Item implements ItemWrapperMethods {

	public final ItemFactory itemFactory;

	public ItemWrapper(ItemFactory item) {
		this.itemFactory = item;
		setUnlocalizedName(item.getID());
		setMaxStackSize(item.getDummy().getMaxStackSize());
	}

	@Override
	public ItemFactory getItemFactory() {
		return itemFactory;
	}

	@Override
	public void registerIcons(IIconRegister ir) {

	}
}
