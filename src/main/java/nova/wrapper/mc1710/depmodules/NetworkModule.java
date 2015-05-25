package nova.wrapper.mc1710.depmodules;

import nova.core.network.NetworkManager;
import nova.wrapper.mc1710.network.netty.MCNetworkManager;
import se.jbee.inject.bind.BinderModule;

public class NetworkModule extends BinderModule {

	@Override
	protected void declare() {
		bind(NetworkManager.class).to(MCNetworkManager.class);
	}

}
