package nova.core.gui.nativeimpl;

import nova.core.gui.GuiComponent;
import nova.core.gui.GuiEvent.KeyEvent;
import nova.core.gui.GuiEvent.KeyEvent.EnumKeyState;
import nova.core.gui.GuiEvent.MouseEvent;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseButton;
import nova.core.gui.GuiEvent.MouseEvent.EnumMouseState;
import nova.core.gui.GuiEvent.MouseWheelEvent;
import nova.core.gui.KeyManager.Key;
import nova.core.gui.Outline;
import nova.core.gui.render.Graphics;
import nova.core.util.transform.Vector2i;

import java.util.Optional;

//TODO Make this generic even if it means writing novels when extending.

/**
 * The native interface for any {@link GuiComponent}.
 *
 * @author Vic Nightfall
 */
public interface NativeGuiComponent {

	public GuiComponent<?, ?> getComponent();

	public Outline getOutline();

	public void setOutline(Outline outline);

	/**
	 * Override this in case the component needs a specific dimension. Generally
	 * components should be designed with a flexible size, however some GUI
	 * systems might not respect that.
	 *
	 * @return preferred size of the native component
	 */
	public default Optional<Vector2i> getPreferredSize() {
		return Optional.empty();
	}

	/**
	 * Override for a minimum size. Will equal {@link #getPreferredSize()} by
	 * default.
	 *
	 * @return minimal size of the native component
	 */
	public default Optional<Vector2i> getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Override for a maximum size. Will equal {@link #getPreferredSize()} by
	 * default.
	 *
	 * @return maximal size of the native component
	 */
	public default Optional<Vector2i> getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Added to support GUI systems that don't re-render the components on every
	 * frame.
	 */
	public void requestRender();

	public default void render(int mouseX, int mouseY, Graphics graphics) {
		if (getComponent().isVisible()) {
			getComponent().preRender(mouseX, mouseY, graphics);
			getComponent().render(mouseX, mouseY, graphics);
		}
	}

	public default void onMousePressed(int mouseX, int mouseY, EnumMouseButton button, EnumMouseState state) {
		if (getComponent().isActive()) {
			getComponent().onEvent(new MouseEvent(mouseX, mouseY, button, state));
		}
	}

	public default void onMouseWheelTurned(int scrollAmount) {
		if (getComponent().isActive()) {
			getComponent().onEvent(new MouseWheelEvent(scrollAmount));
		}
	}

	public default void onKeyPressed(Key key, char character, EnumKeyState state) {
		if (getComponent().isActive()) {
			getComponent().onEvent(new KeyEvent(key, character, state));
		}
	}
}
