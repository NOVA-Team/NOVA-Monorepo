package nova.wrapper.mc1710.backward.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.FormattedText;
import nova.core.gui.render.FormattedText.TextFormat;
import nova.core.gui.render.TextRenderer;
import nova.core.render.Color;
import nova.core.util.transform.Vector2i;

import org.lwjgl.opengl.GL11;

public class MCTextRenderer implements TextRenderer {

	private final FontRenderer fontrenderer;
	private int zIndex;

	private static final Pattern pattern = Pattern.compile("(\u00A7s)|(\u00A7c([-+]?\\d+)\u00A7)");

	private static String unwrap(FormattedText text) {
		TextFormat format = new TextFormat();
		String formatted = "";
		for (FormattedText sub : text) {
			formatted += unwrap(sub, format);
			format = sub.getFormat();
		}
		return formatted;
	}

	private static String unwrap(FormattedText text, TextFormat prev) {
		String formatted = "";
		TextFormat format = text.getFormat();

		if (prev.shadow != format.shadow)
			formatted += "\u00A7s";
		if (prev.color != format.color)
			formatted += "\u00A7c" + format.color.argb() + "\u00A7";

		return formatted + addFormat(text.getText(), format);
	}

	private static String addFormat(String text, TextFormat format) {
		String out = "";
		if (format.bold)
			out += "\u00A7l";
		if (format.italic)
			out += "\u00A7o";
		if (format.strikethrough)
			out += "\u00A7m";
		if (format.underline)
			out += "\u00A7n";

		out += text;
		out += "\u00A7r";

		return out;
	}

	private static List<Text> split(String text) {
		List<Text> unwrapped = new ArrayList<>();
		Matcher matcher = pattern.matcher(text);

		boolean shadow = false;
		Color color = Color.white;

		int index = 0;
		while (matcher.find()) {
			boolean newShadow = shadow;
			Color newColor = color;

			if (matcher.group(1) != null) {
				newShadow = !newShadow;
			} else {
				newColor = Color.argb(Integer.parseInt(matcher.group(3)));
			}

			if (newShadow != shadow || newColor.argb() != color.argb()) {
				String sub = text.substring(index, matcher.start());
				if (sub.length() > 0)
					unwrapped.add(new Text(sub, color, shadow));
				index = matcher.end();
			}

			shadow = newShadow;
			color = newColor;
		}

		if (index < text.length()) {
			String trail = text.substring(index, text.length());
			unwrapped.add(new Text(trail, color, shadow));
		}

		return unwrapped;
	}

	private static class Text {

		private String text;
		private final Color color;
		private final boolean shadow;

		private Text(String text, Color color, boolean shadow) {
			this.text = pattern.matcher(text).replaceAll("");
			this.color = color;
			this.shadow = shadow;
		}
	}

	public MCTextRenderer(FontRenderer fontrenderer) {
		this.fontrenderer = fontrenderer;
	}

	@Override
	public void drawString(int x, int y, FormattedText str) {
		GL11.glTranslatef(0, 0, zIndex);
		List<Text> text = split(unwrap(str));
		int xOffset = 0;
		for (Text sub : text) {
			fontrenderer.drawString(sub.text, x + xOffset, y, sub.color.argb(), sub.shadow);
			xOffset += fontrenderer.getStringWidth(sub.text) + 1;
		}
		GL11.glTranslatef(0, 0, -zIndex);
	}

	@Override
	public void drawString(int x, int y, FormattedText str, int width) {
		GL11.glTranslatef(0, 0, zIndex);

		int xOffset = 0;
		int yOffset = 0;
		int spaceWidth = fontrenderer.getCharWidth(' ');

		TextFormat format = new TextFormat();
		for (FormattedText sub : str) {
			format = sub.getFormat();
			String[] words = sub.getText().split(" +");
			for (String word : words) {
				word = addFormat(word, format);
				int wordWidth = fontrenderer.getStringWidth(word);

				if (xOffset + wordWidth > width) {
					xOffset = 0;
					yOffset += fontrenderer.FONT_HEIGHT + 1;
				}

				fontrenderer.drawString(word, x + xOffset, y + yOffset, format.color.argb(), format.shadow);
				xOffset += wordWidth > 0 ? wordWidth + spaceWidth : 0;
			}
		}

		GL11.glTranslatef(0, 0, -zIndex);
	}

	@Override
	public void drawCutString(int x, int y, FormattedText str, int width) {
		// TODO implement
		throw new UnsupportedOperationException();
	}

	@Override
	public void drawString(int x, int y, String str) {
		GL11.glTranslatef(0, 0, zIndex);
		fontrenderer.drawString(str.replaceAll("\u00A7", ""), x, y, Color.white.argb());
		GL11.glTranslatef(0, 0, -zIndex);
	}

	@Override
	public void drawString(int x, int y, String str, int width) {
		GL11.glTranslatef(0, 0, zIndex);
		fontrenderer.drawSplitString(str.replaceAll("\u00A7", "").replaceAll("%n", "\n"), x, y, width, Color.white.argb());
		GL11.glTranslatef(0, 0, -zIndex);
	}

	@Override
	public void drawCutString(int x, int y, String str, int width) {
		// TODO implement
		throw new UnsupportedOperationException();
	}

	@Override
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	@Override
	public Vector2i getBounds(FormattedText text) {
		return getBounds(text.getText());
	}

	@Override
	public Vector2i getBounds(String str) {
		// TODO What about \n ?
		return new Vector2i(fontrenderer.getStringWidth(str), fontrenderer.FONT_HEIGHT);
	}
}
