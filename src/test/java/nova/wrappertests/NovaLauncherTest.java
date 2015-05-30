package nova.wrappertests;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.internal.launch.NovaLauncher;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NovaLauncherTest extends NovaLauncherTestFactory {

	public NovaLauncherTest() {
		super(TestMod.class, NonAnnotatedTestMod.class, NoLoadableTestMod.class);
	}

	@Test
	public void testLaunching() {
		doLaunchAssert(createLauncher());
	}

	public void doLaunchAssert(NovaLauncher launcher) {
		assertThat(launcher.getModClasses())
			.hasSize(1)
			.containsValues(TestMod.class);
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
