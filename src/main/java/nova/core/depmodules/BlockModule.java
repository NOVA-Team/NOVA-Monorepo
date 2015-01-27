package nova.core.depmodules;

import nova.core.block.BlockManager;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class BlockModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BlockManager.class).in(Singleton.class);
	}

}
