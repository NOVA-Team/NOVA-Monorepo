package nova.core.gui.flexible;

import nova.core.component.ComponentProvider;
import nova.core.inventory.Inventory;

/**
 * An image specifies a texture to be rendered
 * @author Calclavia
 */
public class Slot extends ComponentUI {

	private final String inventoryID;
	private final int slotID;
	protected Inventory inventory;

	public Slot(ComponentProvider provider, String inventoryID, int slotID) {
		super(provider);
		this.inventoryID = inventoryID;
		this.slotID = slotID;
	}

	@Override
	public String getID() {
		return "slot";
	}
}
