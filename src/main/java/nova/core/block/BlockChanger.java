package nova.core.block;

import nova.core.player.Player;

import java.util.Optional;

/**
 * A class representing several types of object which could change a block.
 */
public abstract class BlockChanger {
	/**
	 * An unknown object changed the block.
	 */
	public class Unknown extends BlockChanger {
		public Unknown() {

		}
	}

	/**
	 * A block changed the block.
	 */
	public class Block extends BlockChanger {
		/**
		 * The BlockAccess used to access the block.
		 */
		public final BlockAccess blockAccess;
		/**
		 * The block which changed the block.
		 */
		public final Block block;

		public Block(BlockAccess blockAccess, Block block) {
			this.blockAccess = blockAccess;
			this.block = block;
		}
	}

	/**
	 * An entity changed the block.
	 */
	public class Entity extends BlockChanger {
		/**
		 * The entity which changed the block.
		 */
		public final Entity entity;

		public Entity(Entity entity) {
			this.entity = entity;
		}

		/**
		 * @return true if the Entity is a player.
		 */
		public boolean hasPlayer() {
			return entity instanceof Player;
		}

		/**
		 * Gets the player from the Entity.
		 *
		 * @return an optional of the Player.
		 */
		public Optional<Player> getPlayer() {
			return hasPlayer() ? Optional.of((Player) entity) : Optional.empty();
		}
	}
}
