package nova.core.depmodules;

import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

import nova.core.item.ItemManager;

class ItemModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(ItemManager.class).toConstructor();
	}

}
