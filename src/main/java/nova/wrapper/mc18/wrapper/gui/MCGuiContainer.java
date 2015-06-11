package nova.wrapper.mc18.wrapper.gui;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.nativeimpl.NativeContainer;
import nova.core.gui.render.Canvas;
import nova.core.gui.render.Graphics;
import nova.wrapper.mc18.wrapper.gui.MCGui.MCContainer;

import java.util.ArrayList;
import java.util.List;

public class MCGuiContainer extends MCGuiComponent<AbstractGuiContainer<?, ?>> implements NativeContainer {

	protected List<GuiComponent<?, ?>> components = new ArrayList<>();

	public MCGuiContainer(AbstractGuiContainer<?, ?> component) {
		super(component);
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
	public void onAddedToContainer(MCContainer container) {
		for (GuiComponent<?, ?> component : components) {
			((DrawableGuiComponent) component.getNative()).onAddedToContainer(container);
		}
	}

	// TODO This should be part of NovaCore -> Move to NativeContainer interface
	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		for (GuiComponent<?, ?> component : components) {
			Outline outline = component.getOutline();
			int width = outline.x1i(), height = outline.y1i();
			Canvas canvas = graphics.getCanvas();

			canvas.push();
			canvas.setScissor(component.getOutline());
			canvas.translate(width, height);
			((DrawableGuiComponent) component.getNative()).draw(mouseX - width, mouseY - height, partial, graphics);
			canvas.pop();
		}
		super.draw(mouseX, mouseY, partial, graphics);
	}
}
