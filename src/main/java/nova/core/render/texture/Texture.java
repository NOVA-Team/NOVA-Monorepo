package nova.core.render.texture;

/**
 * A texture has a file location.
 * All texture must be included in /assets/domain/textures/*
 *
 * @author Calclavia
 */
public class Texture {

	public final String domain;
	public final String resource;
	//An integer representing the rotation of this texture
	public final int rotation = 0;

	public Texture(String domain, String resource) {
		this.domain = domain;
		this.resource = resource;
	}

	public String getResource() {
		return domain + ":" + resource;
	}
}
