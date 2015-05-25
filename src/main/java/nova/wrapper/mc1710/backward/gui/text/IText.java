package nova.wrapper.mc1710.backward.gui.text;

import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.text.FormattedText.TextFormat;
import nova.core.util.transform.vector.Vector2d;

import java.util.ArrayList;
import java.util.List;

interface IText {

	public Vector2d getDimensions();

	static class Text implements IText {

		final TextFormat format;
		String text;
		Vector2d dimensions;

		Text(String text, TextFormat format, FontRenderer fontrenderer) {
			this.text = addFormat(text, format);
			this.format = format;
			float scale = format.size / (float) fontrenderer.FONT_HEIGHT;
			this.dimensions = new Vector2d(fontrenderer.getStringWidth(this.text) * scale, fontrenderer.FONT_HEIGHT * scale);
		}

		Text append(String text, TextFormat format, FontRenderer fontrenderer) {
			this.text += addFormat(text, format);
			float scale = format.size / (float) fontrenderer.FONT_HEIGHT;
			this.dimensions = new Vector2d(fontrenderer.getStringWidth(this.text) * scale, fontrenderer.FONT_HEIGHT * scale);
			return this;
		}

		@Override
		public Vector2d getDimensions() {
			return dimensions;
		}

		private String addFormat(String text, TextFormat format) {
			StringBuilder builder = new StringBuilder();
			if (format.bold)
				builder.append("\u00A7l");
			if (format.italic)
				builder.append("\u00A7o");
			if (format.strikethrough)
				builder.append("\u00A7m");
			if (format.underline)
				builder.append("\u00A7n");

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
		public Vector2d getDimensions() {
			double width = 0, height = 0;
			for (T text : text) {
				Vector2d dim = text.getDimensions();
				height = Math.max(height, dim.y);
				width += dim.x;
			}
			return new Vector2d(width, height);
		}
	}

	static class Word implements IText {

		List<Text> text = new ArrayList<>();

		Word append(Text text) {
			this.text.add(text);
			return this;
		}

		@Override
		public Vector2d getDimensions() {
			double width = 0, height = 0;
			for (Text text : text) {
				Vector2d dim = text.getDimensions();
				height = Math.max(height, dim.y);
				width += dim.x;
			}
			return new Vector2d(width, height);
		}
	}
}