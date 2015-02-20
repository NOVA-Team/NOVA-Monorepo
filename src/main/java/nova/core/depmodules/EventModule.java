package nova.core.depmodules;

import nova.core.event.EventManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class EventModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(EventManager.class).toConstructor();
	}

}
