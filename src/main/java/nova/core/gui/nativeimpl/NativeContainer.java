package nova.core.gui.nativeimpl;

import nova.core.gui.GuiElement;

/**
 * A native interface for anything that can hold components.
 * 
 * @author Vic Nightfall
 *
 */
public interface NativeContainer extends NativeGuiElement {

	public void addElement(GuiElement<?> element);

	public void removeElement(GuiElement<?> element);
}
