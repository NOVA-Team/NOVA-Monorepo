package nova.core.game;

import nova.core.block.BlockManager;
import nova.core.entity.EntityManager;
import nova.core.item.ItemManager;
import nova.core.world.WorldManager;

import java.util.Optional;

public class Game {

	/**
	 * Use only when injection is not a solution. For example when performance
	 * is a concern. Treat as -100 style points. Must be initialized by code handling launch and dependency injection entry point.
	 */
	public static Optional<Game> instance = Optional.empty();

	public final BlockManager blockManager;
	public final EntityManager entityManager;
	public final ItemManager itemManager;
	public final WorldManager worldManager;

	private Game(BlockManager blockManager, EntityManager entityManager, ItemManager itemManager, WorldManager worldManager) {
		this.blockManager = blockManager;
		this.entityManager = entityManager;
		this.itemManager = itemManager;
		this.worldManager = worldManager;
	}

}
