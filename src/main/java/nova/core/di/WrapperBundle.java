package nova.core.di;

import nova.core.depmodules.CoreBundle;
import se.jbee.inject.bootstrap.BootstrapperBundle;

public class WrapperBundle extends BootstrapperBundle{

	@Override
	protected void bootstrap() {
		install(CoreBundle.class);
		
		//TODO detect and install modules from wrappers.
	}

}
