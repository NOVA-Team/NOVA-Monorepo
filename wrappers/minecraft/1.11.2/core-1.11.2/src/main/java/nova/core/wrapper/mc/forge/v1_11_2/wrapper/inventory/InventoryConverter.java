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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.inventory;

import net.minecraft.inventory.IInventory;
import nova.core.component.inventory.Inventory;
import nova.core.nativewrapper.NativeConverter;
import nova.internal.core.Game;

/**
 * @author Calclavia
 */
public class InventoryConverter implements NativeConverter<Inventory, IInventory> {

	public static InventoryConverter instance() {
		return Game.natives().getNative(Inventory.class, IInventory.class);
	}

	@Override
	public Class<Inventory> getNovaSide() {
		return Inventory.class;
	}

	@Override
	public Class<IInventory> getNativeSide() {
		return IInventory.class;
	}

	@Override
	public Inventory toNova(IInventory nativeObj) {
		if (nativeObj instanceof FWInventory) {
			return ((FWInventory) nativeObj).wrapped;
		}

		return new BWInventory(nativeObj);
	}

	@Override
	public IInventory toNative(Inventory novaObj) {

		if (novaObj instanceof BWInventory) {
			return ((BWInventory) novaObj).wrapped;
		}
		return new FWInventory(novaObj);
	}

}
