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

import nova.wrappertests.depmodules.FakeLanguageModule.FakeLanguageManager;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class LanguageManagerTest {

	private LanguageManager langauageManager = new FakeLanguageManager();

	public LanguageManagerTest() {
	}

	@Test
	public void testGetCurrentLanguage() {
		assertThat(langauageManager.getCurrentLanguage()).isEqualTo("en-US");
	}

	@Test
	public void testTranslate() {
		assertThat(langauageManager.translate("key.untranslated")).isEqualTo("key.untranslated");
		assertThat(langauageManager.translate("${replacement with spaces} some text $key1, ${key1}", new String[][]{
			{"replacement with spaces","24"},
			{"key1","42"}
		})).isEqualTo("24 some text 42, 42");
	}
}
