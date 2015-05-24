package nova.core.sound;

/** Sound without a resource on the client **/
public abstract class StreamedSound extends Sound {
	public final boolean downloadFirst;

	public StreamedSound(boolean downloadFirst, float pitchModification, float speedModification, float volumeModification) {
		super(pitchModification, speedModification, volumeModification);
		this.downloadFirst = downloadFirst;
	}
}
