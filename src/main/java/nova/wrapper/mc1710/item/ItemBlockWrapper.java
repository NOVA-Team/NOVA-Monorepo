package nova.wrapper.mc1710.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.forward.block.BlockWrapper;

import java.util.List;

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
	public void registerIcons(IIconRegister ir) {

	}
}
