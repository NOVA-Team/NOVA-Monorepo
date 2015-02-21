package nova.wrapper.mc1710.backward.gui;

import nova.core.gui.render.Graphics;

public interface DrawableGuiComponent {
	
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics);
	
}
