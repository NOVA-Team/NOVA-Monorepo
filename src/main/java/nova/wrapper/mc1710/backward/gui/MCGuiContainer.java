package nova.wrapper.mc1710.backward.gui;

import java.util.ArrayList;
import java.util.List;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.nativeimpl.NativeContainer;

public class MCGuiContainer implements NativeContainer {
	
	private AbstractGuiContainer<?, ?> component;
	private List<GuiComponent<?, ?>> components = new ArrayList<>();
	private Outline outline = Outline.empty;
	
	public MCGuiContainer(AbstractGuiContainer<?, ?> component) {
		this.component = component;
	}
	
	@Override
	public GuiComponent<?, ?> getComponent() {
		return component;
	}

	@Override
	public Outline getOutline() {
		return outline;
	}

	@Override
	public void setOutline(Outline outline) {
		this.outline = outline;
	}

	@Override
	public void requestRender() {
		
	}

	@Override
	public void addElement(GuiComponent<?, ?> element) {
		components.add(element);
	}

	@Override
	public void removeElement(GuiComponent<?, ?> element) {
		components.remove(element);
	}
}
