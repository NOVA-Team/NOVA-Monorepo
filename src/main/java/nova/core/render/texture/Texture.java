package nova.core.render.texture;

import nova.core.game.Game;
import nova.core.util.transform.vector.Vector2i;

/**
 * A texture has a file location. All texture must be included in
 * /assets/domain/textures/*
 * @author Calclavia
 */
public class Texture {

	public final String domain;
	public final String resource;
	public final Vector2i dimension;

	@SuppressWarnings("deprecation")
	public Texture(String domain, String resource) {
		this.domain = domain;
		this.resource = resource;
		this.dimension = Game.instance().renderManager().getDimension(this);
	}

	public String getResource() {
		return domain + ":" + resource;
	}

	public String getPath() {
		return resource + ".png";
	}
}
