package nova.wrappertests.depmodules;

import nova.core.entity.Entity;
import nova.core.game.ClientManager;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeClientModule extends BinderModule {

	@Override
	protected void declare() {
		bind(ClientManager.class).to(FakeClientManager.class);
	}

	public static class FakeClientManager extends ClientManager {
		@Override
		public Entity getPlayer() {
			return null;
		}

		@Override
		public boolean isPaused() {
			return false;
		}
	}
}
