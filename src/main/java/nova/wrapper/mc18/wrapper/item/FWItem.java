package nova.wrapper.mc18.wrapper.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import nova.core.item.ItemFactory;

import java.util.List;

/**
 * @author Calclavia
 */
public class FWItem extends net.minecraft.item.Item implements ItemWrapperMethods {

	public final ItemFactory itemFactory;

	public FWItem(ItemFactory item) {
		this.itemFactory = item;
		setUnlocalizedName(item.getID());
		setMaxStackSize(item.getDummy().getMaxCount());
	}

	@Override
	public ItemFactory getItemFactory() {
		return itemFactory;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		ItemWrapperMethods.super.addInformation(itemStack, player, list, p_77624_4_);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ItemWrapperMethods.super.onItemUse(itemStack, player, world, pos.getX(), pos.getY(), pos.getZ(), side.ordinal(), hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return ItemWrapperMethods.super.onItemRightClick(itemStack, world, player);
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return ItemWrapperMethods.super.getColorFromItemStack(p_82790_1_, p_82790_2_);
	}

}
