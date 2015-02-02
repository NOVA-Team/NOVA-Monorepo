package nova.core.gui.elements;

import nova.core.gui.GuiElement;
import nova.core.gui.GuiElementEvent;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.nativeimpl.NativeButton;

public class Button extends GuiElement<NativeButton> {

	public Button(String uniqueID) {
		super(uniqueID);
		registerListener(this::onMousePressed, MouseEvent.class);
	}

	private void onMousePressed(MouseEvent event) {
		if (getShape().contains(event.mouseX, event.mouseY)) {
			triggerEvent(new GuiElementEvent.ActionEvent(this));
		}
	}
}
