package nova.core.sound;

import nova.core.util.Factory;

import java.util.function.Function;

public class SoundFactory extends Factory<Sound> {
	public SoundFactory(Function<Object[], Sound> constructor) {
		super(constructor);
	}
}