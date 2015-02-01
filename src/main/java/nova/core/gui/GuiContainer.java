package nova.core.gui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nova.core.gui.nativeimpl.NativeCanvas;

public class GuiContainer extends GuiElement<NativeCanvas> {

	private GuiLayout layout = new GuiLayout.BorderLayout();
	private LinkedList<GuiComponent> children = new LinkedList<GuiComponent>();

	public GuiContainer(String uniqueID) {
		super(uniqueID);
	}

	public List<GuiComponent> getChildElements() {
		return Collections.unmodifiableList(children);
	}

	public void setLayout(GuiLayout layout) {
		this.layout = layout;
		layout.reposition(this);
	}

	public void onEvent(GuiEvent event) {
		getChildElements().stream().forEach((e) -> {
			e.onEvent(event);
		});
	}

	public void addElement(GuiComponent element, Object... properties) {
		children.add(element);
		layout.add(element, this, properties);
	}

	public void removeElement(GuiComponent element) {
		children.remove(element);
		layout.remove(element);
	}
}
