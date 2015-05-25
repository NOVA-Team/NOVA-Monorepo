package nova.wrapper.mc1710.depmodules;

import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiManager;
import nova.wrapper.mc1710.backward.gui.MCGuiComponentFactory;
import nova.wrapper.mc1710.backward.gui.MCGuiFactory;
import se.jbee.inject.bind.BinderModule;

public class GuiModule extends BinderModule {

	@Override
	protected void declare() {
		bind(GuiComponentFactory.class).to(MCGuiComponentFactory.class);
		bind(GuiManager.class).to(MCGuiFactory.class);
	}

}
