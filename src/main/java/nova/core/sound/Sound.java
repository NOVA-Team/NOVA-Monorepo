package nova.core.sound;

import nova.core.render.Asset;
import nova.core.util.Identifiable;

/**
 * An object representing a sound. (including the modification to pitch and volume, etc...)
 *
 * @author skyem123
 */
public class Sound extends Asset implements Identifiable {

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

	public Sound(String domain, String name) {
		this(domain, name, 1, 1, 1);
	}

	public Sound(String domain, String name, float pitch, float speed, float volume) {
		super(domain, name);
		this.pitch = pitch;
		this.speed = speed;
		this.volume = volume;
	}

	public Sound withPitch(float pitch) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withSpeed(float speed) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withVolume(float volume) {
		return new Sound(domain, name, pitch, speed, volume);
	}

}
