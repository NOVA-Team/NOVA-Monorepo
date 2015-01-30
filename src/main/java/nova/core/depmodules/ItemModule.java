package nova.core.depmodules;

import nova.core.item.ItemManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class ItemModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(ItemManager.class).toConstructor();
	}

}
