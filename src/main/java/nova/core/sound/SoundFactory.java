package nova.core.sound;

import nova.core.util.Factory;

import java.util.function.Function;
import java.util.function.Supplier;

public class SoundFactory extends Factory<Sound> {

	/**
	 * Changes the pitch of the sound.
	 * This does not change the speed of the sound so it can it be used to compensate for the speed of the sound changing to pitch.
	 */
	public final float pitch;
	/**
	 * Changes the speed of the sound.
	 * This also changes the pitch of the sound, and so the pitch change should be compensated with pitchModification.
	 */
	public final float speed;
	/**
	 * Changes the volume of the sound.
	 * Default is 1 for no modification.
	 */
	public final float volume;

	public SoundFactory(Function<Object[], Sound> constructor) {
		this(constructor, 1, 1, 1);
	}

	public SoundFactory(Function<Object[], Sound> constructor, float pitch, float speed, float volume) {
		super(constructor);
		this.pitch = pitch;
		this.speed = speed;
		this.volume = volume;
	}

	public SoundFactory withPitch(float pitch) {
		return new SoundFactory(constructor, pitch, speed, volume);
	}

	public SoundFactory withSpeed(float speed) {
		return new SoundFactory(constructor, pitch, speed, volume);
	}

	public SoundFactory withVolume(float volume) {
		return new SoundFactory(constructor, pitch, speed, volume);
	}

	@Override
	public Sound build(Object... args) {
		Sound build = super.build(args);
		build.pitch = pitch;
		build.volume = volume;
		build.speed = speed;
		return build;
	}
}