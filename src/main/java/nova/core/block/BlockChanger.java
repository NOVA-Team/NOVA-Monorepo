package nova.core.block;

import nova.core.player.Player;

import java.util.Optional;

public abstract class BlockChanger {
	public class Unknown extends BlockChanger {
		public Unknown() {

		}
	}

	public class Block extends BlockChanger {
		public final BlockAccess blockAccess;
		public final Block block;

		public Block(BlockAccess blockAccess, Block block) {
			this.blockAccess = blockAccess;
			this.block = block;
		}
	}

	public class Entity extends BlockChanger {
		public final Entity entity;

		public Entity(Entity entity) {
			this.entity = entity;
		}

		public boolean hasPlayer() {
			return entity instanceof Player;
		}

		public Optional<Player> getPlayer() {
			return hasPlayer() ? Optional.of((Player) entity) : Optional.empty();
		}
	}
}
