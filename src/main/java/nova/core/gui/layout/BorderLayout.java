package nova.core.gui.layout;

import java.util.HashMap;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.layout.LayoutConstraints.BorderLayoutConstraints;

/**
 * A basic layout that splits the parent's container up into multiple regions.
 * It's an implementation of Swing's BorderLayout.
 * 
 * @see java.awt.BorderLayout
 * @author Vic Nightfall
 *
 */
public class BorderLayout extends AbstractGuiLayout<BorderLayoutConstraints> {

	public BorderLayout() {
		super(BorderLayoutConstraints.class);
	}

	private final HashMap<BorderLayout.EnumBorderRegion, GuiComponent<?, ?>> components = new HashMap<>();

	@Override
	public void revalidate(AbstractGuiContainer<?, ?> parent) {
		for (EnumBorderRegion region : components.keySet()) {

		}
	}

	@Override
	protected void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, BorderLayoutConstraints constraints) {
		if (components.containsKey(constraints))
			throw new RuntimeException("BorderLayout doesn't allow multiple elements taking up the same region!");
		components.put(constraints.region, component);
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}

	public static enum EnumBorderRegion {
		CENTER(2, 2), NORTH(1, 1), EAST(1, 2), SOUTH(1, 1), WEST(1, 2);

		public final int priority;
		public final int axis;

		private EnumBorderRegion(int priority, int axis) {
			this.priority = priority;
			this.axis = axis;
		}
	}

	@Override
	public BorderLayoutConstraints constraints() {
		return new BorderLayoutConstraints();
	}
}