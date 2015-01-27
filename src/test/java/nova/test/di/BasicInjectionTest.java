package nova.test.di;

import java.util.Optional;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BasicInjectionTest {

	@Test
	public void starting_injection_should_not_crash(){
		DependencyInjectionEntryPoint ep = new DependencyInjectionEntryPoint();
		ep.preInit();
		ep.init(Optional.empty());
		Game game = ep.postInit(Optional.empty());
		assertThat(game, notNullValue());
	}
	
}
