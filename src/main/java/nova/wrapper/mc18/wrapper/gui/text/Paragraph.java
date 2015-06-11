package nova.wrapper.mc18.wrapper.gui.text;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.text.FormattedText;
import nova.core.gui.render.text.FormattedText.TextFormat;
import nova.core.gui.render.text.TextRenderer;
import nova.core.gui.render.text.TextRenderer.RenderedText;
import nova.wrapper.mc18.wrapper.gui.text.IText.Word;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.regex.Matcher;

class Paragraph extends AbstractParagraph<Word> implements RenderedText {

	public Paragraph(FormattedText text, FontRenderer fontRenderer, int width) {

		List<FormattedText> list = Lists.newArrayList(text);
		Line<Word> currentLine = new Line<>();
		lines.add(currentLine);

		float xOffset = 0;

		for (int i = 0; i < list.size(); i++) {

			FormattedText current = list.get(i);
			TextFormat format = current.getFormat();
			String string = current.getText();
			Matcher matcher = AbstractParagraph.wordPattern.matcher(string);

			while (matcher.find()) {
				if (matcher.group(1) != null) {
					xOffset = 0;
					currentLine = new Line<>();
					lines.add(currentLine);
				} else {
					boolean flag = false;
					String string2 = matcher.group(2);
					if (string2 == null) {
						flag = true;
						string2 = matcher.group(4);
						if (string2 == null) {
							continue;
						}
					}

					Word word = new Word().append(new Text(string2, format, fontRenderer));

					if (flag) {
						FormattedText next;
						int j = i;
						while (++j < list.size()) {
							next = list.get(j);
							String string3 = next.getText();
							Matcher matcher2 = AbstractParagraph.wordSOL.matcher(string3);

							if (matcher2.matches()) {
								i++;
								word.append(new Text(next.getText(), next.getFormat(), fontRenderer));
								continue;
							} else {
								matcher2.reset();

								if (matcher2.find()) {
									word.append(new Text(matcher2.group(), next.getFormat(), fontRenderer));
									list.set(j, new FormattedText(string3.substring(matcher2.end(), string3.length()), next.getFormat()));
								}
								break;
							}
						}
						word.text.get(word.text.size() - 1).append(" ", format, fontRenderer);
					}

					double wordWidth = word.getDimensions().getX();
					if (xOffset + wordWidth > width) {
						xOffset = 0;
						currentLine = new Line<>();
						lines.add(currentLine);
					}
					xOffset += wordWidth;
					currentLine.append(word);
				}
			}
		}
		createDimensions();
	}

	@Override
	public void draw(int x, int y, TextRenderer renderer) {
		MCTextRenderer textRenderer = (MCTextRenderer) renderer;
		GL11.glTranslatef(0, 0, textRenderer.zIndex);

		int xOffset = 0;
		int yOffset = 0;

		for (int i = 0; i < lines.size(); i++) {
			Line<Word> line = lines.get(i);
			if (i == 0) {
				yOffset += line.getDimensions().getY();
			}
			for (Word word : line.text) {
				xOffset = textRenderer.drawText(word.text, x, y, xOffset, yOffset);
			}

			if (i + 1 < lines.size()) {
				yOffset += lines.get(i + 1).getDimensions().getY() + 1;
				xOffset = 0;
			}
		}

		GL11.glTranslatef(0, 0, -textRenderer.zIndex);
	}
}