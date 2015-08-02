package nova.wrapper.mc1710.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Calclavia
 */
public class ModCreativeTab extends CreativeTabs {
	public Item item;

	public ModCreativeTab(String label, Item item) {
		super(label);

		this.item = item;
	}

	public ModCreativeTab(String label) {
		this(label, null);
	}

	@Override
	public Item getTabIconItem() {
		return item;
	}
}
