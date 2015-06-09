package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.layout.Constraints.FlowLayoutConstraints;
import nova.core.util.math.MathUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends AbstractGuiLayout<FlowLayoutConstraints> {

	private Anchor anchor;
	private List<GuiComponent<?, ?>> components = new ArrayList<>();

	public FlowLayout() {
		this(Anchor.WEST);
	}

	public FlowLayout(Anchor anchor) {
		super(FlowLayoutConstraints.class);
		if (anchor == Anchor.CENTER) {
			throw new IllegalArgumentException("Cannot specify " + anchor + " as anchor for FlowLayout!");
		}
		this.anchor = anchor;
	}

	public Anchor getAnchor() {
		return anchor;
	}

	@Override
	public void revalidate(AbstractGuiContainer<?, ?> container) {
		revalidate(container.getOutline(), true);
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}

	@Override
	protected void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, FlowLayoutConstraints constraints) {
		if (components.contains(component)) {
			throw new LayoutException("Tried to add " + component.getID() + " which was already present.");
		}
		components.add(component);
	}

	@Override
	public Vector2D getMinimumSize(GuiComponent<?, ?> component) {
		return revalidate(component.getOutline(), false);
	}

	private Vector2D revalidate(Outline outline, boolean apply) {
		int minX = 0, minY = 0, size = 0, offset = 0;
		int width = outline.getWidth();
		int height = outline.getHeight();
		Vector2D start = anchor.getStart(outline.setPosition(Vector2D.ZERO));

		if (anchor.axis == 1) {
			for (GuiComponent<?, ?> child : components) {
				Vector2D pref = getPreferredSizeOf(child);
				if (offset + pref.getX() > width) {
					minY += size;
					size = offset = 0;
				}
				if (apply) {
					setOutlineOf(child, new Outline(anchor.offset(start, offset).add(new Vector2D(0, minY)), pref));
				}
				offset += pref.getX();
				size = (int) MathUtil.max(size, pref.getY());
			}
			minY += size;
		} else {
			for (GuiComponent<?, ?> child : components) {
				Vector2D pref = getPreferredSizeOf(child);
				if (offset + pref.getY() > height) {
					minX += size;
					size = 0;
				}
				if (apply) {
					setOutlineOf(child, new Outline(anchor.offset(start, offset).add(new Vector2D(minX, 0)), pref));
				}
				offset += pref.getY();
				size = (int) MathUtil.max(size, pref.getX());
			}
			minX += size;
		}
		return new Vector2D(minX, minY);
	}
}
