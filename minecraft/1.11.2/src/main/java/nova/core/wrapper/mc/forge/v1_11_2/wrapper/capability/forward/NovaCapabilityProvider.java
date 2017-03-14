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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA. If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import nova.core.util.Direction;
import nova.core.util.EnumSelector;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.DirectionConverter;

import javax.annotation.Nullable;

/**
 * @author ExE Boss
 */
public interface NovaCapabilityProvider extends ICapabilityProvider {

	boolean hasCapabilities();

	default <T> T addCapability(Capability<T> capability, T instance) {
		return addCapability(capability, instance, Direction.UNKNOWN);
	}

	default <T> T addCapability(Capability<T> capability, T instance, Direction... directions) {
		return addCapability(capability, instance, EnumSelector.of(Direction.class).blockAll().apart(directions).lock());
	}


	default <T> T addCapability(Capability<T> capability, T instance, Direction direction) {
		return addCapability(capability, instance, direction == Direction.UNKNOWN ?
			EnumSelector.of(Direction.class).allowAll().lock() :
			EnumSelector.of(Direction.class).blockAll().apart(direction).lock());
	}


	<T> T addCapability(Capability<T> capability, T instance, EnumSelector<Direction> directions);

	boolean hasCapability(Capability<?> capability, Direction direction);

	@Override
	default boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return hasCapability(capability, DirectionConverter.instance().toNova(facing));
	}

	@Nullable
	<T> T getCapability(Capability<T> capability, Direction direction);

	@Override
	@Nullable
	default <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getCapability(capability, DirectionConverter.instance().toNova(facing));
	}
}
