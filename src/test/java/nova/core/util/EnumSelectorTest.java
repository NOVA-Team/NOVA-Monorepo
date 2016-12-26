/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class EnumSelectorTest {

	EnumSelector <EnumExample> enumSelectorExample;

	public EnumSelectorTest() {
	}

    @Before
    public void setUp() {
		enumSelectorExample = EnumSelector.of(EnumExample.class).blockAll()
				.apart(EnumExample.EXAMPLE_24).apart(EnumExample.EXAMPLE_42).lock();
    }

	@Test
	public void testLocked() {
		boolean result = enumSelectorExample.locked();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void testDisallows_EXAMPLE_8() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_8);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void testDisallows_EXAMPLE_16() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void testAllows_EXAMPLE_24() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_24);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void testDisallows_EXAMPLE_32() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_16);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void testAllows_EXAMPLE_42() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_42);
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void testDisallows_EXAMPLE_48() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_48);
		assertThat(result).isEqualTo(false);
	}

	@Test
	public void testDisallows_EXAMPLE_64() {
		boolean result = enumSelectorExample.allows(EnumExample.EXAMPLE_64);
		assertThat(result).isEqualTo(false);
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
