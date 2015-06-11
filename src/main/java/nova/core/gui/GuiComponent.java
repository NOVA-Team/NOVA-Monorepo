package nova.core.gui;

import nova.core.event.EventBus;
import nova.core.event.EventListener;
import nova.core.event.SidedEventBus;
import nova.core.event.SidedEventBus.SidedEvent;
import nova.core.gui.ComponentEvent.ComponentEventListener;
import nova.core.gui.ComponentEvent.ResizeEvent;
import nova.core.gui.ComponentEvent.SidedComponentEvent;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.factory.GuiComponentFactory;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.gui.render.Graphics;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Syncable;
import nova.core.util.Identifiable;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import se.jbee.inject.Dependency;

import java.util.Optional;
import java.util.UUID;

/**
 * Defines a basic gui component. A component can be added to
 * {@link AbstractGuiContainer}, the root container is a {@link Gui}.
 *
 * A component may or may not specify a unique identifier, but keep in mind that
 * they won't be able to receive any events on the {@link Side#SERVER server}
 * side if none is present.
 * @param <O> Self reference
 * @param <T> {@link NativeGuiComponent} type
 */
@SuppressWarnings("unchecked")
public abstract class GuiComponent<O extends GuiComponent<O, T>, T extends NativeGuiComponent> implements Identifiable, EventListener<GuiEvent>, Syncable {

	private final boolean hasIdentifier;
	private final String uniqueID;
	private static final GuiComponentFactory factory;

	static {
		factory = Game.injector().resolve(Dependency.dependency(GuiComponentFactory.class));
	}

	protected Optional<Vector2D> preferredSize = Optional.empty();
	protected Optional<Vector2D> minimumSize = Optional.empty();
	protected Optional<Vector2D> maximumSize = Optional.empty();
	protected Optional<Background> background = Optional.empty();
	/**
	 * Parent container instance. The instance will be populated once added to a
	 * {@link AbstractGuiContainer}.
	 */
	protected Optional<AbstractGuiContainer<?, ?>> parentContainer = Optional.empty();
	private String qualifiedName;
	private T nativeElement;
	private boolean isActive = true;
	private boolean isVisible = true;
	private boolean isDisplayed = true;
	private boolean isMouseOver = false;
	private SidedEventBus<ComponentEvent> componentEventBus = new SidedEventBus<ComponentEvent>(this::dispatchNetworkEvent);
	private EventBus<GuiEvent> guiEventBus = new EventBus<GuiEvent>();

	public GuiComponent(String uniqueID, Class<T> nativeClass) {
		if (uniqueID.length() > 0) {
			this.uniqueID = uniqueID;
			this.hasIdentifier = true;
		} else {
			this.uniqueID = UUID.randomUUID().toString();
			this.hasIdentifier = false;
		}
		this.qualifiedName = uniqueID;
		factory.applyNativeComponent(this, nativeClass);
	}

	/**
	 * <p>
	 * Creates a new component without provided id. Every implementor should
	 * specify a constructor without id parameter.
	 * </p>
	 * <p>
	 * Implement this or use an empty string.
	 * </p>
	 * @param nativeClass
	 */
	public GuiComponent(Class<T> nativeClass) {
		this("", nativeClass);
	}

