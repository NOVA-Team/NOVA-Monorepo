/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v18.wrapper;

import net.minecraft.util.BlockPos;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class VectorConverterTest {

	VectorConverter converter;

	@Before
	public void setUp() {
		converter = new VectorConverter();
	}

	@Test
	public void testToNova() {
		assertThat(converter.toNova(BlockPos.ORIGIN)).isEqualTo(Vector3D.ZERO);
	}

	@Test
	public void testToNative() {
		assertThat(converter.toNative(Vector3D.ZERO)).isEqualTo(BlockPos.ORIGIN);
	}

}
