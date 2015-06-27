package nova.core.util;

import java.util.Map;

/**
 * Manages translations from key labels to values.
 * @author Calclavia
 */
public abstract class LanguageManager {

	/**
	 * Registers a custom key-value language pair
	 *
	 * @param language The language ID
	 * @param key The unlocalized key
	 * @param value The localized value
	 */
	public abstract void register(String language, String key, String value);

	/**
	 * @return Gets the current language string ID
	 */
	public abstract String getCurrentLanguage();

	public abstract String translate(String key);

	/**
	 * Gets the localization of a key, but applying a set of replacement strings.
	 * @return The localized string, modified with replacements
	 */
	public String translate(String key, Map<String, String> replacements) {
		String str = translate(key);

		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			str = str.replaceAll(replacement.getKey(), replacement.getValue());
		}
		return str;
	}
}
