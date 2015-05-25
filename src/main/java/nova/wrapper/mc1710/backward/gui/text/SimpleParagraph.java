package nova.wrapper.mc1710.backward.gui.text;

import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.text.FormattedText;
import nova.core.gui.render.text.FormattedText.TextFormat;
import nova.core.gui.render.text.TextRenderer;
import nova.core.gui.render.text.TextRenderer.RenderedText;
import nova.wrapper.mc1710.backward.gui.text.IText.Text;

import org.lwjgl.opengl.GL11;

class SimpleParagraph extends AbstractParagraph<Text> implements RenderedText {

	public SimpleParagraph(FormattedText text, FontRenderer fontRenderer) {

		Line<Text> currentLine = new Line<>();
		lines.add(currentLine);

		Text converted = null;
		TextFormat format = new TextFormat();

		for (FormattedText sub : text) {
			TextFormat nextFormat = sub.getFormat();
			String[] split = sub.getText().split("%n|\n", -1);

			for (int i = 0; i < split.length; i++) {
				if (i != 0 || converted == null || nextFormat.shadow != format.shadow || nextFormat.size != format.size || nextFormat.color != format.color) {
					if (i > 0) {
						currentLine = new Line<>();
						lines.add(currentLine);
					}
					converted = new Text(split[i], nextFormat, fontRenderer);
					currentLine.append(converted);
				} else {
					converted.append(sub.getText(), nextFormat, fontRenderer);
				}
			}
			format = nextFormat;
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
			Line<Text> line = lines.get(i);
			if (i == 0) {
				yOffset += line.getDimensions().y;
			}
			xOffset = textRenderer.drawText(line.text, x, y, xOffset, yOffset);
			if (i + 1 < lines.size()) {
				yOffset += lines.get(i + 1).getDimensions().y + 1;
				xOffset = 0;
			}
		}

		GL11.glTranslatef(0, 0, -textRenderer.zIndex);
	}
}