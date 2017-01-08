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

package nova.core.retention;

import org.junit.Test;

import java.util.function.Consumer;

import static nova.testutils.NovaAssertions.assertThat;


public class StorableTest {
    @Test
    public void testSingleStorable() {
        class TestCase implements Storable {
            @Store
            private int value = 0;
        }
        TestCase test = new TestCase();
        test.value = 256;

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.value).isEqualTo(256);
    }

    @Test
    public void testKeyStorable() {
        class TestCase implements Storable {
            @Store(key = "int")
            private int integer = 0;

            @Store(key = "str")
            private String string = "";
        }
        TestCase test = new TestCase();
        test.integer = 256;
        test.string = "test";

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.integer).isEqualTo(256);
        assertThat(test.string).isEqualTo("test");
    }

    public static class SubTestCase implements Storable {
        @Store
        private int value = 0;

        SubTestCase() {}
    }

    @Test
    public void testRecursiveStorable() {

        class TestCase implements Storable {
            @Store
            private SubTestCase sub;
        }

        TestCase test = new TestCase();
        test.sub = new SubTestCase();
        test.sub.value = 256;

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.sub.value).isEqualTo(256);
    }

    @Test
    public void testMultipleStorable() {
        class TestCase implements Storable {
            @Store
            private int integer = 0;

            @Store
            private String string = "";
        }
        TestCase test = new TestCase();
        test.integer = 256;
        test.string = "test";

        Data data = new Data();
        test.save(data);

        test = new TestCase();
        test.load(data);

        assertThat(test.integer).isEqualTo(256);
        assertThat(test.string).isEqualTo("test");
    }
	
	/**
	 * Prints the contents of Data to the console.
	 * 
	 * @param data The data to print.
	 * @param print The method to use. Should insert a newline after every call. (eg. {@link java.io.PrintStream#println()})
	 */
	public static void printData(Data data, Consumer<Object> print) {
		System.out.println("Data:");
		printData(data, 0, print);
	}
	
	private static void printData(Data data, int index, Consumer<Object> print) {
		data.forEach((str, obj) -> {
			if (obj instanceof Data) {
				print.accept(prepend("- " + str + ":", index));
				printData((Data)obj, index + 1, print);
			} else
				print.accept(prepend("- " + str + ": " + obj, index));
		});
	}
	
	private static String prepend(String str, int index) {
		StringBuilder sb = new StringBuilder(str.length() + index * 2);
		for (int i = 0; i < index; i++)
			sb.append("  ");
		sb.append(str);
		return sb.toString();
	}
}
