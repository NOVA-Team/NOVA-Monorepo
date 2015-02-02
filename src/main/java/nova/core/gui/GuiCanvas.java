package nova.core.gui;

import nova.core.gui.nativeimpl.NativeCanvas;

public abstract class GuiCanvas extends GuiElement<NativeCanvas> {

	public GuiCanvas(String uniqueID) {
		super(uniqueID);
	}

}
