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
	public float pitch = 1;
	/**
	 * Changes the speed of the sound.
	 * This also changes the pitch of the sound, and so the pitch change should be compensated with pitchModification.
	 */
	public float speed = 1;
	/**
	 * Changes the volume of the sound.
	 * Default is 1 for no modification.
	 */
	public float volume = 1;

	public Sound(String domain, String name) {
		super(domain, name);
	}

	public Sound withPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public Sound withSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public Sound withVolume(float volume) {
		this.volume = volume;
		return this;
	}
}
