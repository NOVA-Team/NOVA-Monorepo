package nova.core.gui.render;

import java.util.Stack;

import nova.core.gui.Outline;
import nova.core.gui.Spacing;
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
public abstract class Canvas {

	protected Vector2i dimension;
	protected boolean isBuffered;
	protected CanvasState state = new CanvasState();

	private Stack<CanvasState> stack = new Stack<>();

	public Canvas(Vector2i dimension, boolean isBuffered) {
		this.dimension = dimension;
		this.isBuffered = isBuffered;
	}

	public Vector2i getDimension() {
		return dimension;
	}

	public void setZIndex(int zIndex) {
		state.zIndex = zIndex;
	}

	public int getZIndex() {
		return state.zIndex;
	}

	public Color getColor() {
		return state.color;
	}

	public void setColor(Color color) {
		state.color = color;
	}

	public boolean isBuffered() {
		return isBuffered;
	}

	public void translate(double x, double y) {
		state.tx += x;
		state.ty += y;
	}

	public void rotate(double angle) {
		state.angle = (state.angle + angle) % 360D;
	}

	public void setScissor(Outline outline) {
		Vector2i dimension = getDimension();
		setScissor(outline.x1i(), outline.y1i(), dimension.x - outline.getWidth(), dimension.y - outline.getHeight());
		enableScissor();
	}

	public void setScissor(int top, int right, int bottom, int left) {
		state.scissor = new Spacing(top, right, bottom, left);
		enableScissor();
	}

	public void enableScissor() {
		state.isScissor = true;
	}

	public void disableScissor() {
		state.isScissor = false;
	}

	public void push() {
		stack.push(state.clone());
	}

	public void pop() {
		state = stack.pop();
	}

	public abstract void bindTexture(Texture texture);

	public abstract void startDrawing(boolean textured);

	public abstract void addVertex(double x, double y);

	public abstract void addVertexWithUV(double x, double y, double u, double v);

	public void addVertex(Vertex2D v) {
		if (v.uv)
			addVertexWithUV(v.x, v.y, v.u, v.v);
		else
			addVertex(v.x, v.y);
	}

	public abstract void draw();

	protected static class CanvasState implements Cloneable {

		public int zIndex;
		public Color color;
		public double tx, ty;
		public double angle;
		public Spacing scissor = Spacing.empty;
		public boolean isScissor;

		@Override
		protected CanvasState clone() {
			try {
				return (CanvasState) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
