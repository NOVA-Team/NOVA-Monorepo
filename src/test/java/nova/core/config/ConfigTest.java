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

package nova.core.config;

import nova.core.util.exception.NovaException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static nova.core.config.Configuration.load;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConfigTest {

	@ConfigHolder(true)
	static class TestConfigHolder {

		@ConfigHolder
		static class InnerObject {

			@Config(comment = "hm-pf-pf, little kid")
			public int anti344_age = 15;

			@Config
			public boolean otherInner = false;
		}

		@Config("object")
		public InnerObject inner = new InnerObject();

		@Config(value = "object.other", comment = "yay for lol!!")
		public String yolo = "lol";

		@Config(value = "object", comment = "Pretty similar to PI")
		public double higher = 3.1415;

		@Config("object")
		public List<Integer> array = Arrays.asList(3, 2, 1);

		@Config(comment = "test, not root")
		public String root_value = "default";
	}

	@ConfigHolder(false) //explicitly false
	@SuppressWarnings("unused")
	static class FailedConfig {

		@ConfigHolder
		static class Obj {

			@Config
			public int value = 2;
		}

		@Config
		public String tehBestString = "Bite-1987";

		@Config
		public Obj object = new Obj();
	}

	@ConfigHolder(useAll = true)
	static class UseAllTest {

		public int test = 16;
		public String value = "default";
		public double d = 1.71;
	}

	@ConfigHolder
	@SuppressWarnings("unused")
	static class WrongTypeTest {

		@Config
		public int badInt = 7;

		@Config
		public String goodString = "7";
	}

	@Test
	public void testBasicConfig() throws Exception {
		TestConfigHolder holder = new TestConfigHolder();
		String config =
			"object{\n"
				+ "  //thats inner group of test config\n"
				+ "  inner = {\n"
				+ "    anti344_age : 173\n"
				+ "    //another test comment\n"
				+ "    otherInner = true\n"
				+ "  }\n"
				+ "  other: {\n"
				+ "    yolo = destruction catalyst //idk why i put this there\n"
				+ "  }\n"
				+ "\n"
				+ "  higher = 3.1415\n"
				+ "\n"
				+ "  array = [1, 2, 3, 5, 4]\n"
				+ "}\n"
				+ "\n"
				+ "//thats root, yo\n"
				+ "root_value = WHAAAAA";
		assertThat(load(config, holder)).isEqualTo(config);
		assertThat(holder.inner.anti344_age).isEqualTo(173);
		assertThat(holder.inner.otherInner).isEqualTo(true);
		assertThat(holder.yolo).isEqualTo("destruction catalyst");
		assertThat(holder.array).hasSize(5);
		assertThat(holder.higher).isEqualTo(3.1415);
		assertThat(holder.root_value).isEqualTo("WHAAAAA");

	}

	@Test
	public void testConfigGeneration() throws Exception {
		TestConfigHolder holder = new TestConfigHolder();
		assertThat(load("", holder)).isEqualTo(
			"object {\n"
				+ "    array=[\n"
				+ "        3,\n"
				+ "        2,\n"
				+ "        1\n"
				+ "    ]\n"
				+ "    # Pretty similar to PI\n"
				+ "    higher=3.1415\n"
				+ "    inner {\n"
				+ "        # hm-pf-pf, little kid\n"
				+ "        \"anti344_age\"=15\n"
				+ "        otherInner=false\n"
				+ "    }\n"
				+ "    other {\n"
				+ "        # yay for lol!!\n"
				+ "        yolo=lol\n"
				+ "    }\n"
				+ "}\n"
				+ "# test, not root\n"
				+ "\"root_value\"=default\n"
		);
	}

	@Test
	public void testAddingDefaults() throws Exception {
		TestConfigHolder holder = new TestConfigHolder();
		assertThat(load(
			"object{\n"
				+ "  //thats inner group of test config\n"
				+ "  inner.otherInner = true\n"
				+ "  other: {\n"
				+ "    //idk why i put this there\n"
				+ "    yolo = destruction catalyst \n"
				+ "  }\n"
				+ "\n"
				+ "  higher = 3.1415\n"
				+ "}\n"
				+ "\n"
				+ "//thats root, yo\n"
				+ "root_value = WHAAAAA"
			, holder)).isEqualTo(
			"object {\n"
				+ "    array=[\n"
				+ "        3,\n"
				+ "        2,\n"
				+ "        1\n"
				+ "    ]\n"
				+ "    higher=3.1415\n"
				+ "    inner {\n"
				+ "        # hm-pf-pf, little kid\n"
				+ "        \"anti344_age\"=15\n"
				+ "        # thats inner group of test config\n"
				+ "        otherInner=true\n"
				+ "    }\n"
				+ "    other {\n"
				+ "        # idk why i put this there\n"
				+ "        yolo=\"destruction catalyst\"\n"
				+ "    }\n"
				+ "}\n"
				+ "# thats root, yo\n"
				+ "\"root_value\"=WHAAAAA\n");
	}

	@Test
	public void testUseAll() throws Exception {
		UseAllTest holder = new UseAllTest();
		load("test = 21\nvalue = Calclavia\nd = 1277541.5", holder);
		assertThat(holder.test).isEqualTo(21);
		assertThat(holder.value).isEqualTo("Calclavia");
		assertThat(holder.d).isEqualTo(1277541.5);
	}

	@Test
	public void testFailedConfig() throws Exception {
		assertThatThrownBy(() -> load("tehBestString = RX14\nobject.value = 14", new FailedConfig()))
			.isInstanceOf(NovaException.class)
			.hasMessage("Scanning inner-objects is disabled for `object`");
	}

	@Test
	public void testWrongType() throws Exception {
		assertThatThrownBy(() -> load("badInt = badass\ngoodString=captain", new WrongTypeTest()))
			.isInstanceOf(NovaException.class)
			.hasMessage("Field `badInt` is of the wrong type!");
	}
}
