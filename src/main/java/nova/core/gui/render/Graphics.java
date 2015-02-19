package nova.core.gui.render;

import nova.core.render.Color;
import nova.core.render.texture.Texture;
import nova.core.util.transform.Vector2i;

/**
 * 
 * @author Vic Nightfall
 */
public class Graphics implements TextRenderer {

	// TODO Test me!

	private final TextRenderer textRenderer;
	private final Canvas canvas;

	private int linewidth;

	public Graphics(Canvas canvas, TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
		this.canvas = canvas;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setColor(Color color) {
		canvas.setColor(color);
	}

	public void bindTexture(Texture texture) {
		canvas.bindTexture(texture);
	}

	public Color getColor() {
		return canvas.getColor();
	}

	public int getLineWidth() {
		return linewidth;
	}

	public void setLineWidth(int linewidth) {
		this.linewidth = linewidth;
	}

	public void drawLine(int x, int y, int x2, int y2) {
		int g = Math.abs(y2 - y);
		int a = Math.abs(x2 - x);
		double angle = Math.atan(g / (double) a);
		int size = linewidth / 2;
		int ox = (int) (Math.sin(angle) * size);
		int oy = (int) (Math.cos(angle) * size);
		canvas.startDrawing();
		canvas.addVertex(x + ox, y + oy);
		canvas.addVertex(x - ox, y - oy);
		canvas.addVertex(x2 - ox, y2 - oy);
		canvas.addVertex(x2 + ox, y2 + oy);
		canvas.draw();
	}

	public void drawRect(int x, int y, int width, int height) {
		fillRect(x, y - linewidth / 2, width, linewidth);
		fillRect(x + height - linewidth / 2, y, width, linewidth);
		fillRect(x - linewidth / 2, y, linewidth, height);
		fillRect(x + width - linewidth / 2, y, linewidth, height);
	}
	
	public void fillRect(int x, int y, int width, int height) {
		canvas.startDrawing();
		canvas.addVertex(x, y);
		canvas.addVertex(x + width, y);
		canvas.addVertex(x + width, y + width);
		canvas.addVertex(x, y + width);
		canvas.draw();
	}

	public void fillRect(int x, int y, int width, int height, Color c1, Color c2) {
		canvas.startDrawing();
		canvas.setColor(c1);
		canvas.addVertex(x, y);
		canvas.addVertex(x + width, y);
		canvas.setColor(c2);
		canvas.addVertex(x + width, y + width);
		canvas.addVertex(x, y + width);
		canvas.draw();
	}

	public void fillRect(int x, int y, int width, int height, Color c1, Color c2, Color c3, Color c4) {
		canvas.startDrawing();
		canvas.setColor(c1);
		canvas.addVertex(x, y);
		canvas.setColor(c3);
		canvas.addVertex(x + width, y);
		canvas.setColor(c2);
		canvas.addVertex(x + width, y + width);
		canvas.setColor(c4);
		canvas.addVertex(x, y + width);
		canvas.draw();
	}

	public void drawShape(Shape2D shape) {
		Vertex2D[] vertices = shape.vertices();
		for (int i = 0; i < shape.size(); i++) {
			Vertex2D v1 = vertices[i];
			Vertex2D v2 = vertices[(i + 1) % shape.size()];
			drawLine(v1.x, v1.y, v2.x, v2.y);
		}
	}

	public void fillShape(Shape2D shape) {
		canvas.startDrawing();
		for (Vertex2D vertex : shape.vertices()) {
			canvas.addVertex(vertex);
		}
		canvas.draw();
	}

	public void drawTexture(int x, int y, int width, int height, Texture texture) {
		canvas.bindTexture(texture);
		canvas.startDrawing();
		canvas.addVertexWithUV(x, y, 0, 0);
		canvas.addVertexWithUV(x + width, y, 1, 0);
		canvas.addVertexWithUV(x + width, y + width, 1, 1);
		canvas.addVertexWithUV(x, y + width, 0, 1);
		canvas.draw();
	}

	// TODO Icons? A way to draw parts of a texture?

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
	public Vector2i getBounds(FormattedText text) {
		return textRenderer.getBounds(text);
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
