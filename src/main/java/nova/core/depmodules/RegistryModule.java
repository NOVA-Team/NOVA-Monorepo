package nova.core.depmodules;

import nova.core.util.Registry;

import com.google.inject.AbstractModule;

class RegistryModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(Registry.class);
	}

}
