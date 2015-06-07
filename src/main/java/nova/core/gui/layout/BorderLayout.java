package nova.core.gui.layout;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.layout.Constraints.BorderConstraints;
import nova.core.util.math.MathUtil;
import nova.core.util.transform.vector.Vector2i;

import java.util.EnumMap;

/**
 * A basic layout that splits the parent's container up into multiple regions.
 * It's an implementation of Swing's BorderLayout.
 *
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
		Vector2i dimension = outline.getDimension();

		GuiComponent<?, ?> cComp = components.get(Anchor.CENTER);
		GuiComponent<?, ?> wComp = components.get(Anchor.WEST);
		GuiComponent<?, ?> eComp = components.get(Anchor.EAST);
		GuiComponent<?, ?> nComp = components.get(Anchor.NORTH);
		GuiComponent<?, ?> sComp = components.get(Anchor.SOUTH);

		if (nComp != null) {
			setSizeOf(nComp, new Vector2i(dimension.x, nComp.getOutline().getHeight()));
		}
		if (sComp != null) {
			setSizeOf(sComp, new Vector2i(dimension.x, sComp.getOutline().getHeight()));
		}

		Vector2i nDim = getPreferredSizeOf(nComp);
		Vector2i sDim = getPreferredSizeOf(sComp);

		if (wComp != null) {
			setSizeOf(wComp, new Vector2i(wComp.getOutline().getWidth(), dimension.y - nDim.y - sDim.y));
		}
		if (wComp != null) {
			setSizeOf(eComp, new Vector2i(eComp.getOutline().getWidth(), dimension.y - nDim.y - sDim.y));
		}

		Vector2i wDim = getPreferredSizeOf(wComp);
		Vector2i eDim = getPreferredSizeOf(eComp);
		Vector2i cDim = new Vector2i(dimension.x - wDim.x - eDim.x, dimension.y - nDim.y - sDim.y).min(getMaximumSizeOf(cComp));

		Vector2i v4 = new Vector2i(0, dimension.y - nDim.y - sDim.y);
		wDim = wDim.max(v4).min(getMaximumSizeOf(wComp));
		eDim = eDim.max(v4).min(getMaximumSizeOf(eComp));

		Vector2i v5 = new Vector2i(dimension.x, 0);
		nDim = nDim.max(v5).min(getMaximumSizeOf(nComp));
		sDim = sDim.max(v5).min(getMaximumSizeOf(sComp));

		// Centers the border components
		int wOffset = nDim.y + (dimension.y - nDim.y - sDim.y) / 2 - wDim.y / 2;
		int eOffset = nDim.y + (dimension.y - nDim.y - sDim.y) / 2 - eDim.y / 2;
		int nOffset = (dimension.x - nDim.x) / 2;
		int sOffset = (dimension.x - sDim.x) / 2;

		// Center the center component
		int cOffsetX = (dimension.x - wDim.x - eDim.x) / 2 - cDim.x / 2;
		int cOffsetY = (dimension.y - nDim.y - sDim.y) / 2 - cDim.y / 2;

		setOutlineOf(cComp, new Outline(new Vector2i(wDim.x + cOffsetX, nDim.y + cOffsetY), cDim));
		setOutlineOf(wComp, new Outline(new Vector2i(0, wOffset), wDim));
		setOutlineOf(eComp, new Outline(new Vector2i(dimension.x - eDim.x, eOffset), eDim));
		setOutlineOf(nComp, new Outline(new Vector2i(nOffset, 0), nDim));
		setOutlineOf(sComp, new Outline(new Vector2i(sOffset, dimension.y - sDim.y), sDim));
	}

	@Override
	public Vector2i getMinimumSize(GuiComponent<?, ?> component) {
		Vector2i wDim = getMiniumSizeOf(components.get(Anchor.WEST));
		Vector2i eDim = getMiniumSizeOf(components.get(Anchor.EAST));
		Vector2i nDim = getMiniumSizeOf(components.get(Anchor.NORTH));
		Vector2i sDim = getMiniumSizeOf(components.get(Anchor.SOUTH));
		Vector2i cDim = getMiniumSizeOf(components.get(Anchor.CENTER));

		return new Vector2i(MathUtil.max(nDim.x, wDim.x + cDim.x + eDim.x, sDim.x), nDim.y + MathUtil.max(cDim.y, wDim.y, eDim.y) + sDim.y);
	}

	@Override
	protected void addImpl(GuiComponent<?, ?> component, AbstractGuiContainer<?, ?> parent, BorderConstraints constraints) {
		if (components.containsKey(constraints))
			throw new LayoutException("BorderLayout doesn't allow multiple elements taking up the same region!");
		components.put(constraints.region, component);
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}
}
