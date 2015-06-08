package nova.wrapper.mc1710.backward.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import nova.core.gui.Spacing;
import nova.core.gui.render.Canvas;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

public class MCCanvas extends Canvas {

	private final Tessellator tessellator;
	private final float scale;

	public MCCanvas(int width, int height, Tessellator tessellator) {
		this(width, height, tessellator, 1F);
	}

	public MCCanvas(int width, int height, Tessellator tessellator, float scale) {
		super(new Vector2D(width, height), false);
		this.tessellator = tessellator;
		this.scale = scale;
	}

	@Override
	public void bindTexture(Texture texture) {
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(texture.getResource()));
	}

	@Override
	public void startDrawing(boolean textured) {
		if (!textured)
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(state.color().redf(), state.color().greenf(), state.color().bluef(), state.color().alphaf());
		tessellator.startDrawing(GL11.GL_POLYGON);
	}

	@Override
	public void addVertex(double x, double y) {
		tessellator.addVertex(x, y, state.zIndex());
	}

	@Override
	public void addVertexWithUV(double x, double y, double u, double v) {
		tessellator.addVertexWithUV(x, y, state.zIndex(), u, v);
	}

	@Override
	public void draw() {
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void translate(double x, double y) {
		super.translate(x, y);
		GL11.glTranslated(x, y, 0);
	}

	@Override
	public void rotate(double angle) {
		super.rotate(angle);
		GL11.glRotated(angle, 0, 0, 1);
	}

	@Override
	public void push() {
		super.push();
		GL11.glPushMatrix();
	}

	@Override
	public void pop() {
		super.pop();
		GL11.glPopMatrix();

		Spacing scissor = state.scissor();
		glScissor(scissor.top(), scissor.right(), scissor.bottom(), scissor.left());
		if (state.isScissor()) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		} else {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}

	@Override
	public void setScissor(int top, int right, int bottom, int left) {
		glScissor(top, right, bottom, left);
		super.setScissor(top, right, bottom, left);
	}

	@Override
	public void addScissor(int top, int right, int bottom, int left) {
		super.addScissor(top, right, bottom, left);
		Spacing scissor = state.scissor();
		glScissor(scissor.top(), scissor.right(), scissor.bottom(), scissor.left());
	}

	private void glScissor(int top, int right, int bottom, int left) {
		//System.out.println();
		//System.out.println(top + " " + right + " " + bottom + " " + left);
		Minecraft mc = Minecraft.getMinecraft();

		left = (int) Math.max(Math.ceil(left * scale), 0);
		top = (int) Math.max(Math.ceil(top * scale), 0);
		right = (int) Math.min(Math.ceil(mc.displayWidth / scale) * scale - Math.ceil(right * scale), mc.displayWidth);
		bottom = (int) Math.min(Math.ceil(mc.displayHeight / scale) * scale - Math.ceil(bottom * scale), mc.displayHeight);

		//System.out.println(top + " " + right + " " + bottom + " " + left);

		GL11.glScissor(left, mc.displayHeight - bottom - top, right + left, bottom);

	}

	@Override
	public void enableScissor() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		super.enableScissor();
	}

	@Override
	public void disableScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		super.disableScissor();
	}
}
