package nova.wrappertests.depmodules;

import nova.core.game.GameInfo;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeGameInfoModule extends BinderModule {

	@Override
	protected void declare() {
		bind(GameInfo.class).toSupplier(GameInfoSupplier.class);
	}

	public static class GameInfoSupplier implements Supplier<GameInfo> {
		@Override
		public GameInfo supply(Dependency<? super GameInfo> dependency, Injector injector) {
			return new GameInfo("test", "1.0.0");
		}
	}
}
