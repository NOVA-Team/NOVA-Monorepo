package nova.core.util;

import java.util.Map;

/**
 * Manages translations from key labels to values.
 * @author Calclavia
 */
public abstract class LanguageManager {
	public abstract String translate(String key);

	/**
	 * Gets the localization of a key, but applying a set of replacement strings.
	 * @return The localized string, modified with replacements
	 */
	public String translate(String key, Map<String, String> replacements) {
		String str = translate(key);
		replacements.forEach(str::replaceAll);
		return str;
	}
}
