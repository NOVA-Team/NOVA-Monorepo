package nova.core.gui;

import nova.core.gui.nativeimpl.NativeContainer;

public class GuiContainer extends AbstractGuiContainer<GuiContainer, NativeContainer> {

	public GuiContainer(String uniqueID) {
		super(uniqueID, NativeContainer.class);
	}
}
