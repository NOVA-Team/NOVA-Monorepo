package nova.wrappertests;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.block.Block;
import nova.core.game.Game;
import nova.internal.launch.NovaLauncher;
import nova.testutils.FakeBlock;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import nova.wrappertests.depmodules.FakeClientModule;
import nova.wrappertests.depmodules.FakeGuiModule;
import nova.wrappertests.depmodules.FakeKeyModule;
import nova.wrappertests.depmodules.FakeLanguageModule;
import nova.wrappertests.depmodules.FakeNetworkModule;
import nova.wrappertests.depmodules.FakeRenderModule;
import nova.wrappertests.depmodules.FakeSaveModule;
import nova.wrappertests.depmodules.FakeTickerModule;
import org.assertj.core.util.Sets;
import org.junit.Test;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NovaLauncherTest {

	public Set<Class<?>> getTestModClasses() {
		return Sets.newLinkedHashSet(
			TestMod.class,
			NonAnnotatedTestMod.class,
			NoLoadableTestMod.class
		);
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
			FakeTickerModule.class
		);
	}

	/**
	 * Creates a fake launcher to allow mods to unit test.
	 * @return
	 */
	public NovaLauncher createLauncher() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		NovaLauncher launcher = new NovaLauncher(diep, getTestModClasses());

		Game.setInstance(diep.init());

		/**
		 * Register fake air block
		 */
		Game.getInstance().blockManager().register((args) -> new FakeBlock("air") {
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

	@Test
	public void testLaunching() {
		doLaunchAssert(createLauncher());
	}

	public void doLaunchAssert(NovaLauncher launcher) {
		assertThat(launcher.getModClasses())
			.hasSize(1)
			.containsValue(TestMod.class);
	}

	@Test
	public void testResolveGame() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		Game game = diep.init();

		assertThat(game).isNotNull();

		assertThat(game.logger()).isNotNull();
		assertThat(game.clientManager()).isNotNull();
		assertThat(game.blockManager()).isNotNull();
		assertThat(game.entityManager()).isNotNull();
		assertThat(game.itemManager()).isNotNull();
		assertThat(game.fluidManager()).isNotNull();
		assertThat(game.worldManager()).isNotNull();
		assertThat(game.renderManager()).isNotNull();
		assertThat(game.recipeManager()).isNotNull();
		assertThat(game.craftingRecipeManager()).isNotNull();
		assertThat(game.itemDictionary()).isNotNull();
		assertThat(game.eventManager()).isNotNull();
		assertThat(game.networkManager()).isNotNull();
		assertThat(game.saveManager()).isNotNull();
		assertThat(game.languageManager()).isNotNull();
		assertThat(game.keyManager()).isNotNull();
		assertThat(game.nativeManager()).isNotNull();
	}
}
