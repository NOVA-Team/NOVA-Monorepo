package nova.bootstrap;

import nova.core.game.Game;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyInjectionEntryPointTest {

	@Test
	public void startingInjectionShouldNotCrash() {
		DependencyInjectionEntryPoint ep = new DependencyInjectionEntryPoint();

		Game game = ep.init();

		assertThat(game).isNotNull();
		assertThat(game.blockManager).isNotNull();
		assertThat(game.entityManager).isNotNull();
		assertThat(game.itemManager).isNotNull();
		assertThat(game.fluidManager).isNotNull();
		assertThat(game.worldManager).isNotNull();
		assertThat(game.renderManager).isNotNull();
		assertThat(game.recipeManager).isNotNull();
		assertThat(game.craftingRecipeManager).isNotNull();
		assertThat(game.oreDictionary).isNotNull();
	}

}
