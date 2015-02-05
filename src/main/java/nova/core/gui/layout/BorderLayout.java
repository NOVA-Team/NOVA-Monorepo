package nova.core.gui.layout;

import java.util.HashMap;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.layout.LayoutConstraints.BorderLayoutConstraints;
import nova.core.util.transform.Vector2i;

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

	// TODO HIGHLY untested
	@Override
	public void revalidate(AbstractGuiContainer<?, ?> parent) {
		Outline outline = parent.getOutline();
		Vector2i dimension = outline.getDimension();

		GuiComponent<?, ?> cComp = components.get(EnumBorderRegion.CENTER);
		GuiComponent<?, ?> wComp = components.get(EnumBorderRegion.WEST);
		GuiComponent<?, ?> eComp = components.get(EnumBorderRegion.EAST);
		GuiComponent<?, ?> nComp = components.get(EnumBorderRegion.NORTH);
		GuiComponent<?, ?> sComp = components.get(EnumBorderRegion.SOUTH);

		Vector2i wDim = getPreferredSizeOf(wComp);
		Vector2i eDim = getPreferredSizeOf(eComp);
		Vector2i nDim = getPreferredSizeOf(nComp);
		Vector2i sDim = getPreferredSizeOf(sComp);
		Vector2i cDim = Vector2i.zero;

		Vector2i overAllocated = Vector2i.zero;
		Vector2i space = dimension;

		if (cComp != null) {
			Vector2i pref = new Vector2i(dimension.x - wDim.x - nDim.x, dimension.y - nDim.y - sDim.y);
			cDim = pref.min(getMaximumSizeOf(cComp));
			Vector2i cOver = pref.subtract(cDim).divide(2);

			if (cOver.x > 0) {
				if (wComp != null)
					wDim = wDim.add(new Vector2i(cOver.x, 0));
				if (eComp != null)
					eDim = eDim.add(new Vector2i(cOver.x, 0));
			}
			if (cOver.y > 0) {
				if (nComp != null)
					nDim = nDim.add(new Vector2i(0, cOver.y));
				if (sComp != null)
					sDim = sDim.add(new Vector2i(0, cOver.y));
			}

			Vector2i v1 = dimension.subtract(cDim);
			overAllocated = v1.subtract(wDim).subtract(nDim).subtract(eDim).subtract(sDim).inverse().max(Vector2i.zero);
			space = space.subtract(cDim);

		} else {
			overAllocated = dimension.subtract(wDim).subtract(nDim).subtract(eDim).subtract(sDim).inverse().max(Vector2i.zero);
		}

		Vector2i v2 = space.divide(2);

		if (overAllocated.x > 0) {
			Vector2i v3 = wComp != null && eComp != null ? v2 : space;
			wDim = new Vector2i(v3.x, eDim.y);
			eDim = new Vector2i(v3.x, wDim.y);
		}
		if (overAllocated.y > 0) {
			Vector2i v3 = nComp != null && sComp != null ? v2 : space;
			wDim = new Vector2i(eDim.x, v3.y);
			eDim = new Vector2i(wDim.x, v3.y);
		}

		if (cComp != null) {
			cDim = cDim.min(dimension.subtract(wDim).subtract(nDim).subtract(eDim).subtract(sDim));
		}

		// Centers the border components
		int wOffset = (int) ((dimension.y + wDim.y) / 2D);
		int eOffset = (int) ((dimension.y + eDim.y) / 2D);
		int nOffset = (int) ((dimension.x + nDim.x) / 2D);
		int sOffset = (int) ((dimension.x + sDim.x) / 2D);

		setOutlineOf(cComp, new Outline(new Vector2i(wDim.x, nDim.y), dimension));
		setOutlineOf(wComp, new Outline(new Vector2i(0, wOffset), wDim));
		setOutlineOf(eComp, new Outline(new Vector2i(dimension.x - wDim.x, eOffset), wDim));
		setOutlineOf(nComp, new Outline(new Vector2i(nOffset, 0), eDim));
		setOutlineOf(sComp, new Outline(new Vector2i(sOffset, dimension.y - sDim.y), sDim));
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
		CENTER, NORTH, EAST, SOUTH, WEST;
	}

	@Override
	public BorderLayoutConstraints constraints() {
		return new BorderLayoutConstraints();
	}
}