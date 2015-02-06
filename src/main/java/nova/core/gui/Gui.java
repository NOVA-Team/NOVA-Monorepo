package nova.core.gui;

import java.util.Optional;

import nova.core.gui.nativeimpl.NativeGui;
import nova.core.network.Packet;

/**
 * Root container for GUI
 */
public class Gui extends AbstractGuiContainer<Gui, NativeGui> {

	public final String modID;

	protected Gui(String uniqueID, String modID) {
		super(uniqueID);
		this.modID = modID;
	}

	// TODO Do something about the optional id parameter of Synced
	protected void dispatchNetworkEvent(ComponentEvent<?> event, GuiComponent<?, ?> sender) {
		Packet packet = getNative().createPacket();
		GuiFactory.get(modID).constructPacket(event, this, packet, 0);
		getNative().dispatchNetworkEvent(packet);

	}

	@Override
	protected Optional<Gui> getParentGui() {
		// TODO Parented GUIs?
		return Optional.of(this);
	}
}
