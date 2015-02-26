package nova.core.gui.layout;

import java.util.EnumMap;

import nova.core.gui.AbstractGuiContainer;
import nova.core.gui.GuiComponent;
import nova.core.gui.Outline;
import nova.core.gui.layout.Constraints.BorderConstraints;
import nova.core.util.math.MathUtil;
import nova.core.util.transform.Vector2i;

/**
 * A basic layout that splits the parent's container up into multiple regions.
 * It's an implementation of Swing's BorderLayout.
 *
 * @author Vic Nightfall
 * @see java.awt.BorderLayout
 */
public class BorderLayout extends AbstractGuiLayout<BorderConstraints> {

	private Vector2i minimumSize;

	public BorderLayout() {
		super(BorderConstraints.class);
	}

	private final EnumMap<Anchor, GuiComponent<?, ?>> components = new EnumMap<>(Anchor.class);

	@Override
	public void revalidate(AbstractGuiContainer<?, ?> parent) {
		Outline outline = parent.getOutline();
		Vector2i dimension = outline.getDimension();

		GuiComponent<?, ?> cComp = components.get(Anchor.CENTER);
		GuiComponent<?, ?> wComp = components.get(Anchor.WEST);
		GuiComponent<?, ?> eComp = components.get(Anchor.EAST);
		GuiComponent<?, ?> nComp = components.get(Anchor.NORTH);
		GuiComponent<?, ?> sComp = components.get(Anchor.SOUTH);

		Vector2i wDim = getPreferredSizeOf(wComp);
		Vector2i eDim = getPreferredSizeOf(eComp);
		Vector2i nDim = getPreferredSizeOf(nComp);
		Vector2i sDim = getPreferredSizeOf(sComp);
		Vector2i cDim = Vector2i.zero;

		Vector2i overAllocated = Vector2i.zero;

		if (cComp != null) {
			Vector2i pref = new Vector2i(dimension.x - wDim.x - eDim.x, dimension.y - nDim.y - sDim.y);
			cDim = pref.max(getPreferredSizeOf(cComp)).min(getMaximumSizeOf(cComp));
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

			overAllocated = new Vector2i(dimension.x - cDim.x - wDim.x - eDim.x, dimension.y - cDim.y - nDim.y - sDim.y).inverse().max(Vector2i.zero);
		} else {
			overAllocated = new Vector2i(dimension.x - wDim.x - eDim.x, dimension.y - nDim.y - sDim.y).inverse().max(Vector2i.zero);
		}

		if (overAllocated.x > 0) {
			Vector2i v3 = wComp != null && eComp != null ? overAllocated.divide(2) : overAllocated;
			wDim = new Vector2i(wDim.x - v3.x, wDim.y).max(Vector2i.zero);
			eDim = new Vector2i(eDim.x - v3.x, eDim.y).max(Vector2i.zero);
		}
		if (overAllocated.y > 0) {
			Vector2i v3 = nComp != null && sComp != null ? overAllocated.divide(2) : overAllocated;
			nDim = new Vector2i(nDim.x, wDim.y - v3.y).max(Vector2i.zero);
			sDim = new Vector2i(sDim.x, eDim.y - v3.y).max(Vector2i.zero);
		}

		if (cComp != null) {
			cDim = cDim.min(new Vector2i(dimension.x - wDim.x - eDim.x, dimension.y - nDim.y - sDim.y));
		}

		Vector2i v4 = new Vector2i(0, dimension.y - nDim.y - sDim.y);
		wDim = wDim.max(v4).min(getMaximumSizeOf(wComp));
		eDim = eDim.max(v4).min(getMaximumSizeOf(eComp));

		Vector2i v5 = new Vector2i(dimension.x, 0);
		nDim = nDim.max(v5).min(getMaximumSizeOf(nComp));
		sDim = sDim.max(v5).min(getMaximumSizeOf(sComp));

		// Centers the border components
		int wOffset = (int) ((dimension.y - wDim.y) / 2D);
		int eOffset = (int) ((dimension.y - eDim.y) / 2D);
		int nOffset = (int) ((dimension.x - nDim.x) / 2D);
		int sOffset = (int) ((dimension.x - sDim.x) / 2D);

		setOutlineOf(cComp, new Outline(new Vector2i(wDim.x, nDim.y), cDim));
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
			throw new RuntimeException("BorderLayout doesn't allow multiple elements taking up the same region!");
		components.put(constraints.region, component);
	}

	@Override
	public void remove(GuiComponent<?, ?> component) {
		components.remove(component);
	}

	@Override
	public BorderConstraints constraints() {
		return new BorderConstraints();
	}
}