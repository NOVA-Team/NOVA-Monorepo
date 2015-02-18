package nova.core.gui.render;

import nova.core.render.texture.Texture;
import nova.core.util.transform.Vector2i;

/**
 * A canvas is an object that can be drawn onto in 2D space. The content might
 * be back buffered, depends on the context. Use {@link #isBuffered()} to check.
 * A {@link Graphics} object can be used to draw onto a canvas.
 * 
 * @author Vic Nightfall
 */
public interface Canvas {

	public Vector2i getDimension();

	public void setZIndex(int zIndex);

	public void getZIndex();

	public void setColor(Color color);

	public Color getColor();

	public void bindTexture(Texture texture);

	public void translate(int x, int y);

	public void rotate(double angle);

	public void startDrawing();

	public void addVertex(int x, int y);

	public void addVertexWithUV(int x, int y, int u, int v);

	public void draw();

	public boolean isBuffered();
}
