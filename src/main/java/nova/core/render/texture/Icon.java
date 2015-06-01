package nova.core.render.texture;

import nova.core.util.transform.vector.Vector2d;

/**
 * An Icon defines a sub-area of a {@link Texture}.
 * 
 * @author Vic Nightfall
 */
public class Icon {

	protected Texture texture;
	protected Vector2d uv;
	protected Vector2d dimension;

	/** Package private constructor for Texture **/
	Icon() {
	}

	public Icon(Texture texture, Vector2d uv, Vector2d dimension) {
		this.texture = texture;
		this.uv = uv;
		this.dimension = dimension;
	}

	public Texture texture() {
		return texture;
	}

	public Vector2d uv() {
		return uv;
	}

	public Vector2d dimension() {
		return dimension;
	}
}
