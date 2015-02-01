package nova.wrapper.mc1710.forward.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.util.NBTUtility;
import nova.wrapper.mc1710.util.RenderUtility;

/**
 * @author Calclavia
 */
public class ItemWrapper extends net.minecraft.item.Item implements IItemRenderer {

	public final ItemFactory itemFactory;

	public ItemWrapper(ItemFactory item) {
		this.itemFactory = item;
		setUnlocalizedName(item.getID());
	}

	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		if (itemFactory.getDummy().getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(itemFactory.getDummy().getTexture().get());
		}
		return null;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if (itemFactory.makeItem(NBTUtility.nbtToMap(stack.getTagCompound())).getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(itemFactory.makeItem(NBTUtility.nbtToMap(stack.getTagCompound())).getTexture().get());
		}
		return null;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return item.getItem() == this && getIcon(item, 0) == null;
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
