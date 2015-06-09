package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Root class for {@link AbstractGuiContainer} layouts. A layout handles
 * positioning of the elements and needs to be revalidated whenever the internal
 * structure (placing, dimensions, etc...) changed.
 * 
 * @author Vic Nightfall
 */
public interface GuiLayout {

	/**
	 * Called when the parent {@link AbstractGuiContainer} gets resized to
	 * re-validate the positioning.
	 *
	 * @param container Container instance
	 */
	void revalidate(AbstractGuiContainer<?, ?> container);

	void add(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, Object... parameters);

	void remove(GuiComponent<?, ?> component);

	default Vector2D getMinimumSize(GuiComponent<?, ?> component) {
		return Vector2D.ZERO;
	}
}
