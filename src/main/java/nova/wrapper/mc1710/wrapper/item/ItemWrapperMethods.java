package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.component.renderer.ItemRenderer;
import nova.internal.Game;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.wrapper.mc1710.backward.render.BWModel;
import nova.wrapper.mc1710.render.RenderUtility;
import nova.wrapper.mc1710.wrapper.entity.BWEntity;

import java.util.List;
import java.util.Optional;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods extends IItemRenderer {

	ItemFactory getItemFactory();

	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		Item item = Game.natives().toNova(itemStack);
		item.setCount(itemStack.stackSize).tooltipEvent.publish(new Item.TooltipEvent(Optional.of(new BWEntity(player)), list));
		getItemFactory().saveItem(item);
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = Game.natives().toNova(itemStack);
		Item.UseEvent event = new Item.UseEvent(new BWEntity(player), new Vector3i(x, y, z), Direction.fromOrdinal(side), new Vector3d(hitX, hitY, hitZ));
		item.useEvent.publish(event);
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return event.action;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = Game.natives().toNova(itemStack);
		item.rightClickEvent.publish(new Item.RightClickEvent(new BWEntity(player)));
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
		Item item = Game.natives().toNova(itemStack);
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
		Item item = Game.natives().toNova(itemStack);
		if (item.has(ItemRenderer.class)) {
			BWModel model = new BWModel();
			item.get(ItemRenderer.class).onRender.accept(model);
			model.render();
		}
	}

	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ((Item) Game.natives().toNova(itemStack)).colorMultiplier().argb();
	}
}
