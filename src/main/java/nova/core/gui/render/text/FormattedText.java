package nova.core.gui.render.text;

import nova.core.render.Color;
import nova.core.render.RenderException;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An object that holds text and the format to apply to it. A
 * {@link TextRenderer} can be used to draw formatted text. It is <i>highly</i>
 * recommended to store the instance of Formatted text so that the wrapper can
 * cache it.
 *
 * @author Vic Nightfall
 */
public class FormattedText implements Iterable<FormattedText> {

	protected FormattedText child;
	protected FormattedText first = this;
	protected TextFormat format = new TextFormat();
	protected String text;

	public FormattedText() {
		this.text = "";
	}

	public FormattedText(String text) {
		this.text = Objects.requireNonNull(text);
	}

	public FormattedText(String text, TextFormat format) {
		this.text = Objects.requireNonNull(text);
		this.format = Objects.requireNonNull(format);
	}

	public FormattedText add(FormattedText other) {
		Objects.requireNonNull(other);
		// Suppress for TranslatedText, they can't merge.
		if (!(other instanceof TranslatedText) && format.equals(other.format)) {
			return add(other.getText());
		} else {
			child = other;
			other.first = this.first;
			return other;
		}
	}

	public FormattedText add(String text) {
		this.text += Objects.requireNonNull(text);
		return this;
	}

	public FormattedText add(String text, Consumer<TextFormat> consumer) {
		Objects.requireNonNull(text);
		TextFormat format = this.format.clone();
		consumer.accept(format);

		if (this.format.equals(format)) {
			this.text += text;
			return this;
		} else {
			FormattedText child = new FormattedText(text);
			this.child = child;
			child.format = format;
			child.first = this.first;
			return child;
		}
	}

	public TextFormat getFormat() {
		// TODO Could interfere with the cache when modified.
		return format;
	}

	public String getText() {
		return text;
	}

	public static Pattern pattern = Pattern.compile("(?<!\\\\)(?:\\\\\\\\)*(&([^;]{2});|&cr(([-+]?\\d+)|(#[0-F0-f]+));|&sz(\\d+);)");

