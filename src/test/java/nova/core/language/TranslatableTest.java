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

import java.util.Arrays;
import java.util.Optional;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class TranslatableTest {

	LanguageManager languageManager;
	TranslatableImpl translatable;

	@Before
	public void setUp() {
		languageManager = new FakeLanguageManager();
		translatable = new TranslatableImpl();
	}

	@Test
	public void testGetUnlocalizedName() {
		assertThat(translatable.getUnlocalizedName()).isEqualTo("${replacement with spaces} some text $key1, ${key1}; $other");
	}

	@Test
	public void testGetLocalizedName() {
		assertThat(translatable.getLocalizedName()).isEqualTo("24 some text 42, 42; String empty");
	}

	@Test
	public void testError() {
		assertThat(new TranslatableError().getLocalizedName()).isEqualTo("${I AM ERROR!}");
	}

	@Test
	public void testGetReplacements() {
		assertThat(translatable.getReplacements())
			.hasSize(3)
			.containsEntry("key1", "42")
			.containsEntry("replacement with spaces", "24")
			.containsEntry("other", "String empty");
		assertThat(translatable.other.getReplacements())
			.hasSize(2)
			.containsEntry("filledStr", "String")
			.containsEntry("emptyStr", "empty");
	}

	public class TranslatableImpl implements Translatable {

		@Translate
		public final String key1 = "42";

		@Translate("replacement with spaces")
		public final String key2 = "24";

		@Translate
		public final TranslatableOtherImpl other = new TranslatableOtherImpl();

		@Override
		public String getUnlocalizedName() {
			return "${replacement with spaces} some text $key1, ${key1}; $other";
		}

		@Override
		public String getLocalizedName() {
			return languageManager.translate(this.getUnlocalizedName(), this.getReplacements());
		}
	}

	public class TranslatableOtherImpl implements Translatable {

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

	public class TranslatableError implements Translatable {

		@Translate("I AM ERROR!")
		public final Object brokenToString = new Object() {
			@Override
			public String toString() {
				throw new RuntimeException("TranslatableError.brokenToString.toString() exception thrown") {
					private static final long serialVersionUID = 1L;

					@Override
					public synchronized Throwable fillInStackTrace() {
						Throwable self = super.fillInStackTrace();
						self.setStackTrace(Arrays.copyOf(self.getStackTrace(), 1));
						return self;
					}
				};
			}
		};

		@Override
		public String getUnlocalizedName() {
			return "${I AM ERROR!}";
		}

		@Override
		public String getLocalizedName() {
			return languageManager.translate(this.getUnlocalizedName(), this.getReplacements());
		}
	}
}
