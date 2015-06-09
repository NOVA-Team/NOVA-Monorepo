package nova.core.gui.layout;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Constraints} for relative positioning.
 * @author Vic Nightfall
 */
public class RelativePosition extends Constraints<RelativePosition> {

	public static final Pattern pattern = Pattern.compile("(west|east|north|south):\\s?([-+]?\\d+)(%)?[\\s]?", Pattern.CASE_INSENSITIVE);
	public Anchor xAnchor = Anchor.WEST;
	public Anchor yAnchor = Anchor.NORTH;
	public double xOffset;
	public double yOffset;
	public boolean xRelative;
	public boolean yRelative;

	public RelativePosition() {
	}

	public RelativePosition(double xOffset, double yOffset, Anchor xAnchor, Anchor yAnchor, boolean xRelative, boolean yRelative) {
		this(xOffset, yOffset, xAnchor, yAnchor);
		this.xRelative = xRelative;
		this.yRelative = yRelative;
	}

	public RelativePosition(double xOffset, double yOffset, Anchor xAnchor, Anchor yAnchor) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xAnchor = xAnchor;
		this.yAnchor = yAnchor;
	}

	public RelativePosition(Vector2D pos, Anchor xAnchor, Anchor yAnchor) {
		this(pos.getX(), pos.getY(), xAnchor, yAnchor);
		//xRelative = yRelative = true;
	}

	public RelativePosition(Vector2D pos) {
		this(pos, Anchor.WEST, Anchor.NORTH);
	}

	public RelativePosition(int xOffset, int yOffset) {
		this(xOffset, yOffset, Anchor.WEST, Anchor.NORTH);
	}

	public RelativePosition(int xOffset, int yOffset, Anchor xAnchor, Anchor yAnchor) {
		this((float) xOffset, (float) yOffset, xAnchor, yAnchor);
	}

	public RelativePosition(double xOffset, int yOffset) {
		this(xOffset, yOffset, Anchor.WEST, Anchor.NORTH);
	}

	public RelativePosition(double xOffset, int yOffset, Anchor xAnchor, Anchor yAnchor) {
		this(xOffset, (double) yOffset, xAnchor, yAnchor);
		xRelative = true;
	}

	public RelativePosition(int xOffset, double yOffset) {
		this(xOffset, yOffset, Anchor.WEST, Anchor.NORTH);
	}

	public RelativePosition(int xOffset, double yOffset, Anchor xAnchor, Anchor yAnchor) {
		this((double) xOffset, yOffset, xAnchor, yAnchor);
		yRelative = true;
	}

	public RelativePosition(double xOffset, double yOffset) {
		this(xOffset, yOffset, Anchor.WEST, Anchor.NORTH);
		xRelative = yRelative = true;
	}

	public RelativePosition(String str) {
		try {
			int size = 0;
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				size += matcher.end() - matcher.start();
				Anchor anchor = Anchor.valueOf(matcher.group(1).toUpperCase());
				int offset = Integer.valueOf(matcher.group(2));
				boolean relative = matcher.group(3) != null;
				if (anchor.axis == 1) {
					xAnchor = anchor;
					xOffset = relative ? offset / 100F : offset;
					xRelative = relative;
				} else if (anchor.axis == 2) {
					yAnchor = anchor;
					yOffset = relative ? offset / 100F : offset;
					yRelative = relative;
				}
			}
			if (str.length() - size > 0) {
				throw new IllegalArgumentException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LayoutException("Invalid relative position \"" + str + "\"");
		}
	}

	public RelativePosition setX(Anchor anchor, int offset) {
		this.xAnchor = anchor;
		this.xOffset = offset;
		this.xRelative = false;
		return this;
	}

	public RelativePosition setY(Anchor anchor, int offset) {
		this.yAnchor = anchor;
		this.yOffset = offset;
		this.yRelative = false;
		return this;
	}

	public RelativePosition setX(Anchor anchor, double offset) {
		this.xAnchor = anchor;
		this.xOffset = (int) (offset * 100);
		this.xRelative = true;
		return this;
	}

	public RelativePosition setY(Anchor anchor, double offset) {
		this.yAnchor = anchor;
		this.yOffset = (int) (offset * 100);
		this.yRelative = true;
		return this;
	}

	public Vector2D getPositionOf(Vector2D parentSize) {
		int x = xRelative ? (int) (parentSize.getX() * xOffset) : (int) xOffset;
		int y = yRelative ? (int) (parentSize.getY() * yOffset) : (int) yOffset;

		if (xAnchor == Anchor.EAST) {
			x = (int) parentSize.getX() - x;
		}
		if (yAnchor == Anchor.SOUTH) {
			y = (int) parentSize.getY() - y;
		}

		return new Vector2D(x, y);
	}
}
