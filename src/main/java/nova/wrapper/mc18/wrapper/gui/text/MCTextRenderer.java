package nova.wrapper.mc18.wrapper.gui.text;

import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.render.text.FormattedText;
import nova.core.gui.render.text.TextRenderer;
import nova.core.render.Color;
import nova.core.util.math.MathUtil;
import nova.wrapper.mc18.wrapper.gui.MCCanvas;
import nova.wrapper.mc18.wrapper.gui.text.IText.Text;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class MCTextRenderer implements TextRenderer {

	private final FontRenderer fontrenderer;
	MCCanvas canvas;
	int      zIndex;

	public MCTextRenderer(FontRenderer fontrenderer, MCCanvas canvas) {
		this.fontrenderer = fontrenderer;
		this.canvas = canvas;
	}

	public void setCanvas(MCCanvas canvas) {
		this.canvas = canvas;
	}

	protected int drawText(List<Text> text, int x, int y, int xOffset, int yOffset) {
		for (Text sub : text) {
			drawText(sub, x, y, xOffset, yOffset);
			xOffset += sub.getDimensions().getX() + 1;
		}
		return xOffset;
	}

	protected void drawText(Text text, int x, int y, int xOffset, int yOffset) {
		float scale = text.format.size / (float) fontrenderer.FONT_HEIGHT;
		if (text.format.size != fontrenderer.FONT_HEIGHT) {
			GL11.glPushMatrix();
			GL11.glTranslatef(x + xOffset, y - fontrenderer.FONT_HEIGHT * scale + scale * 2F + yOffset, 0);
			GL11.glScalef(scale, scale, 0);
			GL11.glTranslatef(-x - xOffset, -y, 0);

			fontrenderer.drawString(text.text, x + xOffset, y, text.format.color.argb(), text.format.shadow);

			GL11.glPopMatrix();
		} else {
			fontrenderer.drawString(text.text, x + xOffset, y - fontrenderer.FONT_HEIGHT + 2 + yOffset, text.format.color.argb(), text.format.shadow);
		}
	}

	@Override
	public void drawString(int x, int y, String str) {
		GL11.glTranslatef(0, 0, zIndex);
		fontrenderer.drawString(str.replaceAll("\u00A7", ""), x, y, Color.black.argb());
		GL11.glTranslatef(0, 0, -zIndex);
	}

	@Override
	public void drawString(int x, int y, String str, int width) {
		GL11.glTranslatef(0, 0, zIndex);
		fontrenderer.drawSplitString(str.replaceAll("\u00A7", "").replaceAll("%n", "\n"), x, y, width, Color.black.argb());
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
	public Vector2D getBounds(String str) {
		int height = 0, width = 0;
		for (String line : str.split("%n|\n")) {
			width = MathUtil.max(width, fontrenderer.getStringWidth(line));
			height += fontrenderer.FONT_HEIGHT;
		}
		return new Vector2D(width, height);
	}

	@Override
	public RenderedText cacheString(FormattedText str) {
		return new SimpleParagraph(str, fontrenderer);
	}

	@Override
	public RenderedText cacheString(FormattedText str, int width) {
		return new Paragraph(str, fontrenderer, width);
	}

	@Override
	public RenderedText cacheCutString(FormattedText str, int width) {
		// TODO: Implement this.
		throw new UnsupportedOperationException();
	}
}
