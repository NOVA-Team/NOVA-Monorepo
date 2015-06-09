package nova.wrapper.mc1710.wrapper.gui;

import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.nativeimpl.NativeGuiComponent;
import nova.core.gui.render.Graphics;

// TODO Keyboard and mouse interaction
public class MCGuiComponent<T extends GuiComponent<?, ?>> implements NativeGuiComponent, DrawableGuiComponent {

	protected final T component;
	protected Outline outline = Outline.empty;

	public MCGuiComponent(T component) {
		this.component = component;
	}

	@Override
	public T getComponent() {
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
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		getComponent().getNative().render(mouseX, mouseY, graphics);
	}
}
