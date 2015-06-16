package nova.core.sound;

/**
 * a sound object withPriority a resource on the client.
 * @author skyem123
 */
public class ResourceSound extends Sound {
	public final String domain;
	public final String resource;

	public ResourceSound(String domain, String resource, float pitchModification, float speedModification, float volumeModification) {
		super(pitchModification, speedModification, volumeModification);
		this.domain = domain;
		this.resource = resource;
	}
	public ResourceSound(String domain, String resource) {
		super();
		this.domain = domain;
		this.resource = resource;
	}

	@Override
	public String toString() {
		return domain + ":" + resource;
	}
}
