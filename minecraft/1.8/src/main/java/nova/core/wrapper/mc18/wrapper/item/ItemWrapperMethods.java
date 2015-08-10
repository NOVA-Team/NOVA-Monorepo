package nova.core.wrapper.mc18.wrapper.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.wrapper.mc18.wrapper.entity.backward.BWEntity;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 *
 * @author Calclavia
 */
public interface ItemWrapperMethods {

	ItemFactory getItemFactory();

	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		Item item = Game.natives().toNova(itemStack);
		item.setCount(itemStack.stackSize).events.publish(new Item.TooltipEvent(Optional.of(new BWEntity(player)), list));
		getItemFactory().saveItem(item);
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = Game.natives().toNova(itemStack);
		Item.UseEvent event = new Item.UseEvent(new BWEntity(player), new Vector3D(x, y, z), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		item.events.publish(event);
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return event.action;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = Game.natives().toNova(itemStack);
		item.events.publish(new Item.RightClickEvent(new BWEntity(player)));
		return ItemConverter.instance().updateMCItemStack(itemStack, item);
	}

	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ((Item) Game.natives().toNova(itemStack)).colorMultiplier().argb();
	}
}
