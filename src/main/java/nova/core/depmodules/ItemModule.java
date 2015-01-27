package nova.core.depmodules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import nova.core.item.ItemManager;

class ItemModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ItemManager.class).in(Singleton.class);
	}

}
