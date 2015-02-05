package nova.core.di;

import se.jbee.inject.bind.BuildinBundle;
import se.jbee.inject.bootstrap.BootstrapperBundle;

public class DICoreModule extends BootstrapperBundle {

	@Override
	protected void bootstrap() {

		install(BuildinBundle.PROVIDER); //To allow injection of providers

	}

}
