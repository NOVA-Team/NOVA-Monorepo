package nova.wrapper.mc1710.wrapper.gui;

import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.gui.render.Graphics;
import nova.wrapper.mc1710.wrapper.gui.MCGui.MCContainer;

public interface DrawableGuiComponent extends NativeGuiComponent {

	public void draw(int mouseX, int mouseY, float partial, Graphics graphics);

	public default void onAddedToContainer(MCContainer container) {

	}

	public default MCCanvas getCanvas() {
		return getGui().getCanvas();
	}

	public default MCGui getGui() {
		return (MCGui) getComponent().getParentGui().get().getNative();
	}
}
