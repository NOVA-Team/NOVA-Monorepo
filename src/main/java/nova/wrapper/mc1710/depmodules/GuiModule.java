package nova.wrapper.mc1710.depmodules;

import nova.core.gui.factory.GuiComponentFactory;
import nova.wrapper.mc1710.backward.gui.MCGuiComponentFactory;
import se.jbee.inject.bind.BinderModule;

public class GuiModule extends BinderModule {

	@Override
	protected void declare() {
		bind(GuiComponentFactory.class).to(MCGuiComponentFactory.class);
	}

}
