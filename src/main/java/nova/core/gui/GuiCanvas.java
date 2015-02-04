package nova.core.gui;

import nova.core.gui.nativeimpl.NativeGuiComponent;

public abstract class GuiCanvas extends GuiComponent<GuiCanvas, NativeGuiComponent> {

	public GuiCanvas(String uniqueID) {
		super(uniqueID);
	}

}
