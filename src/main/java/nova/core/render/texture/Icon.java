package nova.core.render.texture;

import nova.core.util.transform.vector.Vector2d;

/**
 * An Icon defines a sub-area of a {@link Texture}.
 * 
 * @author Vic Nightfall
 */
public class Icon {

	protected Texture texture;
	protected Vector2d minUV;
	protected Vector2d maxUV;

	/** Package private constructor for Texture **/
	Icon() {
	}

	public Icon(Texture texture, Vector2d minUV, Vector2d maxUV) {
		this.texture = texture;
		this.minUV = minUV;
		this.maxUV = maxUV;
	}

	public Texture texture() {
		return texture;
	}

	public Vector2d minUV() {
		return minUV;
	}

	public Vector2d maxUV() {
		return maxUV;
	}
}
