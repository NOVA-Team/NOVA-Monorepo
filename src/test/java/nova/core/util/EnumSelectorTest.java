/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
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

	@Test(expected = IllegalStateException.class)
	public void testCannotLock() {
		EnumSelector.of(EnumExample.class).lock();
	}

	@Test(expected = IllegalStateException.class)
	public void testCannotRead() {
		EnumSelector.of(EnumExample.class).allowsAll();
	}

	@Test(expected = IllegalStateException.class)
	public void testCannotWrite() {
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
}
