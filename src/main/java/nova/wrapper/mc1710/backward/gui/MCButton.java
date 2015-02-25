package nova.wrapper.mc1710.backward.gui;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.components.Button;
import nova.core.gui.nativeimpl.NativeButton;
import nova.core.gui.render.Graphics;
import nova.core.util.transform.Vector2i;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MCButton implements NativeButton, DrawableGuiComponent {

	private Button component;
	
	@SideOnly(Side.CLIENT)
	private MCGuiButton button;

	public MCButton(Button component) {
		this.component = component;

		if (FMLCommonHandler.instance().getSide().isClient()) {
			button = new MCGuiButton();
		}
	}

	@Override
	public GuiComponent<?, ?> getComponent() {
		return component;
	}

	@Override
	public Outline getOutline() {
		return new Outline(button.xPosition, button.yPosition, button.width, button.height);
	}

	@Override
	public void setOutline(Outline outline) {
		button.xPosition = outline.x1i();
		button.yPosition = outline.y1i();
		button.width = outline.getWidth();
		button.height = outline.getHeight();
	}

	@Override
	public void requestRender() {
		
	}

	@Override
	public String getText() {
		return button.displayString;
	}
	
	@Override
	public Optional<Vector2i> getMinimumSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		return Optional.of(new Vector2i(fontRenderer.getStringWidth(button.displayString) + 10, fontRenderer.FONT_HEIGHT + 10));
	}

	@Override
	public void setText(String text) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			button.displayString = text;
		}
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
		Outline outline = getOutline();
		graphics.getCanvas().translate(outline.x1i(), outline.y1i());
		getComponent().render(mouseX, mouseY, graphics);
		graphics.getCanvas().translate(-outline.x1i(), -outline.y1i());
	}

	@SideOnly(Side.CLIENT)
	public class MCGuiButton extends GuiButtonExt {

		public MCGuiButton() {
			super(0, 0, 0, "");
		}
	}
}
