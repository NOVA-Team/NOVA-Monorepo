package nova.wrapper.mc1710.backward.gui;

import java.util.ArrayList;
import java.util.List;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.nativeimpl.NativeContainer;
import nova.core.render.model.Model;

public class MCGuiContainer implements NativeContainer, DrawableGuiComponent {
	
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

	@Override
	public void draw(int mouseX, int mouseY, float partial) {
		getComponent().render(mouseX, mouseY, new Model());
		components.forEach((component) -> ((DrawableGuiComponent)component.getNative()).draw(mouseX, mouseY, partial));
	}
}
