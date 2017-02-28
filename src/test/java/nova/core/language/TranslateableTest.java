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
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class TranslateableTest {

	LanguageManager languageManager;
	TranslateableImpl translateable;

	@Before
	public void setUp() {
		languageManager = new FakeLanguageManager();
		translateable = new TranslateableImpl();
	}

	@Test
	public void testGetUnlocalizedName() {
		assertThat(translateable.getUnlocalizedName()).isEqualTo("${replacement with spaces} some text $key1, ${key1}; $other");
	}

	@Test
	public void testGetLocalizedName() {
		assertThat(translateable.getLocalizedName()).isEqualTo("24 some text 42, 42; String empty");
	}

	@Test
	public void testGetReplacements() {
		assertThat(translateable.getReplacements())
			.hasSize(3)
			.containsEntry("key1", "42")
			.containsEntry("replacement with spaces", "24")
			.containsEntry("other", "String empty");
		assertThat(translateable.other.getReplacements())
			.hasSize(2)
			.containsEntry("filledStr", "String")
			.containsEntry("emptyStr", "empty");
	}

	public class TranslateableImpl implements Translateable {

		@Translate
		public final String key1 = "42";

		@Translate("replacement with spaces")
		public final String key2 = "24";

		@Translate
		public final TranslateableOtherImpl other = new TranslateableOtherImpl();

		@Override
		public String getUnlocalizedName() {
			return "${replacement with spaces} some text $key1, ${key1}; $other";
		}

		@Override
		public String getLocalizedName() {
			return languageManager.translate(this.getUnlocalizedName(), this.getReplacements());
		}
	}

	public class TranslateableOtherImpl implements Translateable {

		@Translate
		public final Optional<String> filledStr = Optional.of("String");

		@Translate
		public final Optional<String> emptyStr = Optional.empty();

		@Override
		public String getUnlocalizedName() {
			return "$filledStr $emptyStr";
		}

		@Override
		public String getLocalizedName() {
			return languageManager.translate(this.getUnlocalizedName(), this.getReplacements());
		}
	}
}
