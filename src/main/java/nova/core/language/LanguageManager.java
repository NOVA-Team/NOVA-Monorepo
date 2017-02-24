/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.language;

import nova.core.event.LanguageEvent;
import nova.internal.core.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nova.core.util.registry.Manager;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages translations from key labels to values.
 * @author Calclavia
 */
public abstract class LanguageManager extends Manager<LanguageManager> {

	protected final Map<String, Map<String, String>> languageMap = new HashMap<>();

	/**
	 * Gets the instance of LanguageManager
	 * @return The instance of LanguageManager
	 */
	public static LanguageManager instance() {
		return Game.language();
	}

	/**
	 * Registers a custom key-value language pair
	 * @param language The language ID
	 * @param key The unlocalized key
	 * @param value The localized value
	 */
	public void register(String language, String key, String value) {
		if (!languageMap.containsKey(language))
			languageMap.put(language, new HashMap<>());

		LanguageEvent.RegisterTranslation event = new LanguageEvent.RegisterTranslation(language, key, value);
		languageMap.get(language).put(key, event.value);
		Game.events().publish(event);
	}

	/**
	 * Gets the IETF language tag for the current language
	 * @return The current IETF language tag
	 */
	public abstract String getCurrentLanguage();

	/**
	 * Gets the localization of a key.
	 * @param key The unlocalized key
	 * @return The localized string
	 */
	public String translate(String key) {
		return languageMap.getOrDefault(getCurrentLanguage(), Collections.emptyMap()).getOrDefault(key, key);
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	/**
	 * Gets the localization of a key, but applying a set of replacement strings.
	 * @param key The unlocalized key
	 * @param replacements A 2D array of replacements, with keys in the translated string formatted as {@code ${<key>}} or {@code $<key>}
	 * For keys with non-alphanumeric (a-z, A-Z, 0-9 and underscores) characters, only the former is used.
	 * To prevent a '$' character from being matched, escape it with a '\, like so: '\$'.
	 * @return The localized string, modified with replacements
	 */
	public String translate(String key, String[]... replacements) {
		return translate(key, Stream.of(replacements).filter(ary -> ary.length == 2).collect(Collectors.toMap(ary -> ary[0], ary -> ary[1])));
	}

	/**
	 * Gets the localization of a key, but applying a set of replacement strings.
	 * @param key The unlocalized key
	 * @param replacements A map of replacements, with keys in the translated string formatted as {@code ${<key>}} or {@code $<key>}
	 * For keys with non-alphanumeric (a-z, A-Z, 0-9 and underscores) characters, only the former is used.
	 * To prevent a '$' character from being matched, escape it with a '\, like so: '\$'.
	 * @return The localized string, modified with replacements
	 */
	public String translate(String key, Map<String, String> replacements) {
		String str = translate(key);

		str = str.replaceAll("\\\\\\$", "${$}");

		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			if (replacement.getKey().matches("^\\w+$"))
				str = str.replaceAll("\\$" + replacement.getKey(), replacement.getValue());
			str = str.replaceAll("\\$\\{" + replacement.getKey() + "\\}", replacement.getValue());
		}

		str = str.replaceAll("\\$\\{\\$\\}", "$");

		return str;
	}

	public class Init extends ManagerEvent<LanguageManager> {
		public Init(LanguageManager manager) {
			super(manager);
		}
	}
}
