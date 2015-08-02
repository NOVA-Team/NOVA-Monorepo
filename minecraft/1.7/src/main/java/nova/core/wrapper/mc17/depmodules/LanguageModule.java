package nova.core.wrapper.mc17.depmodules;

import nova.core.util.LanguageManager;
import nova.core.wrapper.mc17.util.MCLanguageManager;
import se.jbee.inject.bind.BinderModule;

public class LanguageModule extends BinderModule {

	@Override
	protected void declare() {
		bind(LanguageManager.class).to(MCLanguageManager.class);
	}

}
