package nova.wrapper.mc1710.backward.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.gui.component.inventory.Slot;
import nova.core.gui.nativeimpl.NativeSlot;
import nova.core.gui.render.Graphics;
import nova.wrapper.mc1710.backward.gui.MCGui.MCContainer;
import nova.wrapper.mc1710.backward.gui.MCGui.MCGuiScreen;
import nova.wrapper.mc1710.forward.inventory.FWInventory;
import nova.wrapper.mc1710.wrapper.item.ItemConverter;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public class MCGuiSlot extends MCGuiComponent<Slot> implements NativeSlot, DrawableGuiComponent {

	private MCSlot slot;

	public MCGuiSlot(Slot component) {
		super(component);
	}

	protected static void drawSlot(MCGuiScreen gui, MCSlot slot, int mouseX, int mouseY, boolean drawBackground) {
		RenderHelper.enableGUIStandardItemLighting();

		if (drawBackground) {
			// Draw slot background
			Minecraft.getMinecraft().renderEngine.bindTexture(GuiUtils.RESOURCE_GUI_CONTROLS);
			GL11.glColor3f(1, 1, 1);
			Gui.func_146110_a(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 0, 8, 18, 18, 32, 32);
		}

		// Translate item renderer back to the origin
		GL11.glTranslatef(0, 0, -150);
		gui.func_146977_a(slot);
		GL11.glTranslatef(0, 0, 150);

		if (mouseX >= slot.xDisplayPosition
			&& mouseY >= slot.yDisplayPosition
			&& mouseX < slot.xDisplayPosition + 18
			&& mouseY < slot.yDisplayPosition + 18
			&& slot.func_111238_b()) {

			gui.theSlot = slot;
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glColorMask(true, true, true, false);
			gui.drawGradientRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, 0x80FFFFFF, 0x80FFFFFF);
			GL11.glColorMask(true, true, true, true);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		RenderHelper.disableStandardItemLighting();
	}

	@Override
	public Optional<Vector2D> getPreferredSize() {
		return Optional.of(new Vector2D(18, 18));
	}

	@Override
	public void draw(int mouseX, int mouseY, float partial, Graphics graphics) {
		MCGuiScreen gui = getGui().getGuiScreen();
		MCCanvas canvas = getCanvas();
		slot.reset();
		drawSlot(gui, slot, mouseX, mouseY, !getComponent().getBackground().isPresent());
		slot.setPosition(canvas.getState().txi(), canvas.getState().tyi(), gui);
		super.draw(mouseX, mouseY, partial, graphics);
	}

	@Override
	public void onAddedToContainer(MCContainer container) {
		IInventory inventory = new FWInventory(getComponent().getInventory());
		slot = new MCSlot(inventory, getComponent().getSlotID(), 0, 0, getComponent());
		container.addSlotToContainer(slot);
	}

	public static class MCSlot extends net.minecraft.inventory.Slot {

		private final int xCoord;
		private final int yCoord;
		private final Slot slot;

		public MCSlot(IInventory inventory, int id, int xCoord, int yCoord) {
			this(inventory, id, xCoord, yCoord, null);
		}

		public MCSlot(IInventory inventory, int id, int xCoord, int yCoord, Slot slot) {
			super(inventory, id, 0, 0);
			this.xCoord = xCoord;
			this.yCoord = yCoord;
			this.slot = slot;
		}

		public void setPosition(int x, int y, MCGuiScreen gui) {
			this.xDisplayPosition = xCoord + x - gui.guiLeft();
			this.yDisplayPosition = yCoord + y - gui.guiTop();
		}

		public void reset() {
			this.xDisplayPosition = xCoord;
			this.yDisplayPosition = yCoord;
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return slot == null || !slot.isReadOnly() && slot.accept(ItemConverter.instance().getNovaItem(stack));
		}

		@Override
		public ItemStack decrStackSize(int size) {
			if (slot == null || !slot.isReadOnly()) {
				return super.decrStackSize(size);
			}
			return getStack();
		}
	}
}
