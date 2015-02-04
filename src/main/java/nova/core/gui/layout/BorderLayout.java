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
		PAGE_START(1), PAGE_END(1), LINE_START(1), LINE_END(1), CENTER(2), NORTH(0), EAST(0), SOUTH(0), WEST(0);

		public final int priority;

		private EnumBorderRegion(int priority) {
			this.priority = priority;
		}
	}

	@Override
	public BorderLayoutConstraints constraints() {
		return new BorderLayoutConstraints();
	}
}