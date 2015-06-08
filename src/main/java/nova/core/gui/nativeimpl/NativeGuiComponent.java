package nova.core.gui.nativeimpl;

import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.render.Graphics;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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
	public default Optional<Vector2D> getPreferredSize() {
		return Optional.empty();
	}

	/**
	 * Override for a minimum size. Will equal {@link #getPreferredSize()} by
	 * default.
	 *
	 * @return minimal size of the native component
	 */
	public default Optional<Vector2D> getMinimumSize() {
		return getPreferredSize();
	}

	/**
	 * Override for a maximum size. Will equal {@link #getPreferredSize()} by
	 * default.
	 *
	 * @return maximal size of the native component
	 */
	public default Optional<Vector2D> getMaximumSize() {
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
}
