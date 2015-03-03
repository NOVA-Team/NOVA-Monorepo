package nova.core.gui;

import java.util.Optional;

import nova.core.entity.Entity;
import nova.core.event.EventListener;
import nova.core.game.Game;
import nova.core.gui.factory.GuiEventFactory;
import nova.core.gui.factory.GuiFactory;
import nova.core.gui.nativeimpl.NativeGui;
import nova.core.gui.render.TextMetrics;
import nova.core.loader.NovaMod;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Packet;
import nova.core.util.transform.Vector3i;

/**
 * Root container for GUI
 */
public class Gui extends AbstractGuiContainer<Gui, NativeGui> {

	protected Optional<String> modID = Optional.empty();
	protected final boolean hasServerSide;

	/**
	 * Creates a nwe GUI instance with a {@link Side#SERVER server} side
	 * backend. You can register events with
	 * {@link #onEvent(EventListener, Class, Side)} specifying on which side the
	 * event gets processed. Keep the client side restrictions in mind.
	 * 
	 * @param uniqueID Unique ID of this GUI
	 * @see #Gui(String, boolean)
	 */
	public Gui(String uniqueID) {
		super(uniqueID, NativeGui.class);
		this.hasServerSide = true;
	}

	/**
	 * Creates a new GUI instance with an optional {@link Side#SERVER server}
	 * side backend. GUIs without a server side aren't able to send events over
	 * the network, adding a listener for the server side won't have any effect.
	 * Keep the client side restrictions in mind.
	 * 
	 * @param uniqueID Unique ID of this GUI
	 * @param hasServerSide Optional server side backend
	 */
	public Gui(String uniqueID, boolean hasServerSide) {
		super(uniqueID, NativeGui.class);
		this.hasServerSide = hasServerSide;
	}

	public boolean hasServerSide() {
		return hasServerSide;
	}

	/**
	 * Initializes this GUI with a {@link NovaMod} mod id, you shouldn't be
	 * using this unless really necessary, in general it's done by the
	 * {@link GuiFactory}.
	 * 
	 * @param modID NOVA mod id
	 */
	public void setModID(String modID) {
		this.modID = Optional.of(modID);
	}

	/**
	 * Returns the {@link NovaMod} mod id referenced by this GUI. Populated once
	 * registered with a {@link GuiEventFactory} or in case of an unregistered
	 * GUI, after bind. Can be used for identifying purposes.
	 * 
	 * @return NOVA mod id
	 */
	public Optional<String> getModID() {
		return modID;
	}

	protected void dispatchNetworkEvent(ComponentEvent event, GuiComponent<?, ?> sender) {
		Packet packet = Game.instance.networkManager.newPacket();
		GuiEventFactory.instance.constructPacket(event, this, packet, event.getSyncID());
		getNative().dispatchNetworkEvent(packet);
	}

	/**
	 * Binds the GUI, called when displayed.
	 * 
	 * @param entity Entity which interacted to display this GUI
	 * @param position block position
	 */
	public void bind(Entity entity, Vector3i position) {
		onEvent(new GuiEvent.BindEvent(this, entity, position));
		getNative().bind(entity, position);
		repaint();
	}

	/**
	 * Unbind the GUI, called after getting replaced by another GUI.
	 */
	public void unbind() {
		onEvent(new GuiEvent.UnBindEvent(this));
		getNative().unbind();
	}

	public TextMetrics getTextMetrics() {
		return getNative().getTextMetrics();
	}

	@Override
	public Optional<Gui> getParentGui() {
		// TODO Parented GUIs?
		return Optional.of(this);
	}
}
