package nova.core.gui.render;

import nova.core.util.transform.Vector2i;

/**
 * Provides methods for calculating the bounds of {@link FormattedText} and
 * arbitrary text.
 * 
 * @author Vic Nightfall
 */
public interface TextMetrics {

	/**
	 * Returns the computed boundaries of the provided {@link FormattedText}.
	 * 
	 * @param text FormattedText
	 * @return boundaries
	 */
	public Vector2i getBounds(FormattedText text);

	/**
	 * Returns the computed boundaries of the provided String.
	 * 
	 * @param str String
	 * @return boundaries
	 */
	public Vector2i getBounds(String str);
}
