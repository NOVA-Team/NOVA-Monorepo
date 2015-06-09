package nova.wrappertests;

import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

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
	}
}
