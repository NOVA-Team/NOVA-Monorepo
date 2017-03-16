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
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import nova.core.util.Direction;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.DirectionConverter;

import java.util.function.Supplier;

/**
 * @author ExE Boss
 */
public class CapabilityUtil {

	private CapabilityUtil() {}

	/**
	 * Helper method to easily generate an {@link IStorage} using lambda expressions.
	 *
	 * @param <T> The Capability type
	 * @param <NBT> The NBT Tag Compound type
	 * @param writer The Writer
	 * @param reader The Reader
	 * @return A new {@link IStorage} instance
	 */
	public static <T, NBT extends NBTBase> IStorage<T> createStorage(StorageWriter<T, NBT> writer, StorageReader<T, NBT> reader) {
		return new Capability.IStorage<T>() {
			@Override
			public NBT writeNBT(Capability<T> capability, T instance, EnumFacing side) {
				return writer.writeNBT(capability, instance, DirectionConverter.instance().toNova(side));
			}

			@Override
			@SuppressWarnings("unchecked")
			public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
				reader.readNBT(capability, instance, DirectionConverter.instance().toNova(side), (NBT) nbt);
			}
		};
	}

	public static <T> IStorage<T> unsupportedStorage() {
		return unsupportedStorage((String) null);
	}

	public static <T> IStorage<T> unsupportedStorage(String reason) {
		return unsupportedStorage(() -> reason);
	}

	public static <T> IStorage<T> unsupportedStorage(Supplier<String> reason) {
		return createStorage((capability, instance, side) -> {throw new UnsupportedOperationException(reason.get());},
			(capability, instance, side, nbt) -> {throw new UnsupportedOperationException(reason.get());});
	}

	@FunctionalInterface
	public static interface StorageWriter<T, NBT extends NBTBase> {
		NBT writeNBT(Capability<T> capability, T instance, Direction side);
	}

	@FunctionalInterface
	public static interface StorageReader<T, NBT extends NBTBase> {
		void readNBT(Capability<T> capability, T instance, Direction side, NBT nbt);
	}
}
