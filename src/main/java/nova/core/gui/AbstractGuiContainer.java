package nova.core.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import nova.core.gui.GuiEvent.ResizeEvent;
import nova.core.gui.layout.BorderLayout;
import nova.core.gui.layout.GuiLayout;
import nova.core.gui.nativeimpl.NativeContainer;

/**
 * This class provides container for GUI elements
 */
public abstract class AbstractGuiContainer<T extends NativeContainer> extends GuiElement<T> {

	private GuiLayout layout = new BorderLayout();
	private HashMap<String, GuiElement<?>> children = new HashMap<String, GuiElement<?>>();

	public AbstractGuiContainer(String uniqueID) {
		super(uniqueID);
		this.registerListener(this::onResized, ResizeEvent.class);
	}

	/**
	 * @return Immutable collection of elements inside this container
	 */
	public Collection<GuiElement<?>> getChildElements() {
		return Collections.unmodifiableCollection(children.values());
	}

	/**
	 * Returns a child {@link GuiElement} based on its qualified name.
	 * 
	 * @param qualifiedName qualified name of the sub element
	 * @return The requested {@link GuiElement} or {@code null} if not present.
	 * 
	 * @see GuiElement#getQualifiedName()
	 * @see AbstractGuiContainer#getChildElement(String, Class)
	 */
	public GuiElement<?> getChildElement(String qualifiedName) {
		// TODO untested.
		if (qualifiedName.startsWith(getQualifiedName())) {
			qualifiedName = qualifiedName.substring(getQualifiedName().length());
		}
		int dot = qualifiedName.indexOf(".");
		if (dot == -1) {
			return children.get(qualifiedName);
		}
		GuiElement<?> subContainer = children.get(qualifiedName.substring(0, dot - 1));
		if (subContainer instanceof AbstractGuiContainer) {
			return ((AbstractGuiContainer<?>) subContainer).getChildElement(qualifiedName.substring(dot + 1));
		}
		return null;
	}

	/**
	 * Will return a child element that matches the provided subclass of
	 * {@link GuiElement}.
	 * 
	 * @param qualifiedName qualified name of the sub element
	 * @param clazz class of the requested {@link GuiElement}
	 * 
	 * @return The requested {@link GuiElement} or {@code null} if not present /
	 *         the type doesn't match.
	 */
	@SuppressWarnings("unchecked")
	public <E extends GuiElement<?>> E getChildElement(String qualifiedName, Class<T> clazz) {
		GuiElement<?> element = getChildElement(qualifiedName);
		if (clazz.isInstance(element))
			return (E) element;
		return null;
	}

	/**
	 * Sets layout of this container
	 * 
	 * @param layout {@link GuiLayout} to set
	 * @return This GuiContainer
	 * @throws NullPointerException if the provided layout is {@code null}.
	 */
	public AbstractGuiContainer<T> setLayout(GuiLayout layout) {
		if (layout == null)
			throw new NullPointerException();
		this.layout = layout;
		layout.revalidate(this);
		return this;
	}

	/**
	 * Processes an event, i.e. sends it to each children
	 * 
	 * @param event {@link GuiEvent} to process
	 */
	public void onEvent(GuiEvent event) {
		super.onEvent(event);
		getChildElements().stream().forEach((e) -> {
			e.onEvent(event);
		});
	}

	/**
	 * Adds {@link GuiElement} to this container
	 * 
	 * @param element {@link GuiCanvas} to add
	 * @param properties Properties for the Layout
	 * @return This GuiContainer
	 * 
	 * @see GuiLayout#add(GuiElement, AbstractGuiContainer, Object[])
	 */
	public AbstractGuiContainer<T> addElement(GuiElement<?> element, Object... properties) {
		if (element == null)
			throw new NullPointerException();
		element.parentContainer = Optional.of(this);
		element.updateQualifiedName();
		children.put(element.getID(), element);
		layout.add(element, this, properties);
		return this;
	}

	/**
	 * Removes {@link GuiElement}. Shoudln't be used unless really needed as it
	 * requires the sub element to update its qualified name using
	 * {@link #updateQualifiedName()}.
	 * 
	 * @param element {@link GuiElement} to remove
	 * @return This GuiContainer
	 */
	public AbstractGuiContainer<T> removeElement(GuiElement<?> element) {
		if (element == null)
			throw new NullPointerException();
		children.remove(element);
		layout.remove(element);
		element.updateQualifiedName();
		return this;
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
}
