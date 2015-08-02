package nova.wrapper.mc18.depmodules;

import nova.core.game.GameInfo;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

public class GameInfoModule extends BinderModule {
	private static final GameInfo minecraft = new GameInfo("minecraft", "1.7.10");

	@Override
	protected void declare() {
		bind(GameInfo.class).toSupplier(GameInfoSupplier.class);
	}

	public static class GameInfoSupplier implements Supplier<GameInfo> {
		@Override
		public GameInfo supply(Dependency<? super GameInfo> dependency, Injector injector) {
			return minecraft;
		}
	}
}
