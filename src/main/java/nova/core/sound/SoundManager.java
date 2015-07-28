package nova.core.sound;

import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;

//TODO: Sound factories should use the same factory patterns.
public class SoundManager extends Manager<Sound, SoundFactory> {
	public SoundManager(Registry<SoundFactory> registry) {
		super(registry);
	}

	@Override
	public SoundFactory register(Function<Object[], Sound> constructor) {
		return register(new SoundFactory(constructor));
	}

}
