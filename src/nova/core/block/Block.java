package nova.core.block;

import nova.core.item.ItemStack;
import nova.core.item.ItemUtils;
import nova.core.player.Player;
import nova.core.util.Named;
import nova.core.util.Vector3i;
import nova.core.world.World;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Block implements Named {
	public Collection<ItemStack> getDroppedStacks(World world, Vector3i position) {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ItemUtils.getItemFromBlock(this)));
		return list;
	}

	public void onPlaced(World world, Vector3i position, Player player) {

	}

	public void onRemoved(World world, Vector3i position, Player player) {

	}
}
