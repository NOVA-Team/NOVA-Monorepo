package nova.core.gui.layout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nova.core.util.exception.NovaException;
import nova.core.util.transform.Vector2i;

/**
 * {@link Constraints} for relative positioning
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

	public Vector2i getPositionOf(Vector2i parentSize) {
		int x = xRelative ? (int) (parentSize.x * (xOffset / 100F)) : xOffset;
		int y = yRelative ? (int) (parentSize.y * (yOffset / 100F)) : yOffset;

		if (xAnchor == Anchor.EAST)
			xOffset = parentSize.x - xOffset;
		if (yAnchor == Anchor.SOUTH)
			yOffset = parentSize.y - yOffset;

		return new Vector2i(x, y);
	}

	public static final Pattern pattern = Pattern.compile("(west|east|north|south):([-+]?\\d+)(%){0,1}}[\\s]*", Pattern.CASE_INSENSITIVE);

	public RelativePosition(String str) {
		try {
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				Anchor anchor = Anchor.valueOf(matcher.group(1));
				int offset = Integer.valueOf(matcher.group(2));
				boolean relative = matcher.group(3) != null;
				str = str.substring(0, matcher.start()) + str.substring(matcher.end(), str.length() - 1);
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
			if (str.length() > 0)
				throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new NovaException("Invalid relative position \"" + str + "\"");
		}
	}
}
