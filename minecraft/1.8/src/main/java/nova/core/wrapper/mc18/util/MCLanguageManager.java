package nova.core.wrapper.mc18.util;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import nova.core.util.LanguageManager;

/**
 * @author Calclavia
 */
public class MCLanguageManager extends LanguageManager {

	@Override
	public void register(String language, String key, String value) {
		LanguageRegistry.instance().addStringLocalization(key, language, value);
	}

	@Override
	public String getCurrentLanguage() {
		return FMLCommonHandler.instance().getCurrentLanguage();
	}

	@Override
	public String translate(String key) {
		return StatCollector.translateToLocal(key);
	}
}
