package nova.core.sound;

import java.net.URL;

/**
 * A sound that has a URL that the client can download / stream from.
 */
public class URLSound extends StreamedSound {
	public final URL location;

	public URLSound(URL location, boolean downloadFirst, float pitchModification, float speedModification, float volumeModification) {
		super(downloadFirst, pitchModification, speedModification, volumeModification);
		this.location = location;
	}

	public URLSound(URL location, boolean downloadFirst) {
		super(downloadFirst);
		this.location = location;
	}

	public URLSound(URL location) {
		super();
		this.location = location;
	}

	@Override
	public String toString() {
		return location.toString();
	}
}
