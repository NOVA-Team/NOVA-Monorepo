package nova.wrapper.mc1710.depmodules;

import nova.core.game.ClientManager;
import nova.wrapper.mc1710.manager.FWClientManager;
import se.jbee.inject.bind.BinderModule;

public class ClientModule extends BinderModule {

	@Override
	protected void declare() {
		bind(ClientManager.class).to(FWClientManager.class);
	}
}
