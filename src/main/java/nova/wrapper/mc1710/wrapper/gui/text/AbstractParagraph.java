package nova.wrapper.mc1710.wrapper.gui.text;

import nova.core.gui.render.text.TextRenderer.RenderedText;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

abstract class AbstractParagraph<T extends IText> implements IText, RenderedText {

	protected static Pattern wordSOL = Pattern.compile("^\\b[^\\s\n]+");
	protected static Pattern wordPattern = Pattern.compile("(%n|\n)|([^\\s]+(\\s|\n|%n))|\\s+|(.+)");

	protected List<Line<T>> lines = new ArrayList<>();
	protected Vector2D dimensions;

	protected void createDimensions() {
		double width = 0, height = 0;
		for (IText t2 : lines) {
			Vector2D dim = t2.getDimensions();
			width = Math.max(height, dim.getX());
			height += dim.getY();
		}
		dimensions = new Vector2D(width, height);
	}

	@Override
	public Vector2D getDimensions() {
		return dimensions;
	}
}