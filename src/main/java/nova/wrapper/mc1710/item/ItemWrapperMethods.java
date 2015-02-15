package nova.wrapper.mc1710.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods extends IItemRenderer {

	ItemFactory getItemFactory();

	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.addAll(getItemFactory().makeItem(NBTUtility.nbtToMap(itemStack.getTagCompound())).getTooltips(Optional.of(new BWEntityPlayer(player))));
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return getItemFactory().makeItem(NBTUtility.nbtToMap(itemStack.getTagCompound())).onUse(new BWEntityPlayer(player), new BWWorld(world), new Vector3i(x, y, z), Direction.fromOrdinal(side), new Vector3d(hitX, hitY, hitZ));
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		getItemFactory().makeItem(NBTUtility.nbtToMap(itemStack.getTagCompound())).onRightClick(new BWEntityPlayer(player));
		return itemStack;
	}

	default IIcon getIconFromDamage(int p_77617_1_) {
		if (getItemFactory().getDummy().getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(getItemFactory().getDummy().getTexture().get());
		}
		return null;
	}

	default IIcon getIcon(ItemStack stack, int pass) {
		if (getItemFactory().makeItem(NBTUtility.nbtToMap(stack.getTagCompound())).getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(getItemFactory().makeItem(NBTUtility.nbtToMap(stack.getTagCompound())).getTexture().get());
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
		getItemFactory().makeItem(NBTUtility.nbtToMap(itemStack.getTagCompound())).onRender(type.ordinal(), data);
	}

}
