package nova.core.block;

import nova.core.player.Player;
import nova.core.world.World;

import java.util.Optional;

/**
 * A class representing several types of object which could change a block.
 */
public abstract class BlockChanger {
	/**
	 * An unknown object changed the block.
	 */
	public static class Unknown extends BlockChanger {
		public Unknown() {

		}
	}

	/**
	 * A block changed the block.
	 */
	public static class Block extends BlockChanger {
		/**
		 * The BlockAccess used to access the block.
		 */
		public final World world;
		/**
		 * The block which changed the block.
		 */
		public final Block block;

		public Block(World world, Block block) {
			this.world = world;
			this.block = block;
		}
	}

	/**
	 * An entity changed the block.
	 */
	public static class Entity extends BlockChanger {
		/**
		 * The entity which changed the block.
		 */
		public final nova.core.entity.Entity entity;

		public Entity(nova.core.entity.Entity entity) {
			this.entity = entity;
		}

		/**
		 * @return {@code true} if the Entity is a player.
		 */
		public boolean hasPlayer() {
			return entity instanceof Player;
		}

		/**
		 * Gets the player from the Entity.
		 *
		 * @return An optional of the Player.
		 */
		public Optional<Player> getPlayer() {
			return hasPlayer() ? Optional.of((Player) entity) : Optional.empty();
		}
	}
}
