package nova.wrapper.mc1710.depmodules;

import nova.core.util.RetentionManager;
import nova.wrapper.mc1710.manager.MCRetentionManager;
import se.jbee.inject.bind.BinderModule;

public class SaveModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RetentionManager.class).to(MCRetentionManager.class);
	}

}
