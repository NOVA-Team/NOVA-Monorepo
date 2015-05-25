package nova.wrapper.mc1710.util;

import net.minecraft.util.StatCollector;
import nova.core.util.LanguageManager;

/**
 * @author Calclavia
 */
public class MCLanguageManager extends LanguageManager {

	@Override
	public String translate(String key) {
		return StatCollector.translateToLocal(key);
	}
}
