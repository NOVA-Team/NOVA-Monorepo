package nova.wrappertests.depmodules;

import nova.core.retention.Storable;
import nova.core.util.RetentionManager;
import se.jbee.inject.bind.BinderModule;

import java.io.File;

/**
 * @author Calclavia
 */
public class FakeSaveModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RetentionManager.class).to(FakeRetentionManager.class);
	}

	public static class FakeRetentionManager extends RetentionManager {
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
