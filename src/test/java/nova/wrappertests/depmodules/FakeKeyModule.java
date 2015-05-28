package nova.wrappertests.depmodules;

import nova.core.gui.KeyManager;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeKeyModule extends BinderModule {

	@Override
	protected void declare() {
		bind(KeyManager.class).to(FakeKeyManager.class);
	}

	public static class FakeKeyManager extends KeyManager {
		@Override
		public boolean isKeyDown(Key key) {
			return false;
		}
	}

}
