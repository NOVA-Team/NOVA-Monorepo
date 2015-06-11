package nova.core.sound;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;

public class SoundManager extends Manager<Sound, Factory<Sound>> {
	public SoundManager(Registry<Factory<Sound>> registry) {
		super(registry);
	}

	@Override
	public Factory<Sound> register(Function<Object[], Sound> constructor) {
		return register(new Factory<Sound>(constructor));
	}
	
}
