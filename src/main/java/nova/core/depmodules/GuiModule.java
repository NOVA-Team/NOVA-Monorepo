package nova.core.depmodules;

import nova.core.gui.factory.GuiComponentFactory;
import se.jbee.inject.bind.BinderModule;

class GuiModule extends BinderModule {

	@Override
	protected void declare() {
		//require(GuiComponentFactory.class);
	}

}
