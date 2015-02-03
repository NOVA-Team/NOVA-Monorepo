package nova.core.gui;

import java.util.Optional;

import nova.core.event.EventListener;
import nova.core.event.EventListenerList;
import nova.core.gui.nativeimpl.NativeGuiElement;
import nova.core.network.PacketReceiver;
import nova.core.network.PacketSender;
import nova.core.render.model.Model;
import nova.core.util.Identifiable;

/**
 * Defines basic GuiElement
 * 
 * @param <T> {@link NativeGuiElement} type
 */
public abstract class GuiElement<T extends NativeGuiElement> implements Identifiable, EventListener<GuiEvent>, PacketSender, PacketReceiver {

	private String uniqueID;
	private T nativeElement;
	private EventListenerList<GuiElementEvent> eventListenerList = new EventListenerList<GuiElementEvent>();
	private EventListenerList<GuiEvent> listenerList = new EventListenerList<GuiEvent>();
	private Outline preferredOutline;

	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isMouseOver = false;

	protected Optional<Gui> getParentGui() {
		return parentContainer.isPresent() ? parentContainer.get().getParentGui() : Optional.empty();
	}

	/**
	 * Parent container instance. The instance will be populated once added to a
	 * {@link AbstractGuiContainer}.
	 */
	protected Optional<AbstractGuiContainer<?>> parentContainer = Optional.empty();

	public GuiElement(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	/**
	 * @return Outline of this GuiElement
	 * @see Outline
	 */
	public Outline getOutline() {
		return nativeElement.getOutline();
	}

	/**
	 * Sets the outline of this GuiElement. Shouldn't be used as the layout
	 * controls positioning.
	 * 
	 * @see #setOutline(Outline)
	 * @param outline {@link Outline} to use as outline
	 */
	@Deprecated
	protected void setOutlineNative(Outline outline) {
		nativeElement.setOutline(outline);
	}

	/**
	 * Sets the requested outline for this component. Only works if the parent
	 * container's layout makes use of this.
	 * 
	 * @param outline
	 */
	public void setOutline(Outline outline) {
		preferredOutline = outline;
	}

	/**
	 * @return Native container element
	 */
	protected T getNative() {
		return nativeElement;
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
	 * 
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
	 * 
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
		isMouseOver = getOutline().contains(mouseX, mouseY);
	}

	@Override
	public void onEvent(GuiEvent event) {
		listenerList.publish(event);
	}

	public void triggerEvent(GuiElementEvent event) {
		eventListenerList.publish(event);
	}

	protected <EVENT extends GuiEvent> void registerListener(EventListener<EVENT> listener, Class<EVENT> clazz) {
		listenerList.add(listener, clazz);
	}

	public <EVENT extends GuiElementEvent> GuiElement<T> registerEventListener(EventListener<EVENT> listener, Class<EVENT> clazz) {
		eventListenerList.add(listener, clazz);
		return this;
	}

	/**
	 * Does rendering logic
	 * 
	 * @param mouseX Mouse position in X-axis on screen
	 * @param mouseY Mouse position in Y-axis on screen
	 * @param model {@link nova.core.render.model.Model} to use
	 */
	public void render(int mouseX, int mouseY, Model model) {

	}

	@Override
	public String getID() {
		return uniqueID;
	}

	// TODO Has to construct the name "parent.child.subchild". Might want to
	// buffer it.
	public String getQualifiedName() {
		return null;
	}
}
