package nova.core.gui.layout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nova.core.util.exception.NovaException;
import nova.core.util.transform.Vector2i;

/**
 * {@link Constraints} for relative positioning.
 * 
 * @author Vic Nightfall
 */
public class RelativePosition extends Constraints<RelativePosition> {

	public Anchor xAnchor = Anchor.WEST;
	public Anchor yAnchor = Anchor.NORTH;
	public int xOffset;
	public int yOffset;
	public boolean xRelative = true;
	public boolean yRelative = true;

	public RelativePosition() {

	}

	public RelativePosition(Vector2i pos) {
		xOffset = pos.x;
		yOffset = pos.y;
		xRelative = yRelative = false;
	}

	public RelativePosition(Anchor xAnchor, Anchor yAnchor, int xOffset, int yOffset, boolean xRelative, boolean yRelative) {
		this.xAnchor = xAnchor;
		this.yAnchor = yAnchor;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
	}

	public RelativePosition(Anchor xAnchor, Anchor yAnchor, int xOffset, int yOffset) {
		this(xAnchor, yAnchor, xOffset, yOffset, true, true);
	}

	public RelativePosition(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public static final Pattern pattern = Pattern.compile("(west|east|north|south):\\s?([-+]?\\d+)(%)?[\\s]?", Pattern.CASE_INSENSITIVE);

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
					xOffset = offset;
					xRelative = relative;
				} else if (anchor.axis == 2) {
					yAnchor = anchor;
					yOffset = offset;
					yRelative = relative;
				}
			}
			if (str.length() - size > 0)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NovaException("Invalid relative position \"" + str + "\"");
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

	public RelativePosition setX(Anchor anchor, float offset) {
		this.xAnchor = anchor;
		this.xOffset = (int) (offset * 100);
		this.xRelative = true;
		return this;
	}

	public RelativePosition setY(Anchor anchor, float offset) {
		this.yAnchor = anchor;
		this.yOffset = (int) (offset * 100);
		this.yRelative = true;
		return this;
	}

	public Vector2i getPositionOf(Vector2i parentSize) {
		int x = xRelative ? (int) (parentSize.x * (xOffset / 100F)) : xOffset;
		int y = yRelative ? (int) (parentSize.y * (yOffset / 100F)) : yOffset;

		if (xAnchor == Anchor.EAST)
			x = parentSize.x - x;
		if (yAnchor == Anchor.SOUTH)
			y = parentSize.y - y;

		return new Vector2i(x, y);
	}
}
