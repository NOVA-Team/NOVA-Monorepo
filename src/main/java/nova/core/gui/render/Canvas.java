package nova.core.gui.render;

import nova.core.render.Color;
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

	public int getZIndex();

	public Color getColor();

	public void setColor(Color color);

	public void bindTexture(Texture texture);

	public void translate(double x, double y);

	public void rotate(double angle);

	public void startDrawing(boolean textured);

	public void addVertex(double x, double y);

	public void addVertexWithUV(double x, double y, double u, double v);

	public default void addVertex(Vertex2D v) {
		if (v.uv)
			addVertexWithUV(v.x, v.y, v.u, v.v);
		else
			addVertex(v.x, v.y);
	}

	public void draw();

	public boolean isBuffered();
}
