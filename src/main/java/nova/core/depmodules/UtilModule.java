package nova.core.depmodules;

import nova.core.di.NovaScopes;
import nova.core.util.Dictionary;
import nova.core.util.Registry;
import nova.internal.Game;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class UtilModule extends BinderModule {

	public UtilModule() {
		super(NovaScopes.MULTIPLE_INSTANCES);
	}

	@Override
	protected void declare() {
		bind(Registry.class).toConstructor();
		bind(Dictionary.class).toConstructor();

		per(Scoped.APPLICATION).bind(Game.class).toConstructor();
	}

}
