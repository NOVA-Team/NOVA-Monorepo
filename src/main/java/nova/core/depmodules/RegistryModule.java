package nova.core.depmodules;

import com.google.inject.AbstractModule;
import nova.core.util.Registry;

class RegistryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Registry.class);
	}

}
