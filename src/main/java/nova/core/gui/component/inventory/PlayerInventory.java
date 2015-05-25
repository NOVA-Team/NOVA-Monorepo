package nova.core.gui.component.inventory;

import nova.core.gui.GuiComponent;
import nova.core.gui.GuiEvent.BindEvent;
import nova.core.gui.nativeimpl.NativePlayerInventory;
import nova.core.inventory.component.InventoryPlayer;

/**
 * Defines the standard player inventory. It automatically gets wrapped to the
 * player inventory and its appearance, size, and other properties might differ
 * from wrapper to wrapper.
 * 
 * @author Vic Nightfall
 */
public class PlayerInventory extends GuiComponent<PlayerInventory, NativePlayerInventory> {

	protected InventoryPlayer playerInventory;

	public PlayerInventory(String uniqueID) {
		super(uniqueID, NativePlayerInventory.class);

		onGuiEvent(this::onBind, BindEvent.class);
	}

	public PlayerInventory() {
		this("");
	}

	public void onBind(BindEvent event) {
		playerInventory = event.gui.getPlayerInventory();
	}

	public InventoryPlayer getInventory() {
		return playerInventory;
	}
}
