package nova.core.item.block;

import nova.core.block.Block;
import nova.core.item.ItemStack;
import nova.core.item.ItemUtils;
import nova.core.util.Vector3i;
import nova.core.world.World;

import java.util.ArrayList;
import java.util.Collection;

public interface Droppable {
	default Collection<ItemStack> getDroppedStacks(World world, Vector3i position) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(ItemUtils.getItemFromBlock((Block) this)));
		return list;
	}
}
