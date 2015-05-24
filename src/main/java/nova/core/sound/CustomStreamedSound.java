package nova.core.sound;

/** A sound from which the data comes from the server **/
// TODO
public class CustomStreamedSound extends StreamedSound {
	// TODO: add a way to stream a sound. Raw data from the server, and also a way to allow mods to add their own code to handle the data to convert it into a format that the game can output.
	public CustomStreamedSound(boolean downloadFirst, float pitchModification, float speedModification, float volumeModification) {
		super(downloadFirst, pitchModification, speedModification, volumeModification);
	}

	@Override
	public String getID() {
		return null;
	}
}
