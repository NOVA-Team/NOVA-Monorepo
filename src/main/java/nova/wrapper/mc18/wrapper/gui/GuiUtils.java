package nova.wrapper.mc18.wrapper.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import nova.wrapper.mc18.launcher.NovaMinecraft;
import org.lwjgl.opengl.GL11;

public class GuiUtils {

	public static final ResourceLocation RESOURCE_GUI_CONTROLS = new ResourceLocation(NovaMinecraft.id, "textures/gui/controls.png");

	public static void drawGUIWindow(int xOffset, int yOffset, int width, int height) {
		GL11.glTranslatef(xOffset, yOffset, 0);

		Gui.drawRect(3, 3, width - 3, height - 3, 0xFFC6C6C6);
		Gui.drawRect(4, 0, width - 4, 1, 0xFF000000);
		Gui.drawRect(4, 1, width - 4, 3, 0xFFFFFFFF);
		Gui.drawRect(4, height - 1, width - 4, height, 0xFF000000);
		Gui.drawRect(4, height - 3, width - 4, height - 1, 0xFF555555);
		Gui.drawRect(0, 4, 1, height - 4, 0xFF000000);
		Gui.drawRect(1, 4, 3, height - 4, 0xFFFFFFFF);
		Gui.drawRect(width - 1, 4, width, height - 4, 0xFF000000);
		Gui.drawRect(width - 3, 4, width - 1, height - 4, 0xFF555555);

		Minecraft.getMinecraft().renderEngine.bindTexture(RESOURCE_GUI_CONTROLS);
		GL11.glColor4f(1, 1, 1, 1);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 4, 4, 32, 32);
		Gui.drawModalRectWithCustomSizedTexture(width - 4, 0, 4, 0, 4, 4, 32, 32);
		Gui.drawModalRectWithCustomSizedTexture(width - 4, height - 4, 4, 4, 4, 4, 32, 32);
		Gui.drawModalRectWithCustomSizedTexture(0, height - 4, 0, 4, 4, 4, 32, 32);
		GL11.glTranslatef(-xOffset, -yOffset, 0);
	}
}
