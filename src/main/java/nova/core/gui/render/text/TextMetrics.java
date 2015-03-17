package nova.core.gui.render.text;

import nova.core.gui.render.text.TextRenderer.RenderedText;
import nova.core.util.transform.Vector2d;

/**
 * Provides methods for calculating the bounds of {@link FormattedText} and
 * arbitrary text.
 * 
 * @author Vic Nightfall
 */
public interface TextMetrics {

	public RenderedText cacheString(FormattedText str);

	public RenderedText cacheString(FormattedText str, int width);

	public RenderedText cacheCutString(FormattedText str, int width);

	/**
	 * Returns the computed boundaries of the provided {@link FormattedText}.
	 * 
	 * @param text FormattedText
	 * @return boundaries
	 */
	public default Vector2d getBounds(FormattedText text) {
		return cacheString(text).getDimensions();
	}

	/**
	 * Returns the computed boundaries of the provided String.
	 * 
	 * @param str String
	 * @return boundaries
	 */
	public Vector2d getBounds(String str);
}
