package nova.core.sound;

/**
 * a sound object with a resource on the client.
 */
public class ResourceSound extends Sound {
	public final String domain;
	public final String resource;

	public ResourceSound(String domain, String resource, float pitchModification, float speedModification, float volumeModification) {
		super(pitchModification, speedModification, volumeModification);
		this.domain = domain;
		this.resource = resource;
	}

	@Override
	public String getID() {
		return domain + ":" + resource;
	}
}
