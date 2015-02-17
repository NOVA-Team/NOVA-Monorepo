package nova.core.gui.render;

import nova.core.util.transform.Vector2i;

public class Graphics implements TextRenderer {

	private final TextRenderer textRenderer;
	private final Canvas canvas;

	public Graphics(Canvas canvas, TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
		this.canvas = canvas;
	}

	public void drawRect(int x, int y, int width, int height, Color color) {

	}

	// TODO Add a bunch of 2D drawing methods for textures, for everything. Help
	// would be appreciated...

	// Convenience Methods to bridge to the underlying TextRenderer

	@Override
	public void drawString(int x, int y, FormattedText str) {
		textRenderer.drawString(x, y, str);
	}

	@Override
	public void drawString(int x, int y, FormattedText str, int width) {
		textRenderer.drawString(x, y, str, width);
	}

	@Override
	public void drawCutString(int x, int y, FormattedText str, int width) {
		textRenderer.drawCutString(x, y, str, width);
	}

	@Override
	public void drawString(int x, int y, String str) {
		textRenderer.drawString(x, y, str);
	}

	@Override
	public void drawString(int x, int y, String str, int width) {
		textRenderer.drawString(x, y, str, width);
	}

	@Override
	public void drawCutString(int x, int y, String str, int width) {
		textRenderer.drawCutString(x, y, str, width);
	}

	@Override
	public Vector2i getBounds(String str) {
		return textRenderer.getBounds(str);
	}

	@Override
	public void setZIndex(int zIndex) {
		textRenderer.setZIndex(zIndex);
		canvas.setZIndex(zIndex);
	}
}
