package nova.core.gui.render.text;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Generic interface for a TextRenderer. A TextRenderer can draw text to the
 * screen, in the form of {@link FormattedText} or arbitrary text.
 * @author Vic Nightfall
 */
public interface TextRenderer extends TextMetrics {

	public default void drawCenteredString(int x, int y, FormattedText text, Vector2D dim) {
		org.apache.commons.math3.geometry.euclidean.twod.Vector2D tdim = getBounds(text);
		drawString((int) (dim.getX() / 2 - tdim.getX() / 2), (int) (dim.getY() / 2 - tdim.getY() / 2), text);
	}

	public default void drawCenteredString(int x, int y, String text, Vector2D dim) {
		org.apache.commons.math3.geometry.euclidean.twod.Vector2D tdim = getBounds(text);
		drawString((int) (dim.getX() / 2 - tdim.getX() / 2), (int) (dim.getY() / 2 - tdim.getY() / 2), text);
	}

	/**
	 * Renders {@link FormattedText} to the screen. Performs a line wrap at
	 * {@code %n} and {@code \n}.
	 * @param x x position
	 * @param y y position
	 * @param str FormattedText to render
	 * @see TextRenderer#drawString(int, int, FormattedText, int)
	 * @see TextRenderer#drawCutString(int, int, FormattedText, int)
	 */
	public default void drawString(int x, int y, FormattedText str) {
		cacheString(str).draw(x, y, this);
	}

	/**
	 * Renders {@link FormattedText} to the screen. Performs a line wrap at
	 * {@code %n} and {@code \n}. It also jumps to a new line when the text is
	 * longer than the provided width, wraps per word. <b>Any repeating
	 * whitespace will not be taken into account!</b>
	 * @param x x position
	 * @param y y position
	 * @param str FormattedText to render
	 * @param width size on which to wrap the text
	 */
	public default void drawString(int x, int y, FormattedText str, int width) {
		cacheString(str, width).draw(x, y, this);
	}

	/**
	 * Renders {@link FormattedText} to the screen. It will fit the text into
	 * the desired width and fills the center with "{@code ...}
	 * ". An example would be: "{@code Donaudampfschiff}" - "
	 * {@code Donau...chiff}". The wrapping substrings should have the same
	 * amount of characters. This should only be used on small text.
	 * @param x x position
	 * @param y y position
	 * @param str FormattedText to render
	 * @param width size on which to cut the text
	 */
	public default void drawCutString(int x, int y, FormattedText str, int width) {
		cacheCutString(str, width).draw(x, y, this);
	}

	/**
	 * Works in the same way as {@link #drawString(int, int, FormattedText)}
	 * except that it takes a String as argument. Keep in mind that the wrapper
	 * might apply unwanted formatting to the provided text, so using
	 * {@link FormattedText} is the <i>safe</i> way.
	 * @param x x position
	 * @param y y position
	 * @param str Text to render
	 * @see #drawString(int, int, FormattedText)
	 */
	public void drawString(int x, int y, String str);

	/**
	 * Works in the same way as
	 * {@link #drawString(int, int, FormattedText, int)} except that it takes a
	 * String as argument. Keep in mind that the wrapper might apply unwanted
	 * formatting to the provided text, so using {@link FormattedText} is the
	 * <i>safe</i> way. <b>Any repeating whitespace will not be taken into
	 * account!</b>
	 * @param x x position
	 * @param y y position
	 * @param str Text to render
	 * @param width size on which to wrap the text
	 * @see #drawString(int, int, FormattedText, int)
	 */
	public void drawString(int x, int y, String str, int width);

	/**
	 * Works in the same way as
	 * {@link #drawCutString(int, int, FormattedText, int)} except that it takes
	 * a String as argument. Keep in mind that the wrapper might apply unwanted
	 * formatting to the provided text, so using {@link FormattedText} is the
	 * <i>safe</i> way.
	 * @param x x position
	 * @param y y position
	 * @param str Text to render
	 * @param width size on which to wrap the text
	 * @see #drawCutString(int, int, FormattedText, int)
	 */
	public void drawCutString(int x, int y, String str, int width);

	/**
	 * Sets the z index on which to render the desired text. This will affect
	 * the draw order, shapes with a higher z index will draw first. If you
	 * leave the z index untouched, the shapes will draw in the order they were
	 * queried, so what gets drawn last will be topmost.
	 * @param zIndex z index
	 */
	public void setZIndex(int zIndex);

	/**
	 * Interface to provide information for cached {@link FormattedText}.
	 * @author Vic Nightfall
	 */
	public static interface RenderedText {

		public org.apache.commons.math3.geometry.euclidean.twod.Vector2D getDimensions();

		public void draw(int x, int y, TextRenderer renderer);
	}
}
