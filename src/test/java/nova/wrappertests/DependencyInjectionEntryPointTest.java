package nova.wrappertests;

import static org.assertj.core.api.Assertions.assertThat;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;

import org.junit.Test;

public class DependencyInjectionEntryPointTest {

	@Test
	public void startingInjectionShouldNotCrash() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		Game game = diep.init();

		assertThat(game).isNotNull();
		assertThat(game.blockManager).isNotNull();
		assertThat(game.entityManager).isNotNull();
		assertThat(game.itemManager).isNotNull();
		assertThat(game.fluidManager).isNotNull();
		assertThat(game.worldManager).isNotNull();
		assertThat(game.renderManager).isNotNull();
		assertThat(game.recipeManager).isNotNull();
		assertThat(game.craftingRecipeManager).isNotNull();
		assertThat(game.itemDictionary).isNotNull();
	}

}
