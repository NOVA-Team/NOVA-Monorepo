package nova.core.gui.component;

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

	public Button(final String text) {
		this("", text);
	}

	public String getText() {
		return getNative().getText();
	}

	public Button setText(String text) {
		getNative().setText(text);
		return this;
	}

	private void onMousePressed(MouseEvent event) {
		switch (event.state) {
			case DOWN:
				// TODO Handle overlapping components etc, check if the
				// component is visible at the given position.
				if (isMouseOver()) {
					triggerEvent(new ComponentEvent.ActionEvent(this));
				}
			default:
				break;
		}
	}
}
