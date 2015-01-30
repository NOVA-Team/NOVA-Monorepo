package nova.core.depmodules;

import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;
import nova.core.block.BlockManager;

class BlockModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(BlockManager.class).toConstructor();
	}

}
