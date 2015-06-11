package nova.wrapper.mc18.depmodules;

import nova.core.util.LanguageManager;
import nova.wrapper.mc18.util.MCLanguageManager;
import se.jbee.inject.bind.BinderModule;

public class LanguageModule extends BinderModule {

	@Override
	protected void declare() {
		bind(LanguageManager.class).to(MCLanguageManager.class);
	}

}
