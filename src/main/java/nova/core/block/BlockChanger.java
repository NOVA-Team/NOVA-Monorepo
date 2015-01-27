package nova.core.block;

import nova.core.player.Player;
import nova.core.util.vector.Vector3i;
import nova.core.world.World;

import java.util.Optional;

public abstract class BlockChanger {
	public class Unknown extends BlockChanger {
		public Unknown() {

		}
	}

	public class Block extends BlockChanger {
		private final Block block;
		private final World world;
		private final Vector3i position;

		public Block(World world, Vector3i position, Block block) {
			this.world = world;
			this.position = position;
			this.block = block;
		}
	}

	public class Entity extends BlockChanger {
		private final Entity entity;

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
