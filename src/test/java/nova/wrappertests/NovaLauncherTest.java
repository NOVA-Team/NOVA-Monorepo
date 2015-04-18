package nova.wrappertests;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.internal.NovaLauncher;
import nova.testutils.mod.NoLoadableTestMod;
import nova.testutils.mod.NonAnnotatedTestMod;
import nova.testutils.mod.TestMod;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.slf4j.Logger;
import se.jbee.inject.bootstrap.Bundle;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class NovaLauncherTest {

	Set<Class<?>> testModClasses = Sets.newLinkedHashSet(
		TestMod.class,
		NonAnnotatedTestMod.class,
		NoLoadableTestMod.class
	);

	abstract List<Class<? extends Bundle>> getModules();

	@Test
	public void testLaunching() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		NovaLauncher launcher = new NovaLauncher(diep, testModClasses);

		assertThat(launcher.getModClasses())
			.hasSize(1)
			.containsValue(TestMod.class);

		launcher.generateDependencies();
		launcher.preInit();
		launcher.init();
		launcher.postInit();
	}

	@Test
	public void testResolveGame() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();

		getModules().forEach(diep::install);

		Game game = diep.init();

		assertThat(game).isNotNull();

		assertThat(game.logger).isNotNull();
		assertThat(game.clientManager).isNotNull();
		assertThat(game.blockManager).isNotNull();
		assertThat(game.entityManager).isNotNull();
		assertThat(game.itemManager).isNotNull();
		assertThat(game.fluidManager).isNotNull();
		assertThat(game.worldManager).isNotNull();
		assertThat(game.renderManager).isNotNull();
		assertThat(game.recipeManager).isNotNull();
		assertThat(game.craftingRecipeManager).isNotNull();
		assertThat(game.itemDictionary).isNotNull();
		assertThat(game.eventManager).isNotNull();
		assertThat(game.networkManager).isNotNull();
		assertThat(game.saveManager).isNotNull();
		assertThat(game.languageManager).isNotNull();
		assertThat(game.keyManager).isNotNull();
		assertThat(game.nativeManager).isNotNull();
	}
}
