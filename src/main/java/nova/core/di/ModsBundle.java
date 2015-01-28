package nova.core.di;

import se.jbee.inject.bootstrap.BootstrapperBundle;

public class ModsBundle extends BootstrapperBundle{

	@Override
	protected void bootstrap() {
		install(WrapperBundle.class);
		
		//TODO detect and install modules from mods.
	}

}