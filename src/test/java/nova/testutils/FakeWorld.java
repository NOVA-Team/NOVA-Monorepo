package nova.testutils;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.component.transform.BlockTransform;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.transform.shape.Cuboid;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;
import nova.internal.dummy.Wrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Calclavia
 */
public class FakeWorld extends World {

	public final Map<Vector3i, Block> blockMap = new HashMap<>();

	@Override
	public void markStaticRender(Vector3i position) {

	}

	@Override
	public void markChange(Vector3i position) {

	}

	@Override
	public Optional<Block> getBlock(Vector3i position) {
		//Gives a fake block to represent air
		FakeBlock air = new FakeBlock("air");
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		air.add(component);
		return Optional.of(blockMap.getOrDefault(position, air));
	}

	@Override
	public boolean setBlock(Vector3i position, BlockFactory blockFactory, Object... args) {
		Block newBlock = blockFactory.makeBlock();
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		newBlock.add(component);
		blockMap.put(position, newBlock);
		return true;
	}

	@Override
	public Entity addEntity(EntityFactory factory, Object... args) {
		//TODO: Implement
		return null;
	}

	@Override
	public Entity addEntity(Vector3d position, Item item) {
		//TODO: Implement
		return null;
	}

	@Override
	public Entity addClientEntity(EntityFactory factory) {
		//TODO: Implement
		return null;
	}

	@Override
	public <T extends Entity> T addClientEntity(T entity) {
		//TODO: Implement
		return null;
	}

	@Override
	public void removeEntity(Entity entity) {
		//TODO: Implement

	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		//TODO: Implement
		return null;
	}

	@Override
	public void playSoundAtPosition(Vector3d position, Sound sound) {
		//TODO: Implement
	}

	@Override
	public String getID() {
		return "fakeWorld";
	}
}
