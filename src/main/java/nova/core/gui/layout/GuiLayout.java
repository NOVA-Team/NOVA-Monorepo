package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.util.transform.vector.Vector2i;

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
	public void revalidate(AbstractGuiContainer<?, ?> container);

	public void add(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, Object... parameters);

	public void remove(GuiComponent<?, ?> component);

	public default Vector2i getMinimumSize(GuiComponent<?, ?> component) {
		return Vector2i.zero;
	}
}
