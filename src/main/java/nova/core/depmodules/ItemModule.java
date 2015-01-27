package nova.core.depmodules;

import nova.core.item.ItemManager;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class ItemModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ItemManager.class).in(Singleton.class);
	}

}
