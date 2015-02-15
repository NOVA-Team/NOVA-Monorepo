package nova.wrapper.mc1710.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

/**
 * @author Calclavia
 */
public class ItemBlockWrapper extends net.minecraft.item.ItemBlock implements ItemWrapperMethods {

	public ItemBlockWrapper(net.minecraft.block.Block block) {
		super(block);
	}

	@Override
	public ItemFactory getItemFactory() {
		return ((BlockWrapper) field_150939_a).block.getItemFactory();
	}

	@Override
	public void registerIcons(IIconRegister ir) {

	}
}
