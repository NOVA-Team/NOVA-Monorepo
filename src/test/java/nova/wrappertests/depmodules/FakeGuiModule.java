package nova.wrappertests.depmodules;

import nova.core.entity.Entity;
import nova.core.gui.Gui;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiManager;
import se.jbee.inject.bind.BinderModule;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class FakeGuiModule extends BinderModule {
	@Override
	protected void declare() {
		bind(GuiComponentFactory.class).to(FakeGuiComponentFactory.class);
		bind(GuiManager.class).to(FakeGuiManager.class);
	}

	public static class FakeGuiComponentFactory extends GuiComponentFactory {

	}

	public static class FakeGuiManager extends GuiManager {
		protected Optional<Gui> getActiveGuiImpl() {
			return null;
		}

		protected Optional<Gui> getActiveGuiImpl(Entity player) {
			return null;
		}
	}

}