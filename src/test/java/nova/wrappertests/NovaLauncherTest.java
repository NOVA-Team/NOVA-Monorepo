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
		assertThat(game.blocks()).isNotNull();
		assertThat(game.entities()).isNotNull();
		assertThat(game.items()).isNotNull();
		assertThat(game.fluids()).isNotNull();
		assertThat(game.worlds()).isNotNull();
		assertThat(game.render()).isNotNull();
		assertThat(game.recipes()).isNotNull();
		assertThat(game.craftingRecipes()).isNotNull();
		assertThat(game.itemDictionary()).isNotNull();
		assertThat(game.events()).isNotNull();
		assertThat(game.network()).isNotNull();
		assertThat(game.retention()).isNotNull();
		assertThat(game.language()).isNotNull();
		assertThat(game.input()).isNotNull();
		assertThat(game.natives()).isNotNull();
		assertThat(game.info()).isNotNull();
	}
}
