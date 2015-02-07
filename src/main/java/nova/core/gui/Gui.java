package nova.core.gui;

import java.util.Optional;

import nova.core.gui.factory.GuiEventFactory;
import nova.core.gui.nativeimpl.NativeGui;
import nova.core.network.Packet;

/**
 * Root container for GUI
 */
public class Gui extends AbstractGuiContainer<Gui, NativeGui> {

	public final String modID;

	protected Gui(String uniqueID, String modID) {
		super(uniqueID, NativeGui.class);
		this.modID = modID;
	}

	protected void dispatchNetworkEvent(ComponentEvent<?> event, GuiComponent<?, ?> sender) {
		Packet packet = getNative().createPacket();
		GuiEventFactory.instance.get().constructPacket(event, this, packet, event.getSyncID());
		getNative().dispatchNetworkEvent(packet);

	}

	@Override
	protected Optional<Gui> getParentGui() {
		// TODO Parented GUIs?
		return Optional.of(this);
	}
}
