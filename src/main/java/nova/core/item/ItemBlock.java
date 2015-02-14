package nova.core.item;

import nova.core.block.Block;
import nova.core.entity.Entity;
import nova.core.util.Direction;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;
import nova.core.world.World;

/**
 * An ItemBlock is an Item that is meant to be used to place blocks.
 * @author Calclavia
 */
public class ItemBlock extends Item {

	public final Block block;

	public ItemBlock(Block block) {
		this.block = block;
	}

	@Override
	public boolean onUse(Entity entity, World world, Vector3i position, Direction side, Vector3d hit) {

		Block block = world.getBlock(position).get();
		Vector3i placePos = position.add(side.toVector());

		if (onPrePlace(world, placePos)) {
			return onPostPlace(world, placePos);
		}

		return false;
	}

	protected boolean onPrePlace(World world, Vector3i placePos) {
		return world.setBlock(placePos, block);
	}

	protected boolean onPostPlace(World world, Vector3i placePos) {
		return true;
	}

	@Override
	public String getID() {
		return block.getID();
	}
}
