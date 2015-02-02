package nova.core.gui;

import nova.core.event.EventListener;
import nova.core.event.EventListenerList;
import nova.core.gui.nativeimpl.NativeCanvas;
import nova.core.network.PacketReceiver;
import nova.core.network.PacketSender;
import nova.core.render.model.Model;
import nova.core.util.Identifiable;

/**
 * Defines basic GuiElement
 * @param <T> {@link NativeCanvas} type
 */
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

	/**
	 * @return Shape of this GuiElement
	 * @see Rectangle
	 */
	public Rectangle getShape()
	{
		return nativeElement.getShape();
	}

	/**
	 * Sets shape of this GuiElement
	 * @param rect {@link Rectangle} to use as shape
	 */
	public void setShape(Rectangle rect)
	{
		nativeElement.setShape(rect);
	}

	// TODO inserted by some sort of factory?
	protected void setNativeElement(T nativeElement) {
		this.nativeElement = nativeElement;
		nativeElement.requestRender();
	}

	/**
	 * @return Whether this element is active
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Sets activity state for this element
	 * @param isActive New state
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return Whether this element is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets visibility of this element
	 * @param isVisible New visibility
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return Whether mouse is over this element
	 */
	public boolean isMouseOver() {
		return isMouseOver;
	}

	public final void preRender(int mouseX, int mouseY, Model artist) {
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

	/**
	 * Does rendering logic
	 * @param mouseX Mouse position in X-axis on screen
	 * @param mouseY Mouse position in Y-axis on screen
	 * @param artist {@link nova.core.render.model.Model} to use
	 */
	public void render(int mouseX, int mouseY, Model artist) {

	}

	@Override
	public String getID() {
		return uniqueID;
	}
}
