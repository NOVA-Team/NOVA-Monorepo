package nova.core.gui;

import nova.core.event.EventListener;
import nova.core.event.EventListenerList;
import nova.core.gui.nativeimpl.NativeCanvas;
import nova.core.network.PacketReceiver;
import nova.core.network.PacketSender;
import nova.core.render.Artist;
import nova.core.util.Identifiable;

public abstract class GuiElement<T extends NativeCanvas> implements Identifiable, EventListener<GuiEvent>, PacketSender, PacketReceiver {

	private String uniqueID;
	private T nativeElement;
	private EventListenerList<GuiElementEvent> eventListenerList = new EventListenerList<GuiElementEvent>();

	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isMouseOver = false;

	public GuiElement(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public AxisAlignedRect getShape() {
		return nativeElement.getShape();
	}

	public void setShape(AxisAlignedRect rect) {
		nativeElement.setShape(rect);
	}

	// TODO inserted by some sort of factory?
	protected void setNativeElement(T nativeElement) {
		this.nativeElement = nativeElement;
		nativeElement.requestRender();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}

	public final void preRender(int mouseX, int mouseY, Artist artist) {
		isMouseOver = getShape().contains(mouseX, mouseY);
	}

	@Override
	public void onEvent(GuiEvent event) {
		// TODO Should also have multiple event listeners.
	}

	public void triggerEvent(GuiElementEvent event) {
		// eventListenerList.publish(event);
	}

	public <EVENT extends GuiElementEvent> GuiElement<T> registerEventHandler(EventListener<EVENT> listener, Class<EVENT> clazz) {
		// TODO doesn't discriminate the handlers based on their desired event
		// type.
		// eventListenerList.add(listener);
		return this;
	}

	public void render(int mouseX, int mouseY, Artist artist) {

	}

	@Override
	public String getID() {
		return uniqueID;
	}
}
