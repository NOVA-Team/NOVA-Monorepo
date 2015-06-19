package nova.wrapper.mc18.launcher;

import nova.internal.core.Game;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.wrapper.mc18.depmodules.*;
import nova.wrappertests.depmodules.FakeNetworkModule;
import org.junit.Test;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rx14
 */
public class NovaLauncherTest extends nova.wrappertests.NovaLauncherTest {

	@Override
	public List<Class<? extends Bundle>> getModules() {
		return Arrays.<Class<? extends Bundle>>asList(
			ClientModule.class,
			GuiModule.class,
			KeyModule.class,
			LanguageModule.class,
			FakeNetworkModule.class, //NetworkManager calls into FML code in the class instantiation, so we create a fake.
			RenderModule.class,
			SaveModule.class,
			TickerModule.class,
			GameInfoModule.class
		);
	}

	@Override
	@Test
	public void testLaunching() {
		doLaunchAssert(createLauncher());
	}

	@Override
	@Test
	public void testResolveGame() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		Game game = diep.init();

		assertThat(game).isNotNull();
	}
}
