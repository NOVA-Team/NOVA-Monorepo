package nova.core.sound;

/** Sound without a resource on the client **/
public abstract class StreamedSound extends Sound {
	/**
	 * Makes the client wait for the download to complete before playing the sound.
	 * If you pass it something that is endless (say a radio stream), there will be a download timeout that will stop it from continuing to download.
	 */
	public final boolean downloadFirst;

	public StreamedSound(boolean downloadFirst, float pitchModification, float speedModification, float volumeModification) {
		super(pitchModification, speedModification, volumeModification);
		this.downloadFirst = downloadFirst;
	}

	public StreamedSound(boolean downloadFirst) {
		super();
		this.downloadFirst = downloadFirst;
	}

	public StreamedSound() {
		this(false);
	}
}
