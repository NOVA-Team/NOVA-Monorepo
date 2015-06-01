package nova.wrappertests.depmodules;

import nova.core.gui.InputManager;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeKeyModule extends BinderModule {

	@Override
	protected void declare() {
		bind(InputManager.class).to(FakeInputManager.class);
	}

	public static class FakeInputManager extends InputManager {
		@Override
		public boolean isKeyDown(Key key) {
			return false;
		}
	}

}
