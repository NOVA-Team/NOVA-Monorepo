package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.test.mod.TestMod;
import nova.test.mod.NonAnnotatedTestMod;
import nova.test.mod.NoLoadableTestMod;
import org.assertj.core.data.MapEntry;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NovaLauncherTest {

	@Mock
	DependencyInjectionEntryPoint diepMock;

	Set<Class<?>> testModClasses = Sets.newLinkedHashSet(
		TestMod.class,
		NonAnnotatedTestMod.class,
		NoLoadableTestMod.class
	);

	@Test
	public void testLaunchingDoesNotCrash() {
		DependencyInjectionEntryPoint diepSpy = spy(DependencyInjectionEntryPoint.class);

		NovaLauncher launcher = new NovaLauncher(diepMock, testModClasses);

		assertThat(launcher.getModClasses()).hasSize(1).containsValue(TestMod.class);

		launcher.generateDependencies();
		launcher.preInit();
		launcher.init();
		launcher.postInit();
	}
}