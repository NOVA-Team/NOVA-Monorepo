package nova.core.gui.render;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import nova.core.render.Color;

/**
 * An object that holds text and the format to apply to it. A
 * {@link TextRenderer} can be used to draw formatted text.
 * 
 * @author Vic Nightfall
 */
public class FormattedText implements Iterable<FormattedText> {

	private FormattedText child;
	private FormattedText parent;
	private TextFormat format = new TextFormat();
	private String text;

	public FormattedText(String text) {
		this.text = text;
	}

	public FormattedText(String text, TextFormat format) {
		this.text = text;
		this.format = format;
	}

	public FormattedText add(FormattedText other) {
		if (format.equals(other.format)) {
			return add(other.getText());
		} else {
			child = other;
			other.parent = this;
			return other;
		}
	}

	public FormattedText add(String text) {
		this.text += text;
		return this;
	}

	public FormattedText add(String text, Consumer<TextFormat> consumer) {

		TextFormat format = this.format.clone();
		consumer.accept(format);

		if (this.format.equals(format)) {
			this.text += text;
			return this;
		} else {
			FormattedText child = new FormattedText(text, format);
			this.child = child;
			child.parent = this;
			return child;
		}
	}

	public TextFormat getFormat() {
		return format;
	}

	public String getText() {
		return text;
	}

	public static class TextFormat implements Cloneable {

		public boolean shadow;
		public boolean italic;
		public boolean bold;
		public boolean underline;
		public boolean strikethrough;

		public Color color = Color.white;

		public TextFormat() {

		}

		public TextFormat(Consumer<TextFormat> consumer) {
			consumer.accept(this);
		}

		@Override
		protected TextFormat clone() {
			try {
				return (TextFormat) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int hashCode() {
			return Objects.hash(shadow, italic, bold, underline, strikethrough, color);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

			TextFormat other = (TextFormat) obj;

			return this.bold == other.bold 
				&& this.italic == other.italic 
				&& this.shadow == other.shadow 
				&& this.strikethrough == other.strikethrough 
				&& this.underline == other.underline 
				&& this.color == other.color;
		}
	}

	@Override
	public Iterator<FormattedText> iterator() {
		return new FormattedTextIterator(this);
	}

	private static class FormattedTextIterator implements Iterator<FormattedText> {

		private FormattedText current;

		public FormattedTextIterator(FormattedText first) {
			current = first;
			while (current.parent != null) {
				current = current.parent;
			}
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public FormattedText next() {
			FormattedText text = current;
			current = current.child;
			return text;
		}
	}
}