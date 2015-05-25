package nova.wrapper.mc1710.depmodules;

import nova.core.game.ClientManager;
import nova.wrapper.mc1710.manager.MCClientManager;
import se.jbee.inject.bind.BinderModule;

public class ClientModule extends BinderModule {

	@Override
	protected void declare() {
		bind(ClientManager.class).to(MCClientManager.class);
	}
}
