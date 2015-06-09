package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.HashMap;

/**
 * A fixed layout gives every one of its components a {@link RelativePosition}.
 * The {@link GuiComponent#getPreferredSize()} is taken into account, you will
 * have to specify it in order for this layout to work properly.
 * 
 * @author Vic Nightfall
 */
public class FixedLayout extends AbstractGuiLayout<RelativePosition> {

	private HashMap<GuiComponent<?, ?>, RelativePosition> components = new HashMap<>();

	public FixedLayout() {
		super(RelativePosition.class);
	}

	@Override
	public void revalidate(AbstractGuiContainer<?, ?> container) {
		for (GuiComponent<?, ?> component : components.keySet()) {
			RelativePosition position = components.get(component);

			Vector2D preferredSize = getPreferredSizeOf(component);
			Vector2D computedPosition = position.getPositionOf(container.getOutline().getDimension());

			setOutlineOf(component, new Outline(computedPosition, preferredSize));
		}
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}

	@Override
	protected void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, RelativePosition constraints) {
		components.put(component, constraints);
	}
}
