package nova.core.gui.render;

import nova.core.gui.Outline;
import nova.core.gui.Spacing;
import nova.core.render.Color;
import nova.core.render.RenderException;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Stack;

/**
 * A canvas is an object that can be drawn onto in 2D space. The content might
 * be back buffered, depends on the context. Use {@link #isBuffered()} to check.
 * A {@link Graphics} object can be used to draw onto a canvas.
 * @author Vic Nightfall
 */
public abstract class Canvas {

	private static final int MAX_STACK_DEPTH = Short.MAX_VALUE;

	protected Vector2D dimension;
	protected boolean isBuffered;
	protected CanvasState state = new CanvasState();

	private Stack<CanvasState> stack = new Stack<>();

	public Canvas(Vector2D dimension, boolean isBuffered) {
		this.dimension = dimension;
		this.isBuffered = isBuffered;
	}

	public CanvasState getState() {
		return state;
	}

	public Vector2D getDimension() {
		return dimension;
	}

	public int getZIndex() {
		return state.zIndex;
	}

	public void setZIndex(int zIndex) {
		state.zIndex = zIndex;
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
		Vector2D dimension = getDimension();
		addScissor(
			outline.minYi() + state.tyi(),
			(int) dimension.getX() - outline.getWidth() - state.txi(),
			(int) dimension.getY() - outline.getHeight() - state.tyi(),
			outline.minXi() + state.txi());
	}

	public void setScissor(int top, int right, int bottom, int left) {
		state.scissor = new Spacing(top, right, bottom, left);
		enableScissor();
	}

	public void addScissor(int top, int right, int bottom, int left) {
		state.scissor = state.scissor.combine(new Spacing(top, right, bottom, left));
		enableScissor();
	}

	public void enableScissor() {
		state.isScissor = true;
	}

	public void disableScissor() {
		state.isScissor = false;
	}

	public void push() {
		if (stack.size() >= MAX_STACK_DEPTH) {
			throw new RenderException("Canvas stack overflow! Max: " + MAX_STACK_DEPTH);
		}
		stack.push(state.clone());
	}

	public void pop() {
		if (stack.size() == 0) {
			throw new RenderException("Canvas stack underflow!");
		}
		state = stack.pop();
	}

	public abstract void bindTexture(Texture texture);

	public abstract void startDrawing(boolean textured);

	public abstract void addVertex(double x, double y);

	public abstract void addVertexWithUV(double x, double y, double u, double v);

	public void addVertex(Vertex2D v) {
		if (v.uv) {
			addVertexWithUV(v.getX(), v.getY(), v.u, v.v);
		} else {
			addVertex(v.getX(), v.getY());
		}
	}

	public abstract void draw();

	public static final class CanvasState implements Cloneable {

		protected int zIndex;
		protected Color color = Color.white;
		protected double tx, ty;
		protected double angle;
		protected Spacing scissor = Spacing.empty;
		protected boolean isScissor;

		public int zIndex() {
			return zIndex;
		}

		public Color color() {
			return color;
		}

		public double tx() {
			return tx;
		}

		public double ty() {
			return ty;
		}

		public int txi() {
			return (int) tx;
		}

		public int tyi() {
			return (int) ty;
		}

		public double angle() {
			return angle;
		}

		public Spacing scissor() {
			return scissor;
		}

		public boolean isScissor() {
			return isScissor;
		}

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
