package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.game.Game;
import nova.core.util.Direction;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

import java.util.Optional;

/**
 * An ItemBlock is an Item that is meant to be used to place blocks.
 * @author Calclavia
 */
public class ItemBlock extends Item {

	public final BlockFactory blockFactory;

	public ItemBlock(BlockFactory blockFactory) {
		this.blockFactory = blockFactory;
	}

	@Override
	public boolean onUse(Entity entity, World world, Vector3i position, Direction side, Vector3d hit) {
		Vector3i placePos = position.add(side.toVector());

		if (onPrePlace(world, placePos)) {
			return onPostPlace(world, placePos, entity);
		}

		return false;
	}

	protected boolean onPrePlace(World world, Vector3i placePos) {
		Optional<Block> checkBlock = world.getBlock(placePos);
		if (checkBlock.isPresent() && checkBlock.get().factory().equals(Game.instance.blockManager.getAirBlockFactory())) {
			return world.setBlock(placePos, blockFactory);
		}
		return false;
	}

	protected boolean onPostPlace(World world, Vector3i placePos, Entity entity) {

		Optional<Block> opBlock = world.getBlock(placePos);
		if (opBlock.isPresent() && opBlock.get().sameType(blockFactory.getDummy())) {
			opBlock.get().placeEvent.publish(new Block.BlockPlaceEvent(Optional.of(entity)));
		}

		return true;
	}

	@Override
	public String getID() {
		return blockFactory.getID();
	}
}
