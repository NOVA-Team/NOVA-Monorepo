package nova.wrapper.mc1710.backward.gui;

import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.components.Button;
import nova.core.gui.nativeimpl.NativeButton;
import cpw.mods.fml.client.config.GuiButtonExt;

public class MCButton extends GuiButtonExt implements NativeButton {

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

}
