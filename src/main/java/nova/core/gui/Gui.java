package nova.core.gui;

import java.util.Optional;

import nova.core.gui.factory.GuiEventFactory;
import nova.core.gui.factory.GuiFactory;
import nova.core.gui.nativeimpl.NativeGui;
import nova.core.loader.NovaMod;
import nova.core.network.Packet;

/**
 * Root container for GUI
 */
public class Gui extends AbstractGuiContainer<Gui, NativeGui> {

	protected String modID;

	protected Gui(String uniqueID) {
		super(uniqueID, NativeGui.class);
	}

	/**
	 * Initializes this GUI with a {@link NovaMod} mod id, you shouldn't be
	 * using this unless really necessary, in general it's done by the
	 * {@link GuiFactory}.
	 * 
	 * @param modID NOVA mod id
	 */
	public void setModID(String modID) {
		this.modID = modID;
	}

	/**
	 * Returns the {@link NovaMod} mod id referenced by this GUI.
	 * 
	 * @return NOVA mod id
	 */
	public String getModID() {
		return modID;
	}

	protected void dispatchNetworkEvent(ComponentEvent<?> event, GuiComponent<?, ?> sender) {
		Packet packet = getNative().createPacket();
		GuiEventFactory.instance.get().constructPacket(event, this, packet, event.getSyncID());
		getNative().dispatchNetworkEvent(packet);

	}

	/**
	 * Binds the GUI, called when displayed.
	 * 
	 * @param constraints {@link GuiConstraints} to initialize this GUI with.
	 */
	public void bind(GuiConstraints constraints) {
		onEvent(new GuiEvent.BindEvent(this, constraints));
		getNative().bind(constraints);
		repaint();
	}

	/**
	 * Unbind the GUI, called after getting replaced by another GUI.
	 */
	public void unbind() {
		onEvent(new GuiEvent.UnBindEvent(this));
		getNative().unbind();
	}

	@Override
	protected Optional<Gui> getParentGui() {
		// TODO Parented GUIs?
		return Optional.of(this);
	}
}
