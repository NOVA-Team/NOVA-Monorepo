package nova.core.depmodules;

import com.google.inject.AbstractModule;
import nova.core.util.Dictionary;
import nova.core.util.Registry;

class UtilModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Dictionary.class);
		bind(Registry.class);
	}
}
