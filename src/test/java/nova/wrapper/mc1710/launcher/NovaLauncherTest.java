package nova.wrapper.mc1710.launcher;

import nova.wrapper.mc1710.depmodules.*;
import nova.wrappertests.depmodules.FakeNetworkModule;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.List;

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
			TickerModule.class
		);
	}
}
