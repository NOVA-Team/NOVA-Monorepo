package nova.wrappertests;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.internal.NovaLauncher;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NovaLauncherTest {

	Set<Class<?>> testModClasses = Sets.newLinkedHashSet(
		TestMod.class,
		NonAnnotatedTestMod.class,
		NoLoadableTestMod.class
	);

	@Test
	public void testLaunchingDoesNotCrash() {
		NovaLauncher launcher = new NovaLauncher(new DependencyInjectionEntryPoint(), testModClasses);

		assertThat(launcher.getModClasses()).hasSize(1).containsValue(TestMod.class);

		launcher.generateDependencies();
		launcher.preInit();
		launcher.init();
		launcher.postInit();
	}
}