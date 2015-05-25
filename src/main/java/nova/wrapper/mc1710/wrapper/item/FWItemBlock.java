package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.wrapper.block.forward.FWBlock;

import java.util.List;

/**
 * @author Calclavia
 */
public class FWItemBlock extends net.minecraft.item.ItemBlock implements ItemWrapperMethods {

	public FWItemBlock(net.minecraft.block.Block block) {
		super(block);
	}

	@Override
	public ItemFactory getItemFactory() {
		return ((FWBlock) field_150939_a).block.getItemFactory();
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		ItemWrapperMethods.super.addInformation(itemStack, player, list, p_77624_4_);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return ItemWrapperMethods.super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return ItemWrapperMethods.super.onItemRightClick(itemStack, world, player);
	}

	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return ItemWrapperMethods.super.getIconFromDamage(p_77617_1_);
	}

	@Override
	public IIcon getIcon(ItemStack itemStack, int pass) {
		return ItemWrapperMethods.super.getIcon(itemStack, pass);
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return ItemWrapperMethods.super.getColorFromItemStack(p_82790_1_, p_82790_2_);
	}

	@Override
	public void registerIcons(IIconRegister ir) {

	}
}
