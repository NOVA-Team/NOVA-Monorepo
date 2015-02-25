package nova.core.world;

import nova.core.block.BlockAccess;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.util.Identifiable;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3i;

import java.util.Set;

/**
 * A in-game world
 *
 * @see BlockAccess
 */
public abstract class World implements Identifiable, BlockAccess {

	/**
	 * Marks a position to render static.
	 *
	 * @param position - The position to perform the static re-rendering.
	 */
	public abstract void markStaticRender(Vector3i position);

	/**
	 * Marks a specific block to indicate it changed.
	 *
	 * @param position - The position being changed.
	 */
	public abstract void markChange(Vector3i position);

	/**
	 * Creates an entity
	 *
	 * @param factory - The entity factory
	 */
	public abstract Entity createEntity(EntityFactory factory);

	/**
	 * Creates an entity only on the client side.
	 * For example, particle effects.
	 */
	public abstract Entity createClientEntity(EntityFactory factory);

	/**
	 * Destroys an entity, removing it from the world.
	 */
	public abstract void destroyEntity(Entity entity);

	/**
	 * Gets a set of entities within a certain bound
	 * @param bound The boundary
	 * @return A set of entities.
	 */
	public abstract Set<Entity> getEntities(Cuboid bound);
}
