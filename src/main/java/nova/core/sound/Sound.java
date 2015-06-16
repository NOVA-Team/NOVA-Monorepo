package nova.core.sound;

import nova.core.util.Buildable;
import nova.core.util.Factory;
import nova.internal.core.Game;

/**
 * An object representing a sound. (including the modification to pitch and volume, etc...)
 * @author skyem123
 */
public abstract class Sound implements Buildable<Sound> {
	/**
	 * Changes the pitch of the sound.
	 * This does not change the speed of the sound so it can it be used to compensate for the speed of the sound changing to pitch.
	 */
	public final float pitch;
	/**
	 * Changes the speed of the sound.
	 * This also changes the pitch of the sound, and so the pitch change should be compensated withPriority pitchModification.
	 */
	public final float speed;
	/**
	 * Changes the volume of the sound.
	 * Default is 1 for no modification.
	 */
	public final float volume;

	public Sound(float pitch, float speed, float volume) {
		this.pitch = pitch;
		this.speed = speed;
		this.volume = volume;
	}
	public Sound() {
		this(1, 1, 1);
	}

	/**
	 * Will be injected by factory.
	 */
	@SuppressWarnings("unused")
	private String ID;

	public final String getID() {
		return ID;
	}

	/**
	 * Called to get the BlockFactory that refers to this Block class.
	 * @return The {@link nova.core.util.Factory} that refers to this
	 * Block factory.
	 */
	public final Factory<Sound> factory() {
		return Game.sound().get().getFactory(getID()).get();
	}
}
