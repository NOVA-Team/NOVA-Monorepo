package nova.core.wrapper.mc17.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StatCollector;
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
