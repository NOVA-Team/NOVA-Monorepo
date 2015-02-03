package nova.core.gui;

import nova.core.gui.nativeimpl.NativeGuiComponent;

public abstract class GuiCanvas extends GuiComponent<NativeGuiComponent> {

	public GuiCanvas(String uniqueID) {
		super(uniqueID);
	}

}
