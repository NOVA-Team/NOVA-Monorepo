package nova.core.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import nova.core.gui.ComponentEvent.ResizeEvent;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.layout.BorderLayout;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeContainer;
import nova.core.util.exception.NovaException;
import nova.core.util.transform.Vector2i;

/**
 * This class provides container for {@link GuiComponent}
 * 
 * @param <O> Self reference
 * @param <T> {@link NativeContainer} type
 */
public abstract class AbstractGuiContainer<O extends AbstractGuiContainer<O, T>, T extends NativeContainer> extends GuiComponent<O, T> {

	private GuiLayout layout = new BorderLayout();

	private HashMap<String, GuiComponent<?, ?>> children = new HashMap<>();

	public AbstractGuiContainer(String uniqueID, Class<T> nativeClass) {
		super(uniqueID, nativeClass);
		this.onEvent(this::onResized, ResizeEvent.class);
	}

	public AbstractGuiContainer(Class<T> nativeClass) {
		this("", nativeClass);
	}

	/**
	 * @return Immutable collection of components inside this container
	 */
	public Collection<GuiComponent<?, ?>> getChildComponents() {
		return Collections.unmodifiableCollection(children.values());
	}

	/**
	 * Returns a child {@link GuiComponent} based on its qualified name.
	 *
	 * @param qualifiedName qualified name of the sub component
	 * @return The requested {@link GuiComponent} or {@code null} if not
	 *         present.
	 * @see GuiComponent#getQualifiedName()
	 * @see AbstractGuiContainer#getChildElement(String, Class)
	 */
	public Optional<GuiComponent<?, ?>> getChildElement(String qualifiedName) {
		int dot = qualifiedName.indexOf(".");
		if (dot == -1) {
			GuiComponent<?, ?> child = children.get(qualifiedName);
			if (child != null && !child.hasIdentifer()) {
				// Not funny. No you won't get that one.
				return Optional.empty();
			}
			return Optional.of(child);
		}
		GuiComponent<?, ?> subContainer = children.get(qualifiedName.substring(0, dot - 1));
		if (subContainer instanceof AbstractGuiContainer) {
			return ((AbstractGuiContainer<?, ?>) subContainer).getChildElement(qualifiedName.substring(dot + 1));
		}
		return Optional.empty();
	}

	/**
	 * Will return a child component that matches the provided subclass of
	 * {@link GuiComponent}.
	 * 
	 * @param <E> type of the requested {@link GuiComponent}
	 * @param qualifiedName qualified name of the sub component
	 * @param clazz class of the requested {@link GuiComponent}
	 * @return The requested {@link GuiComponent} or {@code null} if not present
	 *         / the type doesn't match.
	 */
	@SuppressWarnings("unchecked")
	public <E extends GuiComponent<?, ?>> Optional<E> getChildElement(String qualifiedName, Class<E> clazz) {
		GuiComponent<?, ?> component = getChildElement(qualifiedName).get();
		if (clazz.isInstance(component))
			return Optional.of((E) component);
		return Optional.empty();
	}

	/**
	 * Sets layout of this container
	 *
	 * @param layout {@link GuiLayout} to set
	 * @return This GuiContainer
	 * @throws NullPointerException if the provided layout is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	public O setLayout(GuiLayout layout) {
		this.layout = Objects.requireNonNull(layout);
		layout.revalidate(this);
		return (O) this;
	}

	/**
	 * Processes an event, i.e. sends it to each children
	 *
	 * @param event {@link GuiEvent} to process
	 */
	@Override
	public void onEvent(GuiEvent event) {
		super.onEvent(event);
		for (GuiComponent<?, ?> component : getChildComponents()) {
			component.onEvent(event);
		}
	}

	@Override
	public void onMouseEvent(MouseEvent event) {
		super.onMouseEvent(event);
		for (GuiComponent<?, ?> component : getChildComponents()) {
			// Change the mouse position to be relative to the child component's
			// outline.
			Vector2i position = component.getOutline().getPosition();
			component.onMouseEvent(new MouseEvent(event.mouseX - position.x, event.mouseY - position.y, event.button, event.state));
		}
	}

	/**
	 * Adds {@link GuiComponent} to this container.
	 *
	 * @param component {@link GuiCanvas} to add
	 * @param properties Properties for the Layout
	 * @return This GuiContainer
	 * @see GuiLayout#add(GuiComponent, AbstractGuiContainer, Object[])
	 */
	@SuppressWarnings("unchecked")
	public O add(GuiComponent<?, ?> component, Object... properties) {
		Objects.requireNonNull(component);
		component.parentContainer = Optional.of(this);
		children.put(component.getID(), component);
		layout.add(component, this, properties);
		getNative().addElement(component);
		component.triggerEvent(new ComponentEvent.AddEvent(component, this));
		return (O) this;
	}

	/**
	 * Removes {@link GuiComponent}. Shouldn't be used unless really needed as
	 * it requires the sub component to update its qualified name using
	 * updateQualifiedName().
	 *
	 * @param component {@link GuiComponent} to remove
	 * @return This GuiContainer
	 */
	@SuppressWarnings("unchecked")
	public O removeElement(GuiComponent<?, ?> component) {
		Objects.requireNonNull(component);
		if (!children.containsValue(component))
			throw new NovaException("Component couldn't be removed from parent container as it wasn't a child.");
		component.triggerEvent(new ComponentEvent.RemoveEvent(component, this));
		children.remove(component);
		layout.remove(component);
		component.updateQualifiedName();
		getNative().removeElement(component);
		return (O) this;
	}

	public void onResized(ResizeEvent event) {
		revalidate();
	}

	/**
	 * Called when the size changed to update the positions of the child
	 * components.
	 * 
	 * @see GuiLayout#revalidate(AbstractGuiContainer)
	 */
	public void revalidate() {
		layout.revalidate(this);
	}

	@Override
	public Optional<Vector2i> getMinimumSize() {
		return Optional.of(super.getMinimumSize().orElse(layout.getMinimumSize(this)));
	}

	@Override
	protected void updateQualifiedName() {
		super.updateQualifiedName();
		children.forEach((k, v) -> {
			v.updateQualifiedName();
		});
	}

	@Override
	protected void repaint() {
		children.forEach((k, v) -> {
			v.repaint();
		});
		super.repaint();
	}
}
