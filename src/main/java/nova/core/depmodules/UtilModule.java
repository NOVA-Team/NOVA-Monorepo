package nova.core.depmodules;

import nova.core.util.Dictionary;
import nova.core.util.Registry;

import com.google.inject.AbstractModule;

class UtilModule extends AbstractModule{
	@Override
	protected void configure() {
		bind(Dictionary.class);
		bind(Registry.class);
	}
}
