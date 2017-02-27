/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.event;

import nova.core.event.bus.CancelableEvent;

/**
 * All events related to the language.
 * @author ExE Boss
 */
public abstract class LanguageEvent extends CancelableEvent {

	public final String language;

	public LanguageEvent(String language) {
		this.language = language;
	}

	/**
	 * Event is triggered when a translation is registered.
	 *
	 * @see nova.core.language.LanguageManager#register(String, String, String)
	 */
	public static class RegisterTranslation extends LanguageEvent {

		public final String key;
		public String value;

		public RegisterTranslation(String language, String key, String value) {
			super(language);
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * Event is triggered when a language is changed.
	 */
	public static class LanguageChanged extends CancelableEvent {

		public final String newLanguage;
		public final String oldLanguage;

		public LanguageChanged(String oldLanguage, String newLanguage) {
			this.oldLanguage = oldLanguage;
			this.newLanguage = newLanguage;
		}
	}
}
