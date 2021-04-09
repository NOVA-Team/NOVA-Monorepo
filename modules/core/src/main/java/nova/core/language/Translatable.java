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

package nova.core.language;

import nova.core.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implemented by objects that can be translated.
 *
 * @author ExE Boss
 */
public interface Translatable {

	/**
	 * Gets the unlocalized name of this object.
	 * @return The unlocalized name
	 */
	String getUnlocalizedName();

	/**
	 * Gets the localized name of this object.
	 * @return The localized name
	 */
	default String getLocalizedName() {
		return LanguageManager.instance().translate(this.getUnlocalizedName(), this.getReplacements());
	}

	/**
	 * Gets the replacement map for {@link LanguageManager#translate(java.lang.String, java.util.Map)
	 * LanguageManager.translate(String, Map&lt;String, String&gt;)}.
	 * @return The replacement map
	 */
	default Map<String, String> getReplacements() {
		Map<String, String> replacements = new HashMap<>();
		ReflectionUtil.forEachRecursiveAnnotatedField(Translate.class, getClass(), (field, annotation) -> {
			try {
				field.setAccessible(true);
				String key = annotation.value();
				if (key.isEmpty()) {
					key = field.getName();
				}
				Object value = field.get(this);
				if (value instanceof Optional)
					value = ((Optional<?>) value).map(o -> (Object) o).orElse("empty");
				if (value instanceof Translatable)
					replacements.put(key, ((Translatable) value).getLocalizedName());
				else
					replacements.put(key, Objects.toString(value));
				field.setAccessible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return replacements;
	}
}