	private void dispatchNetworkEvent(SidedEvent event) {
		getParentGui().ifPresent((e) -> e.dispatchNetworkEvent((SidedComponentEvent) event, this));
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
	 * @param outline {@link Outline} to use as outline
	 * @see #setPreferredSize(Vector2D)
	 */
	@Deprecated
	public void setOutlineNative(Outline outline) {
		Outline oldOutline = getNative().getOutline();
		nativeElement.setOutline(outline);
		if (!oldOutline.getDimension().equals(outline.getDimension())) {
			triggerEvent(new ResizeEvent(this, oldOutline));
		}
		revalidate();
	}

	public O setPreferredSize(int width, int height) {
		return setPreferredSize(new Vector2D(width, height));
	}

	public O setMinimumSize(int width, int height) {
		return setMinimumSize(new Vector2D(width, height));
	}

	public O setMaximumSize(int width, int height) {
		return setMaximumSize(new Vector2D(width, height));
	}

	public Optional<Vector2D> getPreferredSize() {
		return preferredSize.isPresent() ? preferredSize : nativeElement.getPreferredSize();
	}

	/**
	 * Sets the requested size for this component. Only works if the parent
	 * container's {@link GuiLayout} makes use of it.
	 * @param size preferred size of the component
	 * @return This component
	 */
	public O setPreferredSize(Vector2D size) {
		preferredSize = Optional.of(size);
		revalidate();
		return (O) this;
	}

	public Optional<Vector2D> getMinimumSize() {
		return minimumSize.isPresent() ? minimumSize : nativeElement.getMinimumSize();
	}

	/**
	 * Sets the minimal size of this component. It indicates that this component
	 * shouldn't be shrinked below that size.
	 * @param size minimal size of the component
	 * @return This component
	 */
	public O setMinimumSize(Vector2D size) {
		minimumSize = Optional.of(size);
		revalidate();
		return (O) this;
	}

	public Optional<Vector2D> getMaximumSize() {
		return maximumSize.isPresent() ? maximumSize : nativeElement.getMaximumSize();
	}

	/**
	 * Sets the maximal size of this component. It indicates that this component
	 * shouldn't be stretched beyond that size.
	 * @param size maximal size of the component
	 * @return This component
	 */
	public O setMaximumSize(Vector2D size) {
		maximumSize = Optional.of(size);
		revalidate();
		return (O) this;
	}

	public Optional<Background> getBackground() {
		return background;
	}

	/**
	 * Sets the {@link Background} of this component.
	 * @param background Background to use
	 * @return This component
	 */
	public O setBackground(Background background) {
		this.background = Optional.of(background.clone());
		return (O) this;
	}

	/**
	 * Call this when the component's state has changed and needs to be
	 * re-rendered. The native component is requested to infer
	 * {@link #render(int, int, Graphics)} after.
	 * @see #revalidate()
	 */
	protected void repaint() {
		nativeElement.requestRender();
	}

	/**
	 * Call this when the component's state has changed and the parent layout
	 * has to be refreshed.
	 * @see #repaint()
	 */
	protected void revalidate() {
		// TODO Rewrite this so that it doesn't cause infinite loops
		// getParentContainer().ifPresent(AbstractGuiContainer::revalidate);
	}

	/**
	 * Returns the native component element for this component. <b>This should
	 * only be used by the wrapper!</b>
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
	 * @param isVisible New visibility
	 */
	public void setVisible(boolean isVisible) {
		if (this.isVisible != isVisible) {
			this.isVisible = isVisible;
			repaint();
		}
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplay(boolean isDisplayed) {
		if (this.isDisplayed != isDisplayed) {
			this.isDisplayed = isDisplayed;
			revalidate();
		}
	}

	/**
	 * @return Whether mouse is over this component
	 */
	public boolean isMouseOver() {
		return isMouseOver;
	}

	public final void preRender(int mouseX, int mouseY, Graphics graphics) {
		isMouseOver = getOutline().setPosition(Vector2D.ZERO).contains(mouseX, mouseY);
		if (background.isPresent()) {
			background.get().draw(graphics, getOutline().getDimension());
		}
	}

	@Override
	public void onEvent(GuiEvent event) {
		guiEventBus.publish(event);
	}

	/**
	 * Special handling for mouse events, the position has to be relative to the
	 * parent component.
	 * @param event {@link MouseEvent}
	 */
	public void onMouseEvent(MouseEvent event) {
		guiEventBus.publish(event);
	}

	/**
	 * Triggers an event for the external listeners registered with
	 * registerEventListener
	 * @param event ComponentEvent to trigger
	 */
	public void triggerEvent(ComponentEvent event) {
		componentEventBus.publish(event);
	}

	// Internal listener
	public <EVENT extends GuiEvent> O onGuiEvent(EventListener<EVENT> listener, Class<EVENT> clazz) {
		guiEventBus.add(listener, clazz);
		return (O) this;
	}

	// External listener

	public <EVENT extends ComponentEvent> O onEvent(ComponentEventListener<EVENT, O> listener, Class<EVENT> clazz, Side side) {
		if (side == Side.SERVER && !hasIdentifierRecursive()) {
			throw new GuiComponentException("Components without unique identifier can't recieve events on the server side!");
		}
		componentEventBus.add(listener, clazz, side);
		return (O) this;
	}

	public <EVENT extends ComponentEvent> O onEvent(ComponentEventListener<EVENT, O> listener, Class<EVENT> clazz) {
		return onEvent(listener, clazz, Side.CLIENT);
	}

	public <EVENT extends ComponentEvent> O onEvent(EventListener<EVENT> listener, Class<EVENT> clazz, Side side) {
		if (side == Side.SERVER && !hasIdentifierRecursive()) {
			throw new GuiComponentException("Components without unique identifier can't recieve events on the server side!");
		}
		componentEventBus.add(listener, clazz, side);
		return (O) this;
	}

	public <EVENT extends ComponentEvent> O onEvent(EventListener<EVENT> listener, Class<EVENT> clazz) {
		return onEvent(listener, clazz, Side.CLIENT);
	}

	/**
	 * Renders the component. A super call to this method ensures that the draw
	 * event is passed to subsequent event listeners.
	 * @param mouseX Mouse position in X-axis relative to this component
	 * @param mouseY Mouse position in Y-axis relative to this component
	 * @param graphics {@link Graphics} object used to draw on screen
	 */
	public void render(int mouseX, int mouseY, Graphics graphics) {
		guiEventBus.publish(new GuiEvent.RenderEvent(graphics, mouseX, mouseY));
	}

	/**
	 * Returns the unique ID of this component. Might be an empty string if not
	 * specified.
	 */
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
	 * @return Full qualified unique index for the component.
	 */
	public final String getQualifiedName() {
		return qualifiedName;
	}

	protected final boolean hasIdentifer() {
		return hasIdentifier;
	}

	/**
	 * Returns true if all parent elements declare a unique identifier
	 * @return hasIdentifier
	 */
	protected final boolean hasIdentifierRecursive() {
		if (!hasIdentifer()) {
			return false;
		}
		Optional<AbstractGuiContainer<?, ?>> parent = getParentContainer();
		while (parent.isPresent()) {
			if (!parent.get().hasIdentifer()) {
				return false;
			}
			parent = parent.get().getParentContainer();
		}
		return true;
	}

	/**
	 * Called to recreate the qualified name when added to a component or
	 * removed.
	 */
	protected void updateQualifiedName() {
		if (parentContainer.isPresent()) {
			qualifiedName = parentContainer.get().getQualifiedName() + "." + (hasIdentifer() ? getID() : "<>");
		} else {
			qualifiedName = getID();
		}
	}
}
