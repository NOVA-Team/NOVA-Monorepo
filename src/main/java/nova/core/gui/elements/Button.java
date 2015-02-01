package nova.core.gui.elements;

import nova.core.gui.GuiElement;
import nova.core.gui.GuiElementEvent;
import nova.core.gui.GuiEvent;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.nativeimpl.NativeButton;

public class Button extends GuiElement<NativeButton> {

	public Button(String uniqueID) {
		super(uniqueID);
	}

	@Override
	public void onEvent(GuiEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			if (getShape().contains(mouseEvent.mouseX, mouseEvent.mouseY)) {
				triggerEvent(new GuiElementEvent.ActionEvent(this));
			}
		}
	}
}
