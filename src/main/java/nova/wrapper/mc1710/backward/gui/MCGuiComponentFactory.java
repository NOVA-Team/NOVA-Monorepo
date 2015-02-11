package nova.wrapper.mc1710.backward.gui;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.Gui;
import nova.core.gui.components.Button;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.nativeimpl.NativeButton;
import nova.core.gui.nativeimpl.NativeContainer;
import nova.core.gui.nativeimpl.NativeGui;

public class MCGuiComponentFactory extends GuiComponentFactory {

	public MCGuiComponentFactory() {
		registerNativeComponent(NativeGui.class, (component) -> new MCGui((Gui) component));
		registerNativeComponent(NativeContainer.class, (component) -> new MCGuiContainer((AbstractGuiContainer<?, ?>) component));
		registerNativeComponent(NativeButton.class, (component) -> new MCButton((Button) component));
	}
}
