package nova.core.render.texture;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * An Icon defines a sub-area of a {@link Texture}.
 * 
 * @author Vic Nightfall
 */
public class Icon {

	protected Texture texture;
	protected Vector2D minUV;
	protected Vector2D maxUV;

	/** Package private constructor for Texture **/
	Icon() {
	}

	public Icon(Texture texture, Vector2D minUV, Vector2D maxUV) {
		this.texture = texture;
		this.minUV = minUV;
		this.maxUV = maxUV;
	}

	public Texture texture() {
		return texture;
	}

	public Vector2D minUV() {
		return minUV;
	}

	public Vector2D maxUV() {
		return maxUV;
	}
}
