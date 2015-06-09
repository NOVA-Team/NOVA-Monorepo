package nova.wrapper.mc1710.wrapper.gui.text;

import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.text.FormattedText.TextFormat;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;

interface IText {

	Vector2D getDimensions();

	static class Text implements IText {

		final TextFormat format;
		String text;
		Vector2D dimensions;

		Text(String text, TextFormat format, FontRenderer fontrenderer) {
			this.text = addFormat(text, format);
			this.format = format;
			float scale = format.size / (float) fontrenderer.FONT_HEIGHT;
			this.dimensions = new Vector2D(fontrenderer.getStringWidth(this.text) * scale, fontrenderer.FONT_HEIGHT * scale);
		}

		Text append(String text, TextFormat format, FontRenderer fontrenderer) {
			this.text += addFormat(text, format);
			float scale = format.size / (float) fontrenderer.FONT_HEIGHT;
			this.dimensions = new Vector2D(fontrenderer.getStringWidth(this.text) * scale, fontrenderer.FONT_HEIGHT * scale);
			return this;
		}

		@Override
		public Vector2D getDimensions() {
			return dimensions;
		}

		private String addFormat(String text, TextFormat format) {
			StringBuilder builder = new StringBuilder();
			if (format.bold) {
				builder.append("\u00A7l");
			}
			if (format.italic) {
				builder.append("\u00A7o");
			}
			if (format.strikethrough) {
				builder.append("\u00A7m");
			}
			if (format.underline) {
				builder.append("\u00A7n");
			}

			builder.append(text);
			builder.append("\u00A7r");
			return builder.toString();
		}
	}

	static class Line<T extends IText> implements IText {

		List<T> text = new ArrayList<>();

		Line<T> append(T text) {
			this.text.add(text);
			return this;
		}

		@Override
		public Vector2D getDimensions() {
			double width = 0, height = 0;
			for (T text : this.text) {
				Vector2D dim = text.getDimensions();
				height = Math.max(height, dim.getY());
				width += dim.getX();
			}
			return new Vector2D(width, height);
		}
	}

	static class Word implements IText {

		List<Text> text = new ArrayList<>();

		Word append(Text text) {
			this.text.add(text);
			return this;
		}

		@Override
		public Vector2D getDimensions() {
			double width = 0, height = 0;
			for (Text text : this.text) {
				Vector2D dim = text.getDimensions();
				height = Math.max(height, dim.getY());
				width += dim.getX();
			}
			return new Vector2D(width, height);
		}
	}
}