package nova.wrapper.mc1710.backward.gui;

import net.minecraft.inventory.IInventory;
import nova.core.gui.component.inventory.PlayerInventory;
import nova.core.gui.nativeimpl.NativePlayerInventory;
import nova.core.gui.render.Graphics;
import nova.core.util.transform.vector.Vector2i;
import nova.wrapper.mc1710.backward.gui.MCGui.MCContainer;
import nova.wrapper.mc1710.backward.gui.MCGui.MCGuiScreen;
import nova.wrapper.mc1710.backward.gui.MCGuiSlot.MCSlot;
import nova.wrapper.mc1710.backward.inventory.BWInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MCGuiPlayerInventory extends MCGuiComponent<PlayerInventory> implements NativePlayerInventory {

	List<MCSlot> slots = new ArrayList<>();

	public MCGuiPlayerInventory(PlayerInventory component) {
		super(component);
	}

	@Override
	public Optional<Vector2i> getPreferredSize() {
		return Optional.of(new Vector2i(162, 80));
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		MCCanvas canvas = getCanvas();
		int x = canvas.getState().txi();
		int y = canvas.getState().tyi();

		MCGuiScreen gui = getGui().getGuiScreen();
		for (MCSlot slot : slots) {
			slot.reset();
			MCGuiSlot.drawSlot(gui, slot, mouseX, mouseY, !getComponent().getBackground().isPresent());
			slot.setPosition(x, y, gui);
		}
		super.draw(mouseX, mouseY, partial, graphics);
	}

	@Override
	public void onAddedToContainer(MCContainer container) {
		IInventory inventory = ((BWInventory) getComponent().getInventory()).mcInventory;
		slots.clear();

		for (int j = 0; j < 3; j++) {
			for (int k = 0; k < 9; k++) {
				slots.add(new MCSlot(inventory, k + j * 9 + 9, k * 18, j * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			slots.add(new MCSlot(inventory, i, i * 18, 62));
		}

		slots.forEach(container::addSlotToContainer);
	}
}
