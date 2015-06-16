package nova.core.entity;

import nova.core.game.GameStatusEventBus;
import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

public class EntityManager extends Manager<Entity>{

	private EntityManager(Registry<Factory<Entity>> registry, GameStatusEventBus gseb) {
		super(registry, gseb, Entity.class);
	}

}
