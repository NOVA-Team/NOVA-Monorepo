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

package nova.core.util.registry;

import java.util.Map;

/**
 * Manages translations from key labels to values.
 * @author Calclavia
 */
public abstract class LanguageManager extends Manager<LanguageManager> {

	/**
	 * Registers a custom key-value language pair
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

	public class Init extends ManagerEvent<LanguageManager> {
		public Init(LanguageManager manager) {
			super(manager);
		}
	}
}
