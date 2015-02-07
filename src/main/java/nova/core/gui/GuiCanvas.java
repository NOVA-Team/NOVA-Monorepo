package nova.core.gui;

import nova.core.gui.nativeimpl.NativeGuiComponent;

public abstract class GuiCanvas<O extends GuiCanvas<O>> extends GuiComponent<O, NativeGuiComponent> {

	public GuiCanvas(String uniqueID) {
		super(uniqueID, NativeGuiComponent.class);
	}

}
