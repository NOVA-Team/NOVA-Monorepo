/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class EnumSelectorTest {

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
	public void test1Locked() {
		boolean result = enumSelectorExample1.locked();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test1Disallows_EXAMPLE_8() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_8);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test1Disallows_EXAMPLE_16() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test1Allows_EXAMPLE_24() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_24);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test1Disallows_EXAMPLE_32() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test1Allows_EXAMPLE_42() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_42);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test1Disallows_EXAMPLE_48() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_48);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test1Disallows_EXAMPLE_64() {
		boolean result = enumSelectorExample1.allows(EnumExample.EXAMPLE_64);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test2Locked() {
		boolean result = enumSelectorExample2.locked();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test2Allows_EXAMPLE_8() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_8);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test2Allows_EXAMPLE_16() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test2Disallows_EXAMPLE_24() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_24);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test2Allows_EXAMPLE_32() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test2Disallows_EXAMPLE_42() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_42);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test2Allows_EXAMPLE_48() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_48);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test2Allows_EXAMPLE_64() {
		boolean result = enumSelectorExample2.allows(EnumExample.EXAMPLE_64);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test3Allows_EXAMPLE_24() {
		boolean result = enumSelectorExample3.blocks(EnumExample.EXAMPLE_24);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test3Allows_EXAMPLE_42() {
		boolean result = enumSelectorExample3.blocks(EnumExample.EXAMPLE_42);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test4Disallows_EXAMPLE_24() {
		boolean result = enumSelectorExample4.blocks(EnumExample.EXAMPLE_24);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test4Disallows_EXAMPLE_42() {
		boolean result = enumSelectorExample4.blocks(EnumExample.EXAMPLE_42);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test1NotAllowsAll() {
		boolean result = enumSelectorExample1.allowsAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test1NotBlocksAll() {
		boolean result = enumSelectorExample1.blocksAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test2NotAllowsAll() {
		boolean result = enumSelectorExample2.allowsAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test2NotBlocksAll() {
		boolean result = enumSelectorExample2.blocksAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test3AllowsAll() {
		boolean result = enumSelectorExample3.allowsAll();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test3NotBlocksAll() {
		boolean result = enumSelectorExample3.blocksAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test4NotAllowsAll() {
		boolean result = enumSelectorExample4.allowsAll();
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void test4BlocksAll() {
		boolean result = enumSelectorExample4.blocksAll();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void test1StreamSize() {
		long result = enumSelectorExample1.stream().count();
		assertThat(result).isEqualTo(2);
	}

	@Test
	public void test2StreamSize() {
		long result = enumSelectorExample2.stream().count();
		assertThat(result).isEqualTo(EnumExample.values().length - 2);
	}

	@Test
	public void test3StreamSize() {
		long result = enumSelectorExample3.stream().count();
		assertThat(result).isEqualTo(EnumExample.values().length);
	}

	@Test
	public void test4StreamSize() {
		long result = enumSelectorExample4.stream().count();
		assertThat(result).isEqualTo(0);
	}

	@Test
	public void test1ParallelStreamSize() {
		long result = enumSelectorExample1.parallelStream().count();
		assertThat(result).isEqualTo(2);
	}

	@Test
	public void test2ParallelStreamSize() {
		long result = enumSelectorExample2.parallelStream().count();
		assertThat(result).isEqualTo(EnumExample.values().length - 2);
	}

	@Test
	public void test3ParallelStreamSize() {
		long result = enumSelectorExample3.parallelStream().count();
		assertThat(result).isEqualTo(EnumExample.values().length);
	}

	@Test
	public void test4ParallelStreamSize() {
		long result = enumSelectorExample4.parallelStream().count();
		assertThat(result).isEqualTo(0);
	}

	@Test
	public void test1SetSize() {
		int result = enumSelectorExample1.toSet().size();
		assertThat(result).isEqualTo(2);
	}

	@Test
	public void test2SetSize() {
		int result = enumSelectorExample2.toSet().size();
		assertThat(result).isEqualTo(EnumExample.values().length - 2);
	}

	@Test
	public void test3SetSize() {
		int result = enumSelectorExample3.toSet().size();
		assertThat(result).isEqualTo(EnumExample.values().length);
	}

	@Test
	public void test4SetSize() {
		int result = enumSelectorExample4.toSet().size();
		assertThat(result).isEqualTo(0);
	}

	@Test
	public void testCannotLock() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class);
		IllegalStateException result = null;
		try {
			enumSelectorExample.lock();
		} catch (IllegalStateException ex) {
			result = ex;
		}
		assertThat(result).isNotNull();
	}

	@Test
	public void testCannotRead() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class);
		IllegalStateException result = null;
		try {
			enumSelectorExample.allowsAll();
		} catch (IllegalStateException ex) {
			result = ex;
		}
		assertThat(result).isNotNull();
	}

	@Test
	public void testCannotWrite() {
		IllegalStateException result = null;
		try {
			enumSelectorExample1.apart(EnumExample.EXAMPLE_64);
		} catch (IllegalStateException ex) {
			result = ex;
		}
		assertThat(result).isNotNull();
	}

	@Test
	public void testCannotBlockAllowing() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).allowAll();
		IllegalStateException result = null;
		try {
			enumSelectorExample.blockAll();
		} catch (IllegalStateException ex) {
			result = ex;
		}
		assertThat(result).isNotNull();
	}

	@Test
	public void testCannotAllowBlocking() {
		EnumSelector<EnumExample> enumSelectorExample = EnumSelector.of(EnumExample.class).blockAll();
		IllegalStateException result = null;
		try {
			enumSelectorExample.allowAll();
		} catch (IllegalStateException ex) {
			result = ex;
		}
		assertThat(result).isNotNull();
	}

	public static enum EnumExample {
		EXAMPLE_8,
		EXAMPLE_16,
		EXAMPLE_24,
		EXAMPLE_32,
		EXAMPLE_48,
		EXAMPLE_64,

		EXAMPLE_42;
	}
}
