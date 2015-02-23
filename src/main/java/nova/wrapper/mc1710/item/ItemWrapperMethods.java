package nova.wrapper.mc1710.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.util.StoreUtility;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods extends IItemRenderer {

	ItemFactory getItemFactory();

	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.addAll(getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize).getTooltips(Optional.of(new BWEntityPlayer(player))));
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize);
		boolean b = item.onUse(new BWEntityPlayer(player), new BWWorld(world), new Vector3i(x, y, z), Direction.fromOrdinal(side), new Vector3d(hitX, hitY, hitZ));
		ItemWrapperRegistry.instance.updateMCItemStack(itemStack, item);
		return b;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize);
		item.onRightClick(new BWEntityPlayer(player));
		ItemWrapperRegistry.instance.updateMCItemStack(itemStack, item);
		return itemStack;
	}

	default IIcon getIconFromDamage(int p_77617_1_) {
		if (getItemFactory().getDummy().getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(getItemFactory().getDummy().getTexture().get());
		}
		return null;
	}

	default IIcon getIcon(ItemStack itemStack, int pass) {
		if (getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize).getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize).getTexture().get());
		}
		return null;
	}

	default boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
		return item.getItem() == this && getIcon(item, 0) == null;
	}

	default boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
		return true;
	}

	default void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object... data) {
		getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize).onRender(type.ordinal(), data);
	}

	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return getItemFactory().makeItem(StoreUtility.nbtToData(itemStack.getTagCompound())).setCount(itemStack.stackSize).colorMultiplier().argb();
	}
}