package nova.core.sound;

import nova.core.util.Factory;
import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;

public class SoundManager extends Manager<Sound, SoundManager.SoundFactory> {
	public SoundManager(Registry<SoundFactory> registry) {
		super(registry);
	}

	@Override
	public SoundFactory register(Function<Object[], Sound> constructor) {
		return register(new SoundFactory(constructor));
	}

	protected static class SoundFactory extends Factory<Sound> {
		public SoundFactory(Function<Object[], Sound> constructor) {
			super(constructor);
		}
	}
}
