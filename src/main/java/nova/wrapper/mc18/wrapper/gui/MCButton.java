package nova.wrapper.mc18.wrapper.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseState;
import nova.core.gui.Outline;
import nova.core.gui.component.Button;
import nova.core.gui.nativeimpl.NativeButton;
import nova.core.gui.render.Graphics;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public class MCButton extends MCGuiComponent<Button> implements NativeButton, DrawableGuiComponent {

	@SideOnly(Side.CLIENT)
	private MCGuiButton button;

	public MCButton(Button component) {
		super(component);

		if (FMLCommonHandler.instance().getSide().isClient()) {
			button = new MCGuiButton();
		}

		component.onGuiEvent(this::onMousePressed, MouseEvent.class);
	}

	@Override
	public void setOutline(Outline outline) {
		button.width = outline.getWidth();
		button.height = outline.getHeight();
		button.xPosition = outline.minXi();
		button.yPosition = outline.minYi();
		super.setOutline(outline);
	}

	@Override
	public String getText() {
		return button.displayString;
	}

	@Override
	public void setText(String text) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			button.displayString = text;
		}
	}

	@Override
	public Optional<Vector2D> getMinimumSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		return fontRenderer != null ? Optional.of(new Vector2D(fontRenderer.getStringWidth(button.displayString) + 10, fontRenderer.FONT_HEIGHT + 10)) : Optional.empty();
	}

	@Override
	public boolean isPressed() {
		// TODO
		return false;
	}

	@Override
	public void setPressed(boolean isPressed) {
		// TODO
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		super.draw(mouseX, mouseY, partial, graphics);
	}

	public void onMousePressed(MouseEvent event) {
		if (event.state == EnumMouseState.DOWN) {
			if (getComponent().isMouseOver()) {
				button.playPressSound(Minecraft.getMinecraft().getSoundHandler());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public class MCGuiButton extends GuiButtonExt {

		public MCGuiButton() {
			super(0, 0, 0, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			GL11.glTranslatef(-xPosition, -yPosition, 0);
			super.drawButton(mc, mouseX + xPosition, mouseY + yPosition);
			GL11.glTranslatef(xPosition, yPosition, 0);
		}
	}
}
