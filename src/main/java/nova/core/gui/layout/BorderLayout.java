package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.layout.Constraints.BorderConstraints;
import nova.core.util.math.MathUtil;
import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.EnumMap;

/**
 * A basic layout that splits the parent's container up into multiple regions.
 * It's an implementation of Swing's BorderLayout.
 * @author Vic Nightfall
 * @see java.awt.BorderLayout
 */
public class BorderLayout extends AbstractGuiLayout<BorderConstraints> {

	private final EnumMap<Anchor, GuiComponent<?, ?>> components = new EnumMap<>(Anchor.class);

	public BorderLayout() {
		super(BorderConstraints.class);
	}

	@Override
	public void revalidate(AbstractGuiContainer<?, ?> parent) {
		Outline outline = parent.getOutline();
		Vector2D dimension = outline.getDimension();

		GuiComponent<?, ?> cComp = components.get(Anchor.CENTER);
		GuiComponent<?, ?> wComp = components.get(Anchor.WEST);
		GuiComponent<?, ?> eComp = components.get(Anchor.EAST);
		GuiComponent<?, ?> nComp = components.get(Anchor.NORTH);
		GuiComponent<?, ?> sComp = components.get(Anchor.SOUTH);

		if (nComp != null) {
			setSizeOf(nComp, new Vector2D(dimension.getX(), nComp.getOutline().getHeight()));
		}
		if (sComp != null) {
			setSizeOf(sComp, new Vector2D(dimension.getX(), sComp.getOutline().getHeight()));
		}

		Vector2D nDim = getPreferredSizeOf(nComp);
		Vector2D sDim = getPreferredSizeOf(sComp);

		if (wComp != null) {
			setSizeOf(wComp, new Vector2D(wComp.getOutline().getWidth(), dimension.getY() - nDim.getY() - sDim.getY()));
		}
		if (wComp != null) {
			setSizeOf(eComp, new Vector2D(eComp.getOutline().getWidth(), dimension.getY() - nDim.getY() - sDim.getY()));
		}

		Vector2D wDim = getPreferredSizeOf(wComp);
		Vector2D eDim = getPreferredSizeOf(eComp);
		Vector2D cDim = Vector2DUtil.min(new Vector2D(dimension.getX() - wDim.getX() - eDim.getX(), dimension.getY() - nDim.getY() - sDim.getY()), getMaximumSizeOf(cComp));

		Vector2D v4 = new Vector2D(0, dimension.getY() - nDim.getY() - sDim.getY());
		wDim = Vector2DUtil.min(Vector2DUtil.max(wDim, v4), getMaximumSizeOf(wComp));
		eDim = Vector2DUtil.min(Vector2DUtil.max(eDim, v4), getMaximumSizeOf(eComp));

		Vector2D v5 = new Vector2D(dimension.getX(), 0);
		nDim = Vector2DUtil.min(Vector2DUtil.max(nDim, v5), getMaximumSizeOf(nComp));
		sDim = Vector2DUtil.min(Vector2DUtil.max(sDim, v5), getMaximumSizeOf(sComp));

		// Centers the border components
		int wOffset = (int) (nDim.getY() + (dimension.getY() - nDim.getY() - sDim.getY()) / 2 - wDim.getY() / 2);
		int eOffset = (int) (nDim.getY() + (dimension.getY() - nDim.getY() - sDim.getY()) / 2 - eDim.getY() / 2);
		int nOffset = (int) ((dimension.getX() - nDim.getX()) / 2);
		int sOffset = (int) ((dimension.getX() - sDim.getX()) / 2);

		// Center the center component
		int cOffsetX = (int) ((dimension.getX() - wDim.getX() - eDim.getX()) / 2 - cDim.getX() / 2);
		int cOffsetY = (int) ((dimension.getY() - nDim.getY() - sDim.getY()) / 2 - cDim.getY() / 2);

		setOutlineOf(cComp, new Outline(new Vector2D(wDim.getX() + cOffsetX, nDim.getY() + cOffsetY), cDim));
		setOutlineOf(wComp, new Outline(new Vector2D(0, wOffset), wDim));
		setOutlineOf(eComp, new Outline(new Vector2D(dimension.getX() - eDim.getX(), eOffset), eDim));
		setOutlineOf(nComp, new Outline(new Vector2D(nOffset, 0), nDim));
		setOutlineOf(sComp, new Outline(new Vector2D(sOffset, dimension.getY() - sDim.getY()), sDim));
	}

	@Override
	public Vector2D getMinimumSize(GuiComponent<?, ?> component) {
		Vector2D wDim = getMiniumSizeOf(components.get(Anchor.WEST));
		Vector2D eDim = getMiniumSizeOf(components.get(Anchor.EAST));
		Vector2D nDim = getMiniumSizeOf(components.get(Anchor.NORTH));
		Vector2D sDim = getMiniumSizeOf(components.get(Anchor.SOUTH));
		Vector2D cDim = getMiniumSizeOf(components.get(Anchor.CENTER));

		return new Vector2D(MathUtil.max(nDim.getX(), wDim.getX() + cDim.getX() + eDim.getX(), sDim.getX()), nDim.getY() + MathUtil.max(cDim.getY(), wDim.getY(), eDim.getY()) + sDim.getY());
	}

	@Override
	protected void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, BorderConstraints constraints) {
		if (components.containsKey(constraints)) {
			throw new LayoutException("BorderLayout doesn't allow multiple elements taking up the same region!");
		}
		components.put(constraints.region, component);
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}
}
