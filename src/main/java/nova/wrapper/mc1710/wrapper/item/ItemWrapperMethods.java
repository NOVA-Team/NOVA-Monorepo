package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.backward.entity.BWEntityPlayer;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.wrapper.block.world.BWWorld;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods extends IItemRenderer {

	ItemFactory getItemFactory();

	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		Item item = Game.instance.nativeManager.toNative(itemStack);

		item.setCount(itemStack.stackSize)
			.getTooltips(Optional.of(new BWEntityPlayer(player)), list);

		getItemFactory().saveItem(item);
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = Game.instance.nativeManager.toNative(itemStack);
		boolean b = item.onUse(new BWEntityPlayer(player), new BWWorld(world), new Vector3i(x, y, z), Direction.fromOrdinal(side), new Vector3d(hitX, hitY, hitZ));
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return b;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = Game.instance.nativeManager.toNative(itemStack);
		item.onRightClick(new BWEntityPlayer(player));
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return itemStack;
	}

	default IIcon getIconFromDamage(int p_77617_1_) {
		if (getItemFactory().getDummy().getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(getItemFactory().getDummy().getTexture().get());
		}
		return null;
	}

	default IIcon getIcon(ItemStack itemStack, int pass) {
		Item item = Game.instance.nativeManager.toNative(itemStack);
		if (item.getTexture().isPresent()) {
			return RenderUtility.instance.getIcon(item.getTexture().get());
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
		((Item) Game.instance.nativeManager.toNative(itemStack)).onRender(type.ordinal(), data);
	}

	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ((Item) Game.instance.nativeManager.toNative(itemStack)).colorMultiplier().argb();
	}
}