package nova.wrapper.mc1710.launcher;

import nova.wrapper.mc1710.depmodules.ClientModule;
import nova.wrapper.mc1710.depmodules.FakeNetworkModule;
import nova.wrapper.mc1710.depmodules.GuiModule;
import nova.wrapper.mc1710.depmodules.KeyModule;
import nova.wrapper.mc1710.depmodules.LanguageModule;
import nova.wrapper.mc1710.depmodules.SaveModule;
import nova.wrapper.mc1710.depmodules.TickerModule;
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
			GuiModule.class,
			FakeNetworkModule.class, //NetworkManager calls into FML code in the class instantiation, so we create a fake.
			SaveModule.class,
			TickerModule.class,
			LanguageModule.class,
			KeyModule.class,
			ClientModule.class
		);
	}
}
