package nova.core.gui.elements;

import nova.core.gui.GuiCanvas;
import nova.core.gui.ComponentEvent;
import nova.core.gui.GuiEvent.MouseEvent;

public class Button extends GuiCanvas {

	public Button(String uniqueID) {
		super(uniqueID);
		registerListener(this::onMousePressed, MouseEvent.class);
	}

	private void onMousePressed(MouseEvent event) {
		switch (event.state) {
			case CLICK:
				if (getOutline().contains(event.mouseX, event.mouseY)) {
					triggerEvent(new ComponentEvent.ActionEvent(this));
				}
			default:
				break;
		}
	}
}
