package nova.internal.core.depmodules;

import nova.core.block.BlockManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class BlockModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(BlockManager.class).toConstructor();
	}

}
