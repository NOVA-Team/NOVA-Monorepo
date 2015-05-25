package nova.wrapper.mc1710.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Calclavia
 */
public class ModCreativeTab extends CreativeTabs {
	public Item item;

	public ModCreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return item;
	}
}
