package nova.core.gui.nativeimpl;

import nova.core.entity.Entity;
import nova.core.gui.Gui;
import nova.core.gui.GuiEvent;
import nova.core.gui.Outline;
import nova.core.gui.factory.GuiEventFactory;
import nova.core.network.Packet;
import nova.core.util.transform.Vector3i;

public interface NativeGui extends NativeContainer {

	// TODO This is a little bit inconvenient
	public Packet createPacket();

	public void dispatchNetworkEvent(Packet packet);

	public default void bind(Entity entity, Vector3i pos) {

	}

	public default void unbind() {

	}

	/**
	 * Called when the GUI was resized and the child components need to
	 * re-validate their layout. The new size has to be set before calling this.
	 *
	 * @param oldOutline Old {@link Outline}
	 */
	public default void onResized(Outline oldOutline) {
		getComponent().onEvent(new GuiEvent.ResizeEvent(oldOutline));
	}

	public default void recieveNetworkEvent(Packet packet) {
		Gui gui = (Gui) getComponent();
		gui.triggerEvent(GuiEventFactory.instance.get().constructEvent(packet, gui));
	}
}
