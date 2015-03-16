package nova.core.gui.components.inventory;

import java.util.Optional;

import nova.core.gui.Gui;
import nova.core.gui.GuiComponent;
import nova.core.gui.GuiEvent.BindEvent;
import nova.core.gui.nativeimpl.NativeSlot;
import nova.core.inventory.Inventory;
import nova.core.item.Item;
import nova.core.util.exception.NovaException;

/**
 * A slot is a {@link GuiComponent} that can hold {@link Item Items}.
 * 
 * @author Vic Nightfall
 */
public class Slot extends GuiComponent<Slot, NativeSlot> {

	private final String inventoryID;
	private final int slotID;
	protected Inventory inventory;

	/**
	 * Creates a new Slot instance. The inventory id specifies which
	 * {@link Inventory} the slot will apply to, has to be specified on the
	 * parent GUI with the {@link BindEvent} and
	 * {@link Gui#addInventory(String, Inventory)}
	 * 
	 * @param uniqueID
	 * @param inventoryID
	 * @param slotID
	 */
	public Slot(String uniqueID, String inventoryID, int slotID) {
		super(uniqueID, NativeSlot.class);
		this.inventoryID = inventoryID;
		this.slotID = slotID;

		onGuiEvent(this::onBind, BindEvent.class);
	}

	public Optional<Item> getItem() {
		if (inventory == null)
			return Optional.empty();
		return inventory.get(slotID);
	}

	public Optional<Item> removeItem(int amount) {
		if (inventory == null)
			return Optional.empty();
		return inventory.remove(slotID, amount);
	}

	public boolean setItem(Item item) {
		if (inventory == null)
			return false;
		return inventory.set(slotID, item);
	}

	public int addItem(Item item) {
		if (inventory == null)
			return item.count();
		return inventory.add(slotID, item);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public int getSlotID() {
		return slotID;
	}

	protected void onBind(BindEvent event) {
		inventory = event.gui.getInventory(inventoryID);
		if (inventory == null) {
			throw new NovaException("Unsupplied inventory \"" + inventoryID + "\" for Slot " + getID());
		}
	}
}
