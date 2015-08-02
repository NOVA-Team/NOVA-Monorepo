package nova.core.wrapper.mc18.manager.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * @author Calclavia
 */
public class NovaGuiConfig extends GuiConfig {
	public NovaGuiConfig(GuiScreen parentScreen, Configuration config, String modID) {
		super(parentScreen, new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), modID, false, false, GuiConfig.getAbridgedConfigPath(config.toString()));
	}
}
