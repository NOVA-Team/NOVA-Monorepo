package nova.core.gui.render;

import nova.core.util.transform.Vector2i;

/**
 * Generic interface for a TextRenderer. A TextRenderer can draw text to the
 * screen, in the form of {@link FormattedText} or arbitrary text.
 * 
 * @author Vic Nightfall
 */
public interface TextRenderer {

	public void drawString(int x, int y, FormattedText str);

	public void drawString(int x, int y, FormattedText str, int width);

	public void drawCutString(int x, int y, FormattedText str, int width);

	public void drawString(int x, int y, String str);

	public void drawString(int x, int y, String str, int width);

	public void drawCutString(int x, int y, String str, int width);

	public void setZIndex(int zIndex);

	public Vector2i getBounds(String str);
}
