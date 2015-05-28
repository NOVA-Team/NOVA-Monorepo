package nova.core.item;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
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

		if (onPrePlace(entity, world, placePos, side, hit)) {
			return onPostPlace(entity, world, placePos, side, hit);
		}

		return false;
	}

	protected boolean onPrePlace(Entity entity, World world, Vector3i placePos, Direction side, Vector3d hit) {
		Optional<Block> checkBlock = world.getBlock(placePos);
		if (checkBlock.isPresent() && checkBlock.get().factory().equals(Game.instance.blockManager.getAirBlockFactory())) {
			return world.setBlock(placePos, blockFactory);
		}
		return false;
	}

	protected boolean onPostPlace(Entity entity, World world, Vector3i placePos, Direction side, Vector3d hit) {
		Optional<Block> opBlock = world.getBlock(placePos);
		if (opBlock.isPresent() && opBlock.get().sameType(blockFactory.getDummy())) {
			//TODO: What if the block is NOT placed by a player?
			opBlock.get().placeEvent.publish(new Block.BlockPlaceEvent(entity, side, hit, this));
		}

		return true;
	}

	@Override
	public String getID() {
		return blockFactory.getID();
	}
}
