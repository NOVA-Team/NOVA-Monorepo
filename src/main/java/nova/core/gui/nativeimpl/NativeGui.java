package nova.core.gui.nativeimpl;

import nova.core.event.SidedEventListenerList;
import nova.core.event.SidedEventListenerList.SidedEvent;
import nova.core.gui.GuiEvent;
import nova.core.gui.Outline;

public interface NativeGui extends NativeContainer {

	public void dispatchNetworkEvent(SidedEventListenerList.SidedEvent sidedEvent);

	/**
	 * Called when the GUI was resized and the child components need to
	 * re-validate their layout. The new size has to be set before calling this.
	 * 
	 * @param oldOutline
	 */
	public default void onResized(Outline oldOutline) {
		getElement().onEvent(new GuiEvent.ResizeEvent(oldOutline));
	}
}
