package nova.core.gui;

import java.util.Optional;

import nova.core.event.EventListener;
import nova.core.event.EventListenerList;
import nova.core.event.SidedEventListenerList;
import nova.core.gui.GuiEvent.ConstructionEvent;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.factory.GuiFactory;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.PacketReceiver;
import nova.core.network.PacketSender;
import nova.core.render.model.Model;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector2i;

/**
 * Defines a basic gui component. A component can be added to
 * {@link AbstractGuiContainer}, the root container is a {@link Gui}.
 *
 * @param <T> {@link NativeGuiComponent} type
 */
@SuppressWarnings("unchecked")
public abstract class GuiComponent<O extends GuiComponent<O, T>, T extends NativeGuiComponent> implements Identifiable, EventListener<GuiEvent>, PacketSender, PacketReceiver {

	private String uniqueID;
	protected String qualifiedName;

	private T nativeElement;
	private SidedEventListenerList<ComponentEvent<?>> eventListenerList = new SidedEventListenerList<ComponentEvent<?>>(this::dispatchNetworkEvent);
	private EventListenerList<GuiEvent> listenerList = new EventListenerList<GuiEvent>();

	protected Optional<Vector2i> preferredSize = Optional.empty();
	protected Optional<Vector2i> minimumSize = Optional.empty();
	protected Optional<Vector2i> maximumSize = Optional.empty();

	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isMouseOver = false;

	/**
	 * Parent container instance. The instance will be populated once added to a
	 * {@link AbstractGuiContainer}.
	 */
	protected Optional<AbstractGuiContainer<?, ?>> parentContainer = Optional.empty();

	private void dispatchNetworkEvent(SidedEventListenerList.SidedEvent event) {
		getParentGui().ifPresent((e) -> e.dispatchNetworkEvent((ComponentEvent<?>) event, this));
	}

	// TODO Recursive call or field? Same goes for the qualified name.
	protected Optional<Gui> getParentGui() {
		return parentContainer.isPresent() ? parentContainer.get().getParentGui() : Optional.empty();
	}

	public GuiComponent(String uniqueID, Class<T> nativeClass) {
		this.uniqueID = uniqueID;
		this.qualifiedName = uniqueID;
		GuiComponentFactory.applyNativeComponent(this, nativeClass);
	}

	public Optional<AbstractGuiContainer<?, ?>> getParentContainer() {
		return parentContainer;
	}

	/**
	 * @return Outline of this component.
	 * @see Outline
	 */
	public Outline getOutline() {
		return nativeElement.getOutline();
	}

	/**
	 * Sets the outline of this component. Shouldn't be used as the layout
	 * controls positioning. (If you are a layout don't mind the @deprecated)
	 *
	 * @param outline {@link Outline} to use as outline
	 * @see #setPreferredSize(Vector2i)
	 */
	@Deprecated
	public void setOutlineNative(Outline outline) {
		nativeElement.setOutline(outline);
	}

	/**
	 * Sets the requested size for this component. Only works if the parent
	 * container's {@link GuiLayout} makes use of it.
	 *
	 * @param size preferred size of the component
	 */
	public O setPreferredSize(Vector2i size) {
		preferredSize = Optional.of(size);
		return (O) this;
	}

	public O setPreferredSize(int width, int height) {
		return setPreferredSize(new Vector2i(width, height));
	}

	/**
	 * Sets the minimal size of this component. It indicates that this component
	 * shouldn't be shrinked below that size.
	 *
	 * @param size minimal size of the component
	 */
	public O setMinimumSize(Vector2i size) {
		minimumSize = Optional.of(size);
		return (O) this;
	}

	public O setMinimumSize(int width, int height) {
		return setMinimumSize(new Vector2i(width, height));
	}

	/**
	 * Sets the maximal size of this component. It indicates that this component
	 * shouldn't be stretched beyond that size.
	 *
	 * @param size maximal size of the component
	 */
	public O setMaximumSize(Vector2i size) {
		maximumSize = Optional.of(size);
		return (O) this;
	}

