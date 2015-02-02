package nova.core.gui;

import nova.core.gui.nativeimpl.NativeGuiElement;

public abstract class GuiCanvas extends GuiElement<NativeGuiElement> {

	public GuiCanvas(String uniqueID) {
		super(uniqueID);
	}

}
