package nova.core.world;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.game.Game;
import nova.core.item.Item;
import nova.core.util.Identifiable;
import nova.core.util.transform.Cuboid;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;

import java.util.Optional;
import java.util.Set;

/**
 * An in-game world
 * @author Calclavia
 */
public abstract class World implements Identifiable {

	/**
	 * Marks a position to render static.
	 * @param position The position to perform the static re-rendering.
	 */
	public abstract void markStaticRender(Vector3i position);

	/**
	 * Marks a specific block to indicate it changed.
	 * @param position The position being changed.
	 */
	public abstract void markChange(Vector3i position);

	/**
	 * Gets the block which occupies the given position.
	 * @param position The position to query.
	 * @return The block at the position. If the block is air, it will return the air block. If no block is present (the void), it will return an empty optional.
	 */
	public abstract Optional<Block> getBlock(Vector3i position);

	/**
	 * Sets the block occupying a given position.
	 * @param position The position of the block to set.
	 * @param blockFactory The block factory.
	 * @param args The block constructor arguments.
	 * @return {@code true} if the replace was successful.
	 */
	public abstract boolean setBlock(Vector3i position, BlockFactory blockFactory, Object... args);

	/**
	 * Removes the block in the specified position.
	 * @param position the position of the block to remove.
	 * @return {@code true} if the block was removed.
	 */
	public boolean removeBlock(Vector3i position) {
		return setBlock(position, Game.instance.blockManager.getAirBlockFactory());
	}

	/**
	 * Creates an entity
	 * @param factory The entity factory
	 */
	public abstract Entity addEntity(EntityFactory factory, Object... args);

	/**
	 * Creates an entity that holds an item
	 * @param item The item
	 */
	public abstract Entity addEntity(Vector3d position, Item item);

	/**
	 * Creates an entity only on the client side.
	 * For example, particle effects.
	 */
	public abstract Entity addClientEntity(EntityFactory factory);

	/**
	 * Creates an entity only on the client side.
	 * For example, particle effects.
	 */
	public abstract <T extends Entity> T addClientEntity(T entity);

	/**
	 * Destroys an entity, removing it from the world.
	 */
	public abstract void removeEntity(Entity entity);

	/**
	 * Gets a set of entities within a certain bound
	 * @param bound The boundary
	 * @return A set of entities.
	 */
	public abstract Set<Entity> getEntities(Cuboid bound);
}
