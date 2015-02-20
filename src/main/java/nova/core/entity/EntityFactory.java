package nova.core.entity;

import nova.core.util.Factory;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class EntityFactory extends Factory<Entity> {

	public EntityFactory(Supplier<Entity> constructor) {
		super(constructor);
	}

	public Entity makeEntity(World world, Vector3d position, Quaternion rotation) {
		Entity newEntity = constructor.get();
		newEntity.setWorld(world);
		newEntity.setPosition(position);
		newEntity.setRotation(rotation);
		return newEntity;
	}
}
