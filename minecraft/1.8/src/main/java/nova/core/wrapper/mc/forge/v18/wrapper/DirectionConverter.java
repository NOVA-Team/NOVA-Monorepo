/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v18.wrapper;

import net.minecraft.util.EnumFacing;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.Direction;

/**
 *
 * @author ExE Boss
 */
public class DirectionConverter implements NativeConverter<Direction, EnumFacing> {

	@Override
	public Class<Direction> getNovaSide() {
		return Direction.class;
	}

	@Override
	public Class<EnumFacing> getNativeSide() {
		return EnumFacing.class;
	}

	@Override
	public Direction toNova(EnumFacing nativeObj) {
		switch (nativeObj) {
			case DOWN:  return Direction.DOWN;
			case UP:    return Direction.UP;
			case NORTH: return Direction.NORTH;
			case SOUTH: return Direction.SOUTH;
			case WEST:  return Direction.WEST;
			case EAST:  return Direction.EAST;
			default: return Direction.UNKNOWN;
		}
	}

	@Override
	public EnumFacing toNative(Direction novaObj) {
		switch (novaObj) {
			case DOWN:  return EnumFacing.DOWN;
			case UP:    return EnumFacing.UP;
			case NORTH: return EnumFacing.NORTH;
			case SOUTH: return EnumFacing.SOUTH;
			case WEST:  return EnumFacing.WEST;
			case EAST:  return EnumFacing.EAST;
			default: return (EnumFacing) null;
		}
	}
}
