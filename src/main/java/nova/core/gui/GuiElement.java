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
	protected String qualifiedName;

	private T nativeElement;
	private EventListenerList<GuiElementEvent> eventListenerList = new EventListenerList<GuiElementEvent>();
	private EventListenerList<GuiEvent> listenerList = new EventListenerList<GuiEvent>();
	protected Outline preferredOutline;

	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isMouseOver = false;

	// TODO Recursive call or field? Same goes for the qualified name.
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
		this.qualifiedName = uniqueID;
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
	 * controls positioning. (If you are a layout don't mind the @deprecated)
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
	public final String getID() {
		return uniqueID;
	}

	/**
	 * <p>
	 * Will return the fully qualified name for this element used to pull from
	 * the parent GUI. Every element is indexed via "parent.child.subchild", it
	 * will find the element recursive. The qualified name is always relative to
	 * the GUI, thus using {@code parent.child.subchild} will also return the
	 * proper element when getting pulled from {@code parent.child}. If this
	 * element isn't added to a parent container, the qualified name will equal
	 * {@link #getID()}.
	 * </p>
	 * 
	 * <p>
	 * You should always use the full qualified name if possible, as it is
	 * guaranteed to result in the same element on whatever sub component you
	 * request it.
	 * </p>
	 * 
	 * @return Full qualified unique index for the element.
	 */
	public final String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * Only implemented by {@link AbstractGuiContainer} to update the qualified
	 * name of its children in case it's been removed from the parent container.
	 */
	protected void updateQualifiedName() {

	}
}
