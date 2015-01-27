package nova.core.depmodules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class BlockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BlockModule.class).in(Singleton.class);
	}

}
