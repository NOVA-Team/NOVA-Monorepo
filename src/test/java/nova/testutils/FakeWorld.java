package nova.testutils;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.component.transform.EntityTransform;
import nova.core.entity.Entity;
import nova.core.item.Item;
import nova.core.sound.Sound;
import nova.core.util.Factory;
import nova.core.util.shape.Cuboid;
import nova.core.world.World;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Calclavia
 */
public class FakeWorld extends World {

	public final Map<Vector3D, Block> blockMap = new HashMap<>();
	public final Set<Entity> entities = new HashSet<>();

	@Override
	public void markStaticRender(Vector3D position) {

	}

	@Override
	public void markChange(Vector3D position) {

	}

	@Override
	public Optional<Block> getBlock(Vector3D position) {
		//Gives a fake block to represent air
		Block air = Game.blocks().getAirBlock();
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		air.add(component);
		return Optional.of(blockMap.getOrDefault(position, air));
	}

	@Override
	public boolean setBlock(Vector3D position, Factory<Block> blockFactory, Object... args) {
		Block newBlock = blockFactory.make();
		BlockTransform component = new BlockTransform();
		component.setPosition(position);
		component.setWorld(this);
		newBlock.add(component);
		blockMap.put(position, newBlock);
		return true;
	}

	@Override
	public Entity addEntity(Factory<Entity> factory, Object... args) {
		Entity make = factory.make(args);
		EntityTransform component = new EntityTransform();
		component.setWorld(this);
		make.add(component);
		entities.add(make);
		return make;
	}

	@Override
	public Optional<Entity> getEntity(String UUID) {
		return entities.stream()
			.filter(entity -> entity.getUniqueID().equals(UUID))
			.findAny();
	}

	@Override
	public Entity addEntity(Vector3D position, Item item) {
		//TODO: Implement
		return null;
	}

	@Override
	public Entity addClientEntity(Factory<Entity>  factory) {
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
		entities.remove(entity);
	}

	@Override
	public Set<Entity> getEntities(Cuboid bound) {
		//TODO: Implement
		return null;
	}

	@Override
	public void playSoundAtPosition(Vector3D position, Sound sound) {
		//TODO: Implement
	}

	@Override
	public String getID() {
		return "fakeWorld";
	}
}
