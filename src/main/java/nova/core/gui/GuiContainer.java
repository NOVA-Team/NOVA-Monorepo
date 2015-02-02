package nova.core.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nova.core.gui.nativeimpl.NativeContainer;

/**
 * This class provides container for GUI elements
 */
public class GuiContainer extends GuiElement<NativeContainer> {

	private GuiLayout layout = new GuiLayout.BorderLayout();
	private LinkedList<GuiCanvas> children = new LinkedList<GuiCanvas>();

	public GuiContainer(String uniqueID) {
		super(uniqueID);
	}

	/**
	 * @return Immutable list of elements inside this container
	 */
	public List<GuiCanvas> getChildElements() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * Sets layout of this container
	 * 
	 * @param layout {@link GuiLayout} to set
	 * @return This GuiContainer
	 */
	public GuiContainer setLayout(GuiLayout layout) {
		this.layout = layout;
		layout.reposition(this);
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
	 * Adds {@link GuiCanvas} to this container
	 * 
	 * @param element {@link GuiCanvas} to add
	 * @param properties Properties for
	 *        {@link GuiLayout#add(GuiCanvas, GuiContainer, Object...)
	 *        GuiLayout.add}
	 * @return This GuiContainer
	 */
	public GuiContainer addElement(GuiCanvas element, Object... properties) {
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
	public GuiContainer removeElement(GuiCanvas element) {
		children.remove(element);
		layout.remove(element);
		return this;
	}
}
