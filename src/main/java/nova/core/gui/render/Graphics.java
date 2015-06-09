package nova.core.gui.render;

import nova.core.gui.render.Shape2D.PolygonShape;
import nova.core.gui.render.text.FormattedText;
import nova.core.gui.render.text.TextRenderer;
import nova.core.render.Color;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * 
 * @author Vic Nightfall
 */
public class Graphics implements TextRenderer {

	// TODO Test me!

	private final TextRenderer textRenderer;
	private final Canvas canvas;

	private int linewidth = 1;

	public Graphics(Canvas canvas, TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
		this.canvas = canvas;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void bindTexture(Texture texture) {
		canvas.bindTexture(texture);
	}

	public Color getColor() {
		return canvas.getColor();
	}

	public void setColor(Color color) {
		canvas.setColor(color);
	}

	public int getLineWidth() {
		return linewidth;
	}

	public void setLineWidth(int linewidth) {
		this.linewidth = linewidth;
	}

	public void drawLine(double x, double y, double x2, double y2) {
		double g = y2 - y;
		double a = x2 - x;
		double angle = Math.atan(g / a);
		double size = linewidth / 2D;
		double ox = Math.sin(angle) * size;
		double oy = Math.cos(angle) * size;

		canvas.startDrawing(false);
		canvas.addVertex(x + ox, y - oy);
		canvas.addVertex(x - ox, y + oy);
		canvas.addVertex(x2 - ox, y2 + oy);
		canvas.addVertex(x2 + ox, y2 - oy);
		canvas.draw();
	}

	public void drawRect(double x, double y, double width, double height) {
		fillRect(x, y, width, linewidth);
		fillRect(x, y + height - linewidth, width, linewidth);
		fillRect(x, y, linewidth, height);
		fillRect(x + width - linewidth, y, linewidth, height);
	}

	public void fillRect(double x, double y, double width, double height) {
		canvas.startDrawing(false);
		canvas.addVertex(x, y);
		canvas.addVertex(x + width, y);
		canvas.addVertex(x + width, y + height);
		canvas.addVertex(x, y + height);
		canvas.draw();
	}

	public void fillRect(double x, double y, double width, double height, Color c1, Color c2) {
		canvas.startDrawing(false);
		canvas.setColor(c1);
		canvas.addVertex(x, y);
		canvas.addVertex(x + width, y);
		canvas.setColor(c2);
		canvas.addVertex(x + width, y + height);
		canvas.addVertex(x, y + height);
		canvas.draw();
	}

	public void fillRect(double x, double y, double width, double height, Color c1, Color c2, Color c3, Color c4) {
		canvas.startDrawing(false);
		canvas.setColor(c1);
		canvas.addVertex(x, y);
		canvas.setColor(c3);
		canvas.addVertex(x + width, y);
		canvas.setColor(c2);
		canvas.addVertex(x + width, y + height);
		canvas.setColor(c4);
		canvas.addVertex(x, y + height);
		canvas.draw();
	}

	public void fillEllipse(double x, double y, double width, double height) {
		double rX = width / 2D;
		double rY = height / 2D;
		canvas.startDrawing(false);
		for (int i = 0; i < 360; i++) {
			double rad = Math.toRadians(i);
			canvas.addVertex(Math.cos(rad) * rX + x, Math.sin(rad) * rY + y);
		}
		canvas.draw();
	}

	public void drawEllipse(double x, double y, double width, double height) {
		Vertex2D[] vertices = new Vertex2D[360];
		double rX = width / 2D;
		double rY = height / 2D;
		for (int i = 0; i < 360; i++) {
			double rad = Math.toRadians(i);
			vertices[i] = new Vertex2D(Math.cos(rad) * rX + x, Math.sin(rad) * rY + y);
		}
		drawShape(new PolygonShape(vertices));
	}

	public void drawPath(Shape2D shape) {
		// TODO
		throw new UnsupportedOperationException();
	}

	public void drawShape(Shape2D shape) {
		int shapeSize = shape.size();
		Vertex2D[] vertices = shape.vertices();
		Vertex2D[] outline = new Vertex2D[shapeSize * 4];

		for (int i = 0; i < shapeSize; i++) {
			Vertex2D v1 = vertices[i];
			Vertex2D v2 = vertices[(i + 1) % shapeSize];

			double g = v2.getY() - v1.getY();
			double a = v2.getX() - v1.getX();
			double angle = Math.atan(g / a);
			double size = linewidth / 2D;

			double ox = Math.sin(angle) * size;
			double oy = Math.cos(angle) * size;

			int j = i * 4;
			outline[j] = v1.offset(ox, -oy);
			outline[j + 1] = v1.offset(-ox, oy);
			outline[j + 2] = v2.offset(-ox, oy);
			outline[j + 3] = v2.offset(ox, -oy);
		}

		for (int i = 0; i < shapeSize; i++) {
			int j = i * 4;
			canvas.startDrawing(false);
			canvas.addVertex(outline[j]);
			canvas.addVertex(outline[j + 1]);
			canvas.addVertex(outline[j + 2]);
			canvas.addVertex(outline[j + 3]);
			canvas.draw();

			canvas.startDrawing(false);
			canvas.addVertex(outline[j + 2]);
			canvas.addVertex(outline[j + 3]);
			canvas.addVertex(outline[(j + 4) % (shapeSize * 4)]);
			canvas.addVertex(outline[(j + 5) % (shapeSize * 4)]);

			// Add the first two points again...
			// TODO There might be better ways
			canvas.addVertex(outline[j + 3]);
			canvas.addVertex(outline[j + 2]);
			canvas.draw();
		}
	}

	public void fillShape(Shape2D shape) {
		fillShape(shape, false);
	}

	public void fillShape(Shape2D shape, boolean textured) {
		canvas.startDrawing(textured);
		for (Vertex2D vertex : shape.vertices()) {
			canvas.addVertex(vertex);
		}
		canvas.draw();
	}

	public void drawTexture(double x, double y, double width, double height, Texture texture) {
		drawTexture(x, y, width, height, texture, Color.white);
	}

	public void drawTexture(double x, double y, double width, double height, Texture texture, Color color) {
		canvas.setColor(color);
		canvas.bindTexture(texture);
		canvas.startDrawing(true);
		canvas.addVertexWithUV(x, y, 0, 0);
		canvas.addVertexWithUV(x + width, y, 1, 0);
		canvas.addVertexWithUV(x + width, y + height, 1, 1);
		canvas.addVertexWithUV(x, y + height, 0, 1);
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
	public Vector2D getBounds(FormattedText text) {
		return textRenderer.getBounds(text);
	}

	@Override
	public Vector2D getBounds(String str) {
		return textRenderer.getBounds(str);
	}

	@Override
	public void setZIndex(int zIndex) {
		textRenderer.setZIndex(zIndex);
		canvas.setZIndex(zIndex);
	}

	@Override
	public RenderedText cacheString(FormattedText str) {
		return textRenderer.cacheString(str);
	}

	@Override
	public RenderedText cacheString(FormattedText str, int width) {
		return textRenderer.cacheString(str, width);
	}

	@Override
	public RenderedText cacheCutString(FormattedText str, int width) {
		return textRenderer.cacheCutString(str, width);
	}
}
