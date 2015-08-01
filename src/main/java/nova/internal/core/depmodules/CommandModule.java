package nova.internal.core.depmodules;

import nova.core.command.CommandManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class CommandModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(CommandManager.class).toConstructor();
	}

}
