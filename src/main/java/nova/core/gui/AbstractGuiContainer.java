package nova.core.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import nova.core.gui.GuiEvent.ResizeEvent;
import nova.core.gui.layout.BorderLayout;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeContainer;

/**
 * This class provides container for {@link GuiComponent}
 * @param <O> -describe me-
 * @param <T> {@link NativeContainer} type
 */
public abstract class AbstractGuiContainer<O extends AbstractGuiContainer<O, T>, T extends NativeContainer> extends GuiComponent<O, T> {

	private GuiLayout layout = new BorderLayout();

	private HashMap<String, GuiComponent<?, ?>> children = new HashMap<String, GuiComponent<?, ?>>();

	public AbstractGuiContainer(String uniqueID, Class<T> nativeClass) {
		super(uniqueID, nativeClass);
		this.registerListener(this::onResized, ResizeEvent.class);
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
		// TODO untested.
		if (qualifiedName.startsWith(getQualifiedName())) {
			qualifiedName = qualifiedName.substring(getQualifiedName().length());
		}
		int dot = qualifiedName.indexOf(".");
		if (dot == -1) {
			return Optional.of(children.get(qualifiedName));
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
		getChildComponents().stream().forEach((e) -> {
			e.onEvent(event);
		});
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
	public O addElement(GuiComponent<?, ?> component, Object... properties) {
		Objects.requireNonNull(component);
		component.parentContainer = Optional.of(this);
		component.updateQualifiedName();
		children.put(component.getID(), component);
		layout.add(component, this, properties);
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
		children.remove(component);
		layout.remove(component);
		component.updateQualifiedName();
		return (O) this;
	}

	public void onResized(ResizeEvent event) {
		layout.revalidate(this);
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
