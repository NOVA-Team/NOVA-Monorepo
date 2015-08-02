package nova.core.wrapper.mc18.depmodules;

import nova.core.network.NetworkManager;
import nova.core.wrapper.mc18.network.netty.MCNetworkManager;
import se.jbee.inject.bind.BinderModule;

public class NetworkModule extends BinderModule {

	@Override
	protected void declare() {
		bind(NetworkManager.class).to(MCNetworkManager.class);
	}

}
