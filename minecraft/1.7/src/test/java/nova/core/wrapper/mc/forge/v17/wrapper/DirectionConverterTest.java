/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v17.wrapper;

import net.minecraft.util.EnumFacing;
import nova.core.util.Direction;
import nova.internal.core.Game;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class DirectionConverterTest {

	DirectionConverter converter;

	@Before
	public void setUp() {
		converter = new DirectionConverter();
	}

	@Test
	public void testToNova() {
		assertThat(converter.toNova(EnumFacing.DOWN)).isEqualTo(Direction.DOWN);
		assertThat(converter.toNova(EnumFacing.UP)).isEqualTo(Direction.UP);
		assertThat(converter.toNova(EnumFacing.NORTH)).isEqualTo(Direction.NORTH);
		assertThat(converter.toNova(EnumFacing.SOUTH)).isEqualTo(Direction.SOUTH);
		assertThat(converter.toNova(EnumFacing.WEST)).isEqualTo(Direction.WEST);
		assertThat(converter.toNova(EnumFacing.EAST)).isEqualTo(Direction.EAST);
	}

	@Test
	public void testToNative() {
		assertThat(converter.toNative(Direction.DOWN)).isEqualTo(EnumFacing.DOWN);
		assertThat(converter.toNative(Direction.UP)).isEqualTo(EnumFacing.UP);
		assertThat(converter.toNative(Direction.NORTH)).isEqualTo(EnumFacing.NORTH);
		assertThat(converter.toNative(Direction.SOUTH)).isEqualTo(EnumFacing.SOUTH);
		assertThat(converter.toNative(Direction.WEST)).isEqualTo(EnumFacing.WEST);
		assertThat(converter.toNative(Direction.EAST)).isEqualTo(EnumFacing.EAST);
		assertThat(converter.toNative(Direction.UNKNOWN)).isNull();
	}
}
