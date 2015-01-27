package nova.core.block;

import nova.core.item.ItemStack;
import nova.core.item.ItemUtils;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public abstract class Block implements Identifiable {
	private final BlockAccess blockAccess;
	private final Vector3i position;

	public Block(BlockAccess blockAccess, Vector3i position) {
		this.blockAccess = blockAccess;
		this.position = position;
	}

	public Collection<ItemStack> getDroppedStacks() {
		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(ItemUtils.getItemFromBlock(this)));
		return list;
	}

	public BlockAccess getBlockAccess() {
		return blockAccess;
	}

	public Vector3i getPosition() {
		return position;
	}

	public int getX() {
		return position.x;
	}

	public int getY() {
		return position.y;
	}

	public int getZ() {
		return position.z;
	}

	public boolean isCube() {
		return true;
	}

	public boolean isOpaqueCube() {
		return isCube();
	}

	public void onNeighborChange(Vector3i neighborPosition) {

	}

	public void onPlaced(BlockChanger changer) {

	}

	public void onRemoved(BlockChanger changer) {

	}
}
