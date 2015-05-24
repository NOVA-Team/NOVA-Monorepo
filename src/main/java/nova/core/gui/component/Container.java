package nova.core.gui.component;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.nativeimpl.NativeContainer;

public class Container extends AbstractGuiContainer<Container, NativeContainer> {

	public Container(String uniqueID) {
		super(uniqueID, NativeContainer.class);
	}

	public Container() {
		super(NativeContainer.class);
	}
}
