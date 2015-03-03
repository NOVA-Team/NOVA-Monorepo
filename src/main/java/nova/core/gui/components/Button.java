package nova.core.gui.components;

import nova.core.gui.ComponentEvent;
import nova.core.gui.GuiComponent;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.nativeimpl.NativeButton;

public class Button extends GuiComponent<Button, NativeButton> {

	public Button(String uniqueID, final String text) {
		super(uniqueID, NativeButton.class);
		onGuiEvent(this::onMousePressed, MouseEvent.class);
		getNative().setText(text);
	}

	private void onMousePressed(MouseEvent event) {
		switch (event.state) {
			case DOWN:
				// TODO Handle overlapping components etc, check if the
				// component is visible at the given position.
				if (getOutline().contains(event.mouseX, event.mouseY)) {
					triggerEvent(new ComponentEvent.ActionEvent(this));
				}
			default:
				break;
		}
	}
}
