package nova.wrapper.mc1710.forward.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.util.NBTUtility;

/**
 * @author Calclavia
 */
public class ItemWrapper extends net.minecraft.item.Item implements IItemRenderer {

	public final ItemFactory itemFactory;

	public ItemWrapper(ItemFactory item) {
		this.itemFactory = item;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return item.getItem() == this;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		itemFactory.makeItem(NBTUtility.nbtToMap(itemStack.getTagCompound())).onRender(type.ordinal(), data);
	}
}
