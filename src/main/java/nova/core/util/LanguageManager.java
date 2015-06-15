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
	 * @return The localized string, modified withPriority replacements
	 */
	public String translate(String key, Map<String, String> replacements) {
		String str = translate(key);

		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			str = str.replaceAll(replacement.getKey(), replacement.getValue());
		}
		return str;
	}
}
