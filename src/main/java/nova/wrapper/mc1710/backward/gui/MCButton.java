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

public class MCButton extends GuiButtonExt implements NativeButton, DrawableGuiComponent {

	private Button component;
	
	public MCButton(Button component) {
		super(0, 0, 0, "");
		this.component = component;
	}

	@Override
	public GuiComponent<?, ?> getComponent() {
		return component;
	}

	@Override
	public Outline getOutline() {
		return new Outline(xPosition, yPosition, width, height);
	}

	@Override
	public void setOutline(Outline outline) {
		xPosition = outline.x1i();
		yPosition = outline.y1i();
		width = outline.getWidth();
		height = outline.getHeight();
	}

	@Override
	public void requestRender() {
		
	}

	@Override
	public String getText() {
		return displayString;
	}
	
	@Override
	public Optional<Vector2i> getMinimumSize() {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		return Optional.of(new Vector2i(fontRenderer.getStringWidth(displayString) + 10, fontRenderer.FONT_HEIGHT + 10));
	}

	@Override
	public void setText(String text) {
		this.displayString = text;
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
		drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		Outline outline = getOutline();
		graphics.getCanvas().translate(outline.x1i(), outline.y1i());
		getComponent().render(mouseX, mouseY, graphics);
		graphics.getCanvas().translate(-outline.x1i(), -outline.y1i());
	}
}
