package nova.wrappertests.depmodules;

import nova.core.retention.Storable;
import nova.core.util.SaveManager;
import se.jbee.inject.bind.BinderModule;

import java.io.File;

/**
 * @author Calclavia
 */
public class FakeSaveModule extends BinderModule {

	@Override
	protected void declare() {
		bind(SaveManager.class).to(FakeSaveManager.class);
	}

	public static class FakeSaveManager extends SaveManager {
		@Override
		public void save(String filename, Storable storable) {

		}

		@Override
		public void load(String filename, Storable storable) {

		}

		@Override
		public File getSaveDirectory() {
			return null;
		}
	}

}
