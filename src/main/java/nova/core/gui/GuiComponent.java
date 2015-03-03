package nova.core.gui;

import java.util.Optional;

import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.SidedEventBus;
import nova.core.game.Game;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.gui.render.Graphics;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.PacketHandler;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector2i;

/**
 * Defines a basic gui component. A component can be added to
 * {@link AbstractGuiContainer}, the root container is a {@link Gui}.
 *
 * @param <O> Self reference
 * @param <T> {@link NativeGuiComponent} type
 */
@SuppressWarnings("unchecked")
public abstract class GuiComponent<O extends GuiComponent<O, T>, T extends NativeGuiComponent> implements Identifiable, EventListener<GuiEvent>, PacketHandler {

	protected String qualifiedName;
	private String uniqueID;
	private T nativeElement;

	protected Optional<Vector2i> preferredSize = Optional.empty();
	protected Optional<Vector2i> minimumSize = Optional.empty();
	protected Optional<Vector2i> maximumSize = Optional.empty();
	protected Optional<Background> background = Optional.empty();

	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isMouseOver = false;

	private SidedEventBus<ComponentEvent<?>> eventListenerList = new SidedEventBus<ComponentEvent<?>>(this::dispatchNetworkEvent);
	private EventBus<GuiEvent> listenerList = new EventBus<GuiEvent>();

	/**
	 * Parent container instance. The instance will be populated once added to a
	 * {@link AbstractGuiContainer}.
	 */
	protected Optional<AbstractGuiContainer<?, ?>> parentContainer = Optional.empty();

	public GuiComponent(String uniqueID, Class<T> nativeClass) {
		this.uniqueID = uniqueID;
		this.qualifiedName = uniqueID;
		Game.instance.guiComponentFactory.applyNativeComponent(this, nativeClass);
	}

	private void dispatchNetworkEvent(SidedEventBus.SidedEvent event) {
		getParentGui().ifPresent((e) -> e.dispatchNetworkEvent((ComponentEvent<?>) event, this));
	}

	public Optional<Gui> getParentGui() {
		return parentContainer.isPresent() ? parentContainer.get().getParentGui() : Optional.empty();
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

	public O setPreferredSize(int width, int height) {
		return setPreferredSize(new Vector2i(width, height));
	}

	public O setMinimumSize(int width, int height) {
		return setMinimumSize(new Vector2i(width, height));
	}

	public O setMaximumSize(int width, int height) {
		return setMaximumSize(new Vector2i(width, height));
	}

	public Optional<Vector2i> getPreferredSize() {
		return preferredSize.isPresent() ? preferredSize : nativeElement.getPreferredSize();
	}

	/**
	 * Sets the requested size for this component. Only works if the parent
	 * container's {@link GuiLayout} makes use of it.
	 *
	 * @param size preferred size of the component
	 * @return This component
	 */
	public O setPreferredSize(Vector2i size) {
		preferredSize = Optional.of(size);
		return (O) this;
	}

	public Optional<Vector2i> getMinimumSize() {
		return minimumSize.isPresent() ? minimumSize : nativeElement.getMinimumSize();
	}

	/**
	 * Sets the minimal size of this component. It indicates that this component
	 * shouldn't be shrinked below that size.
	 *
	 * @param size minimal size of the component
	 * @return This component
	 */
	public O setMinimumSize(Vector2i size) {
		minimumSize = Optional.of(size);
		return (O) this;
	}

	public Optional<Vector2i> getMaximumSize() {
		return maximumSize.isPresent() ? maximumSize : nativeElement.getMaximumSize();
	}

	/**
	 * Sets the maximal size of this component. It indicates that this component
	 * shouldn't be stretched beyond that size.
	 *
	 * @param size maximal size of the component
	 * @return This component
	 */
	public O setMaximumSize(Vector2i size) {
		maximumSize = Optional.of(size);
		return (O) this;
	}

	/**
	 * Sets the {@link Background} of this component.
	 * 
	 * @param background Background to use
	 * @return This component
	 */
	public O setBackground(Background background) {
		this.background = Optional.of(background.clone());
		return (O) this;
	}

	public Optional<Background> getBackground() {
		return background;
	}

	/**
	 * Call this when the component's state has changed and needs to be
	 * re-rendered. The native component is requested to infer
	 * {@link #render(int, int, Graphics)} after.
	 */
	protected void repaint() {
		nativeElement.requestRender();
	}

	/**
	 * @return Native component element
	 */
	public T getNative() {
		return nativeElement;
	}

	public void setNativeComponent(T nativeElement) {
		this.nativeElement = nativeElement;
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

	public final void preRender(int mouseX, int mouseY, Graphics graphics) {
		isMouseOver = getOutline().contains(mouseX, mouseY);
		if (background.isPresent())
			background.get().draw(graphics, getOutline().getDimension());
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
	public <EVENT extends GuiEvent> O onGuiEvent(EventListener<EVENT> listener, Class<EVENT> clazz) {
		listenerList.add(listener, clazz);
		return (O) this;
	}

	// External listener

	// TODO Should be <EVENT extends ComponentEvent<O>> but that somehow
	// destroys everything.
	public <EVENT extends ComponentEvent<?>> O onEvent(EventListener<EVENT> listener, Class<EVENT> clazz, Side side) {
		eventListenerList.add(listener, clazz, side);
		return (O) this;
	}

	public <EVENT extends ComponentEvent<?>> O onEvent(EventListener<EVENT> listener, Class<EVENT> clazz) {
		return onEvent(listener, clazz, Side.CLIENT);
	}

	/**
	 * Renders the component. A super call to this method ensures that the draw
	 * event is passed to subsequent event listeners.
	 *
	 * @param mouseX Mouse position in X-axis on screen
	 * @param mouseY Mouse position in Y-axis on screen
	 * @param graphics {@link Graphics} object used to draw on screen
	 */
	public void render(int mouseX, int mouseY, Graphics graphics) {
		if (getBackground().isPresent()) {
			getBackground().get().draw(graphics, getOutline().getDimension());
		}
		onEvent(new GuiEvent.RenderEvent(graphics, mouseX, mouseY));
	}

	@Override
	public final String getID() {
		return uniqueID;
	}

	/**
	 * <p>
	 * Will return the fully qualified name for this component used to pull from
	 * the parent GUI. Every component is indexed via
	 * {@code parent.child.subchild}, it will find the element recursive. The
	 * qualified name is always relative to the GUI, thus using
	 * {@code parent.child.subchild} will also return the proper component when
	 * getting pulled from {@code parent.child}. If this component isn't added
	 * to a parent container, the qualified name will equal {@link #getID()}.
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
