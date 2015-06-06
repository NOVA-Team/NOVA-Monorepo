package nova.wrappertests;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.Game;
import nova.internal.launch.NovaLauncher;
import nova.testutils.FakeBlock;
import nova.wrappertests.depmodules.FakeClientModule;
import nova.wrappertests.depmodules.FakeGameInfoModule;
import nova.wrappertests.depmodules.FakeGuiModule;
import nova.wrappertests.depmodules.FakeKeyModule;
import nova.wrappertests.depmodules.FakeLanguageModule;
import nova.wrappertests.depmodules.FakeNetworkModule;
import nova.wrappertests.depmodules.FakeRenderModule;
import nova.wrappertests.depmodules.FakeSaveModule;
import nova.wrappertests.depmodules.FakeTickerModule;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author Calclavia
 */
public class NovaLauncherTestFactory {

	public final Class<?>[] modClasses;

	public NovaLauncherTestFactory(Class<?>... modClasses) {
		this.modClasses = modClasses;
	}

	public List<Class<? extends Bundle>> getModules() {
		return Arrays.<Class<? extends Bundle>>asList(
			FakeClientModule.class,
			FakeGuiModule.class,
			FakeKeyModule.class,
			FakeLanguageModule.class,
			FakeNetworkModule.class, //NetworkManager calls into FML code in the class instantiation, so we create a fake.
			FakeRenderModule.class,
			FakeSaveModule.class,
			FakeTickerModule.class,
			FakeGameInfoModule.class
		);
	}

	/**
	 * Creates a fake launcher to allow mods to unit test.
	 * @return
	 */
	public NovaLauncher createLauncher() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		NovaLauncher launcher = new NovaLauncher(diep, new HashSet<>(Arrays.asList(modClasses)));

		Game.inject(diep.init());

		/**
		 * Register fake air block
		 */
		Game.blocks().register((args) -> new FakeBlock("air") {
			@Override
			public void onRegister() {

			}
		});

		launcher.generateDependencies();
		launcher.load();
		launcher.preInit();
		launcher.init();
		launcher.postInit();
		return launcher;
	}

}
