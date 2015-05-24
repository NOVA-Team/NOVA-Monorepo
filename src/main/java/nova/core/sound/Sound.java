package nova.core.sound;

import nova.core.util.Identifiable;

/**
 * An object representing a sound. (including the modification to pitch and volume, etc...)
 */
public abstract class Sound implements Identifiable {
	public final float pitchModification;
	public final float speedModification;
	public final float volumeModification;

	public Sound(float pitchModification, float speedModification, float volumeModification) {
		this.pitchModification = pitchModification;
		this.speedModification = speedModification;
		this.volumeModification = volumeModification;
	}
}
