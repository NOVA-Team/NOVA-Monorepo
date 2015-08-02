package nova.core.wrapper.mc17.manager.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * @author Calclavia
 */
public class NovaGuiConfig extends GuiConfig {
	public NovaGuiConfig(GuiScreen parentScreen, Configuration config, String modID) {
		super(parentScreen, new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), modID, false, false, GuiConfig.getAbridgedConfigPath(config.toString()));
	}
}