	public O setMaximumSize(int width, int height) {
		return setMaximumSize(new Vector2i(width, height));
	}

	public Optional<Vector2i> getPreferredSize() {
		return preferredSize.isPresent() ? preferredSize : nativeElement.getPreferredSize();
	}

	public Optional<Vector2i> getMinimumSize() {
		return minimumSize.isPresent() ? minimumSize : nativeElement.getMinimumSize();
	}

	public Optional<Vector2i> getMaximumSize() {
		return maximumSize.isPresent() ? maximumSize : nativeElement.getMaximumSize();
	}

	/**
	 * Call this when the component's state has changed and needs to be
	 * re-rendered. The native component is requested to infer
	 * {@link #render(int, int, Model)} after.
	 */
	protected void repaint() {
		nativeElement.requestRender();
	}

	/**
	 * @return Native component element
	 */
	protected T getNative() {
		return nativeElement;
	}

	/**
	 * Gets called right after the {@link #nativeElement} instance has been
	 * populated by a {@link GuiFactory}. Use this to pass arguments to the
	 * underlying {@link NativeGuiComponent}.
	 */
	protected void construct() {
		onEvent(new ConstructionEvent());
	}

	protected void setNativeElement(T nativeElement) {
		this.nativeElement = nativeElement;
		construct();
		repaint();
	}

	/**
	 * @return Whether this component is active
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Sets activity state for this component
	 *
	 * @param isActive New state
	 */
	public void setActive(boolean isActive) {
		if (this.isActive != isActive) {
			this.isActive = isActive;
			repaint();
		}
	}

	/**
	 * @return Whether this component is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Sets visibility of this component
	 *
	 * @param isVisible New visibility
	 */
	public void setVisible(boolean isVisible) {
		if (this.isVisible != isVisible) {
			this.isVisible = isVisible;
			repaint();
		}
	}

	/**
	 * @return Whether mouse is over this component
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

	/**
	 * Triggers an event for the external listeners registered with
	 * registerEventListener
	 * 
	 * @param event ComponentEvent to trigger
	 */
	public void triggerEvent(ComponentEvent<?> event) {
		eventListenerList.publish(event);
	}

	// Internal listener
	// TODO expose for GUI builders?

	protected <EVENT extends GuiEvent> void registerListener(EventListener<EVENT> listener, Class<EVENT> clazz) {
		listenerList.add(listener, clazz);
	}

	// External listener

	// TODO Should be <EVENT extends ComponentEvent<O>> but that somehow
	// destroys everything.
	public <EVENT extends ComponentEvent<?>> O registerEventListener(EventListener<EVENT> listener, Class<EVENT> clazz, Side side) {
		eventListenerList.add(listener, clazz, side);
		return (O) this;
	}

	public <EVENT extends ComponentEvent<?>> O registerEventListener(EventListener<EVENT> listener, Class<EVENT> clazz) {
		eventListenerList.add(listener, clazz);
		return (O) this;
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
	 * Will return the fully qualified name for this component used to pull from
	 * the parent GUI. Every component is indexed via "parent.child.subchild",
	 * it will find the element recursive. The qualified name is always relative
	 * to the GUI, thus using {@code parent.child.subchild} will also return the
	 * proper component when getting pulled from {@code parent.child}. If this
	 * component isn't added to a parent container, the qualified name will
	 * equal {@link #getID()}.
	 * </p>
	 *
	 * <p>
	 * You should always use the full qualified name if possible, as it is
	 * guaranteed to result in the same element on whatever sub component you
	 * request it.
	 * </p>
	 *
	 * @return Full qualified unique index for the component.
	 */
	public final String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * Called to recreate the qualified name when added to a component or
	 * removed.
	 */
	protected void updateQualifiedName() {
		if (parentContainer.isPresent()) {
			qualifiedName = parentContainer.get().getQualifiedName() + "." + getID();
		} else {
			qualifiedName = getID();
		}
	}
}
