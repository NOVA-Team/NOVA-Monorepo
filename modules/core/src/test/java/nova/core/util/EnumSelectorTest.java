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

package nova.core.util;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static nova.testutils.NovaAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ExE Boss
 */
public class EnumSelectorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	EnumSelector <EnumExample> enumSelectorExample1;
	EnumSelector <EnumExample> enumSelectorExample2;
	EnumSelector <EnumExample> enumSelectorExample3;
	EnumSelector <EnumExample> enumSelectorExample4;

	public EnumSelectorTest() {
	}

    @Before
    public void setUp() {
		enumSelectorExample1 = EnumSelector.of(EnumExample.class).blockAll()
				.apart(EnumExample.EXAMPLE_24).apart(EnumExample.EXAMPLE_42).lock();

		enumSelectorExample2 = EnumSelector.of(EnumExample.class).allowAll()
				.apart(EnumExample.EXAMPLE_24, EnumExample.EXAMPLE_42).lock();

		enumSelectorExample3 = EnumSelector.of(EnumExample.class).allowAll().lock();
		enumSelectorExample4 = EnumSelector.of(EnumExample.class).blockAll().lock();
    }

	@Test
	public void testAdditionalMethods() {
		EnumSelector<EnumExample> ex1a = EnumSelector.of(EnumExample.class).blockAll().apart(enumSelectorExample1).lock();
		EnumSelector<EnumExample> ex1b = EnumSelector.of(EnumExample.class).allowAll().apart(enumSelectorExample1).lock();
		EnumSelector<EnumExample> ex1c = EnumSelector.of(EnumExample.class).blockAll().apart(enumSelectorExample1.toSet()).lock();
		EnumSelector<EnumExample> ex1d = EnumSelector.of(EnumExample.class).allowAll().apart(enumSelectorExample1.toSet()).lock();

		EnumSelector<EnumExample> ex2a = EnumSelector.of(EnumExample.class).blockAll().apart(enumSelectorExample2).lock();
		EnumSelector<EnumExample> ex2b = EnumSelector.of(EnumExample.class).allowAll().apart(enumSelectorExample2).lock();
		EnumSelector<EnumExample> ex2c = EnumSelector.of(EnumExample.class).blockAll().apart(enumSelectorExample2::iterator).lock();
		EnumSelector<EnumExample> ex2d = EnumSelector.of(EnumExample.class).allowAll().apart(enumSelectorExample2::iterator).lock();

		EnumSelector<EnumExample> ex3a = EnumSelector.of(EnumExample.class).blockAll().apart(EnumExample.values()).lock();
		EnumSelector<EnumExample> ex3b = EnumSelector.of(EnumExample.class).allowAll().apart(EnumExample.values()).lock();

		assertThat(ex1a.allows(EnumExample.EXAMPLE_42)).isTrue();
		assertThat(ex1b.allows(EnumExample.EXAMPLE_42)).isFalse();
		assertThat(ex1c.allows(EnumExample.EXAMPLE_42)).isTrue();
		assertThat(ex1d.allows(EnumExample.EXAMPLE_42)).isFalse();

		assertThat(ex2a.allows(EnumExample.EXAMPLE_42)).isFalse();
		assertThat(ex2b.allows(EnumExample.EXAMPLE_42)).isTrue();
		assertThat(ex2c.allows(EnumExample.EXAMPLE_42)).isFalse();
		assertThat(ex2d.allows(EnumExample.EXAMPLE_42)).isTrue();

		assertThat(ex3a.allowsAll()).isTrue();
		assertThat(ex3b.blocksAll()).isTrue();
	}

	@Test
	public void testAllLocked() {
		assertThat(enumSelectorExample1.locked()).isEqualTo(true);
		assertThat(enumSelectorExample2.locked()).isEqualTo(true);
		assertThat(enumSelectorExample3.locked()).isEqualTo(true);
		assertThat(enumSelectorExample4.locked()).isEqualTo(true);
	}

	@Test
	public void test1Allows() {
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_8)).isEqualTo(false);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_16)).isEqualTo(false);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_24)).isEqualTo(true);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_32)).isEqualTo(false);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_42)).isEqualTo(true);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_48)).isEqualTo(false);
		assertThat(enumSelectorExample1.allows(EnumExample.EXAMPLE_64)).isEqualTo(false);
	}

	@Test
	public void test2Allows() {
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_8)).isEqualTo(true);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_16)).isEqualTo(true);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_24)).isEqualTo(false);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_32)).isEqualTo(true);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_42)).isEqualTo(false);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_48)).isEqualTo(true);
		assertThat(enumSelectorExample2.allows(EnumExample.EXAMPLE_64)).isEqualTo(true);
	}

	@Test
	public void test3Allows() {
		assertThat(enumSelectorExample3.blocks(EnumExample.EXAMPLE_24)).isEqualTo(false);
		assertThat(enumSelectorExample3.blocks(EnumExample.EXAMPLE_42)).isEqualTo(false);
	}

	@Test
	public void test4Allows() {
		assertThat(enumSelectorExample4.blocks(EnumExample.EXAMPLE_24)).isEqualTo(true);
		assertThat(enumSelectorExample4.blocks(EnumExample.EXAMPLE_42)).isEqualTo(true);
	}

	@Test
	public void testAllowsAll() {
		assertThat(enumSelectorExample1.allowsAll()).isEqualTo(false);
		assertThat(enumSelectorExample2.allowsAll()).isEqualTo(false);
		assertThat(enumSelectorExample3.allowsAll()).isEqualTo(true);
		assertThat(enumSelectorExample4.allowsAll()).isEqualTo(false);
	}

	@Test
	public void testBlocksAll() {
		assertThat(enumSelectorExample1.blocksAll()).isEqualTo(false);
		assertThat(enumSelectorExample2.blocksAll()).isEqualTo(false);
		assertThat(enumSelectorExample3.blocksAll()).isEqualTo(false);
		assertThat(enumSelectorExample4.blocksAll()).isEqualTo(true);
	}

	@Test
	public void testStreamSize() {
		assertThat(enumSelectorExample1.stream().count()).isEqualTo(2);
		assertThat(enumSelectorExample2.stream().count()).isEqualTo(EnumExample.values().length - 2);
		assertThat(enumSelectorExample3.stream().count()).isEqualTo(EnumExample.values().length);
		assertThat(enumSelectorExample4.stream().count()).isEqualTo(0);
	}

	@Test
	public void testParallelStreamSize() {
		assertThat(enumSelectorExample1.parallelStream().count()).isEqualTo(2);
		assertThat(enumSelectorExample2.parallelStream().count()).isEqualTo(EnumExample.values().length - 2);
		assertThat(enumSelectorExample3.parallelStream().count()).isEqualTo(EnumExample.values().length);
		assertThat(enumSelectorExample4.parallelStream().count()).isEqualTo(0);
	}

	@Test
	public void testSetSize() {
		assertThat(enumSelectorExample1.toSet().size()).isEqualTo(2);
		assertThat(enumSelectorExample2.toSet().size()).isEqualTo(EnumExample.values().length - 2);
		assertThat(enumSelectorExample3.toSet().size()).isEqualTo(EnumExample.values().length);
		assertThat(enumSelectorExample4.toSet().size()).isEqualTo(0);
	}

	@Test
	public void testSize() {
		assertThat(enumSelectorExample1.size()).isEqualTo(2);
		assertThat(enumSelectorExample2.size()).isEqualTo(EnumExample.values().length - 2);
		assertThat(enumSelectorExample3.size()).isEqualTo(EnumExample.values().length);
		assertThat(enumSelectorExample4.size()).isEqualTo(0);
	}

	@Test
 	public void testCannotLock() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Cannot lock EnumSelector without specifying default behaviour.");
		EnumSelector.of(EnumExample.class).lock();
	}

	@Test
 	public void testCannotRead() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Cannot use EnumSelector that is not locked.");
		EnumSelector.of(EnumExample.class).allowsAll();
	}

	@Test
 	public void testCannotWrite() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("No edits are allowed after EnumSelector has been locked.");
		enumSelectorExample1.apart(EnumExample.EXAMPLE_64);
	}

	@Test
	public void testCannotBlockAllowing() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).allowAll();
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("You can't block all enum values when you are already allowing them.");
		enumSelectorExample.blockAll();
	}

	@Test
	public void testCannotAllowBlocking() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).blockAll();
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("You can't allow all enum values when you are already blocking them.");
		enumSelectorExample.allowAll();
	}

	@Test
	public void testApartIteratorException() {
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Cannot call EnumSelector.apart(EnumSelector) without specifying default behaviour.");
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).apart(enumSelectorExample1);
	}

	@Test
	public void testApartEnumExampleException() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectCause(CoreMatchers.instanceOf(IllegalStateException.class));
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).allowAll().apart(EnumSelector.of(EnumExample.class));
	}

	@Test
	public void testToString() {
		assertThat(enumSelectorExample1.toString()).isEqualTo('[' + String.join(", ", EnumExample.EXAMPLE_24.toString(), EnumExample.EXAMPLE_42.toString()) + ']');
		assertThat(enumSelectorExample4.toString()).isEqualTo("[]");
	}

	@Test
	public void testHashCode() {
		assertThat(enumSelectorExample3.hashCode()).isEqualTo(EnumSelector.of(EnumExample.class).allowAll().lock().hashCode());
		assertThat(enumSelectorExample4.hashCode()).isEqualTo(EnumSelector.of(EnumExample.class).blockAll().lock().hashCode());
	}

	@Test
	public void testEquals() {
		assertThat(enumSelectorExample1).isEqualTo(enumSelectorExample1);
		assertThat(enumSelectorExample2).isNotEqualTo(null);
		assertThat(enumSelectorExample2).isNotEqualTo(this);
		assertThat(enumSelectorExample2).isNotEqualTo(enumSelectorExample3);
		assertThat(enumSelectorExample4).isEqualTo(EnumSelector.of(EnumExample.class).blockAll().lock());
	}
}
