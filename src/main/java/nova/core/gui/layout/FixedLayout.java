package nova.core.gui.layout;

import java.util.HashMap;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.util.transform.Vector2i;

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

			Vector2i preferredSize = getPreferredSizeOf(component);
			Vector2i computedPosition = position.getPositionOf(container.getOutline().getDimension());

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
