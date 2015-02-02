package nova.core.gui;

import nova.core.util.transform.Vector2i;

//TODO Could probably use this for things like Icons too.
public class AxisAlignedRect {

	public final Vector2i position;
	public final Vector2i dimension;

	public AxisAlignedRect(Vector2i position, Vector2i dimension) {
		this.position = position;
		this.dimension = dimension;
	}

	public AxisAlignedRect(int x, int y, int width, int height) {
		this.position = new Vector2i(x, y);
		this.dimension = new Vector2i(width, height);
	}

	public AxisAlignedRect setPosition(Vector2i position) {
		return new AxisAlignedRect(position, this.dimension);
	}

	public AxisAlignedRect setDimension(Vector2i dimension) {
		return new AxisAlignedRect(this.position, dimension);
	}

	public AxisAlignedRect setPosition(int x, int y) {
		return setPosition(new Vector2i(x, y));
	}

	public AxisAlignedRect setDimension(int width, int height) {
		return setDimension(new Vector2i(width, height));
	}

	public int x1() {
		return position.x;
	}

	public int y1() {
		return position.y;
	}

	public int x2() {
		return position.x + dimension.x;
	}

	public int y2() {
		return position.y + dimension.y;
	}

	public boolean contains(Vector2i point) {
		return contains(point.x, point.y);
	}

	public boolean contains(int x, int y) {
		return x >= x1() && x <= x2() && y >= y1() && y >= y2();
	}
}
