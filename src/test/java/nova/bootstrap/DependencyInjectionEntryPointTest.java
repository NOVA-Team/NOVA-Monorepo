package nova.bootstrap;

import nova.core.game.Game;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DependencyInjectionEntryPointTest {

	@Test
	public void starting_injection_should_not_crash(){
		DependencyInjectionEntryPoint ep = new DependencyInjectionEntryPoint();

		ep.preInit();
		ep.init();
		Game game = ep.postInit();

		assertThat(game).isNotNull();
		assertThat(game.blockManager).isNotNull();
	}
	
}