	// TODO Support for localization
	/**
	 * <p>
	 * Parses an arbitrary String to {@link FormattedText}. The format applied
	 * has the following syntax: {@code &<tag>[data];} There are two types of
	 * tags, tags without additional data are flags, i.e they are toggled each
	 * time. {@code &rt;} turns all flags back to {@code false}. Tags with
	 * additional data are not affected by {@code &rt;}. Tags can be escaped
	 * with an {@code \} character. The case is taken into account, only
	 * lowercase characters are permitted for tags.
	 * </p>
	 *
	 * <table>
	 * <caption><b>The formatting codes applied are as follows:</b></caption>
	 * <tr>
	 * <th>Code:</th>
	 * <th>Description:</th>
	 * <th>Example:</th>
	 * </tr>
	 * <tr>
	 * <td>{@code &sh;}</td>
	 * <td>Toggles the flag for Text shadow</td>
	 * <td style='text-shadow:black 2px 2px 2px;'>
	 * "The quick brown fox jumps over the lazy dog"</td>
	 * </tr>
	 * <tr>
	 * <td>{@code &it;}</td>
	 * <td>Toggles the flag for italics</td>
	 * <td><i>"The quick brown fox jumps over the lazy dog"</i></td>
	 * </tr>
	 * <tr>
	 * <td>{@code &bd;}</td>
	 * <td>Toggles the flag for bold</td>
	 * <td><b>"The quick brown fox jumps over the lazy dog"</b></td>
	 * </tr>
	 * <tr>
	 * <td>{@code &ul;}</td>
	 * <td>Toggles the flag for underline</td>
	 * <td><u>"The quick brown fox jumps over the lazy dog"</u></td>
	 * </tr>
	 * <tr>
	 * <td>{@code &st;}</td>
	 * <td>Toggles the flag for strikethrough</td>
	 * <td style="text-decoration: strikethrough">
	 * "The quick brown fox jumps over the lazy dog"</td>
	 * </tr>
	 * <tr>
	 * <td>{@code &rt;}</td>
	 * <td>Resets all previous flags to {@code false}</td>
	 * <td>"The quick brown fox jumps over the lazy dog"</td>
	 * </tr>
	 * <tr>
	 * <td>{@code &cr[ARGB];}</td>
	 * <td>Sets the text color to the given color, you can use
	 * {@link Color#argb()}. Colors prefixed with {@code #} are interpreted as
	 * hexadecimal.</td>
	 * <td style='color: blue;'>
	 * "The quick brown fox jumps over the lazy dog"</td>
	 * </tr>
	 * <tr>
	 * <td>{@code &sz[size];}</td>
	 * <td>Sets the text size to the given size in pixels. See
	 * {@link TextFormat#DEFAULT_SIZE}</td>
	 * <td style='font-size: 15px;'>
	 * "The quick brown fox jumps over the lazy dog"</td>
	 * </tr>
	 * </table>
	 *
	 * @param string String to parse
	 * @return FormattedText instance
	 */
	public static FormattedText parse(String string) {
		Objects.requireNonNull(string);
		Matcher matcher = pattern.matcher(string);
		TextFormat format = new TextFormat();
		FormattedText text = new FormattedText();
		int index = 0;
		while (matcher.find()) {
			int end = matcher.end();
			int start = matcher.start(1);

			if (start - index > 0) {
				String substr = string.substring(index, start);
				FormattedText append = new FormattedText(substr);
				append.format = format;
				text = text.add(append);
			}

			if (matcher.group(2) != null) {
				format = format.clone();
				switch (matcher.group(2)) {
					case "sh":
						format.shadow = !format.shadow;
						break;
					case "it":
						format.italic = !format.italic;
						break;
					case "bd":
						format.bold = !format.bold;
						break;
					case "ul":
						format.underline = !format.underline;
						break;
					case "st":
						format.strikethrough = !format.strikethrough;
						break;
					case "rt":
						format.reset();
						break;
				}
			} else if (matcher.group(3) != null) {
				format = format.clone();
				try {
					if (matcher.group(4) != null) {
						format.color = Color.argb(Integer.parseInt(matcher.group(4)));
					} else {
						// Parse hex string, needs to be a long as
						// Integer.parseInt doesn't deal with the sign here.
						format.color = Color.argb((int) Long.parseLong(matcher.group(5).substring(1), 16));
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new RenderException("Failed to parse color at index " + matcher.start() + ": '" + matcher.group(3) + "'.");
				}
			} else if (matcher.group(6) != null) {
				format = format.clone();
				try {
					format.size = Integer.parseInt(matcher.group(6));
				} catch (NumberFormatException e) {
					throw new RenderException("Failed to parse size at index " + matcher.start() + ": '" + matcher.group(6) + "'.");
				}
			}
			index = end;
		}
		if (string.length() - index > 0) {
			String trail = string.substring(index, string.length());
			FormattedText append = new FormattedText(trail);
			append.format = format;
			text = text.add(append);
		}

		return text;
	}

	public static class TextFormat implements Cloneable {

		public static int DEFAULT_SIZE = 9;
		public static Color DEFAULT_COLOR = Color.black;

		public boolean shadow;
		public boolean italic;
		public boolean bold;
		public boolean underline;
		public boolean strikethrough;

		public Color color = DEFAULT_COLOR;
		public int size = DEFAULT_SIZE;

		public TextFormat() {

		}

		public TextFormat(Consumer<TextFormat> consumer) {
			consumer.accept(this);
			if (color == null)
				throw new NullPointerException("Color not supplied!");
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
			return Objects.hash(shadow, italic, bold, underline, strikethrough, color, size);
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
					&& this.size == other.size
					&& this.color.equals(other.color);
		}

		/**
		 * Resets all formatting, the color stays the same.
		 */
		public void reset() {
			shadow = italic = bold = underline = strikethrough = false;
		}
	}

	@Override
	public Iterator<FormattedText> iterator() {
		return new FormattedTextIterator(this);
	}

	@Override
	public Spliterator<FormattedText> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED | Spliterator.NONNULL);
	}

	public Stream<FormattedText> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	private static class FormattedTextIterator implements Iterator<FormattedText> {

		private FormattedText current;

		private FormattedTextIterator(FormattedText text) {
			current = text.first;
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
