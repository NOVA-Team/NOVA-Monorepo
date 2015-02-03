package nova.core.gui;

import java.util.Optional;

import nova.core.gui.GuiEvent.SidedEvent;
import nova.core.gui.nativeimpl.NativeGui;

/**
 * Root container for GUI
 */
public abstract class Gui extends AbstractGuiContainer<NativeGui> {

	private Gui(String uniqueID) {
		super(uniqueID);
	}

	// TODO Has to construct packets and send them over the network using the
	// qualified name of the components.
	protected void dispatchNetworkEvent(SidedEvent event, GuiElement<?> sender) {
		// event.getTarget();
		sender.getQualifiedName();
	}

	@Override
	protected Optional<Gui> getParentGui() {
		// TODO Parented GUIs?
		return Optional.of(this);
	}
}
