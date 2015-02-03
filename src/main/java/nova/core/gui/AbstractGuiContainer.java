package nova.core.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	private LinkedList<GuiElement<?>> children = new LinkedList<GuiElement<?>>();

	public AbstractGuiContainer(String uniqueID) {
		super(uniqueID);
		this.registerListener(this::onResized, ResizeEvent.class);
	}

	/**
	 * @return Immutable list of elements inside this container
	 */
	public List<GuiElement<?>> getChildElements() {
		return Collections.unmodifiableList(children);
	}

	// TODO Will work using "parent.child.subchild"
	public GuiElement<?> getChildElement(String uniqueIdentifier) {
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
		element.parentContainer = Optional.of(this);
		children.add(element);
		layout.add(element, this, properties);
		return this;
	}

	/**
	 * Removes {@link GuiCanvas}
	 * 
	 * @param element {@link GuiCanvas} to remove
	 * @return This GuiContainer
	 */
	public AbstractGuiContainer<T> removeElement(GuiElement<?> element) {
		children.remove(element);
		layout.remove(element);
		return this;
	}

	public void onResized(ResizeEvent event) {
		layout.revalidate(this);
	}
}
