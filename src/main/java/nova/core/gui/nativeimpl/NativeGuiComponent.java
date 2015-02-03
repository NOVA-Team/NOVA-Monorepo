package nova.core.gui.nativeimpl;

import nova.core.gui.GuiComponent;
import nova.core.gui.GuiEvent.KeyEvent;
import nova.core.gui.GuiEvent.KeyEvent.EnumKeyState;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseButton;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseState;
import nova.core.gui.GuiEvent.MouseWheelEvent;
import nova.core.gui.Outline;
import nova.core.render.model.Model;

/**
 * The native interface for any {@link GuiComponent}.
 * 
 * @author Vic Nightfall
 *
 */
public interface NativeGuiComponent {

	public void applyElement(GuiComponent<?> component);

	public GuiComponent<?> getElement();

	public Outline getOutline();

	public void setOutline(Outline outline);

	/**
	 * Added to support GUI systems that don't re-render the components on every
	 * frame.
	 */
	public void requestRender();

	public default void render(int mouseX, int mouseY, Model artist) {
		if (getElement().isVisible()) {
			getElement().preRender(mouseX, mouseY, artist);
			getElement().render(mouseX, mouseY, artist);
		}
	}

	public default void onMousePressed(int mouseX, int mouseY, EnumMouseButton button, EnumMouseState state) {
		if (getElement().isActive()) {
			getElement().onEvent(new MouseEvent(mouseX, mouseY, button, state));
		}
	}

	public default void onMouseWheelTurned(int scrollAmount) {
		if (getElement().isActive()) {
			getElement().onEvent(new MouseWheelEvent(scrollAmount));
		}
	}

	// TODO Create a wrapper for key codes.
	public default void onKeyPressed(int keyCode, char character, EnumKeyState state) {
		if (getElement().isActive()) {
			getElement().onEvent(new KeyEvent(keyCode, character, state));
		}
	}
}
