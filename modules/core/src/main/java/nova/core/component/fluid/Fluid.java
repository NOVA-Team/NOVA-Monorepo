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

package nova.core.component.fluid;

import nova.core.block.BlockFactory;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.Identifiable;
import nova.internal.core.Game;

import java.util.Optional;

public class Fluid implements Identifiable, Storable, Cloneable {
	/**
	 * 1000 liters = 1 cubic meter
	 */
	public static final int bucketVolume = 1000;
	/**
	 * Fluid amount is measured in liters.
	 */
	@Store(key = "amount")
	private int amount = 1;

	//TODO: Public instance variable is not good practice
	public FluidFactory factory;

	/**
	 * @return Amount of fluid
	 */
	public int amount() {
		return amount;
	}

	/**
	 * Sets new size of this FluidStack
	 * Note that there can never be fluid with "zero" amount. Use Optional.empty() instead.
	 * @param amount New size
	 * @return {@code this} instance
	 */
	public Fluid setAmount(int amount) {
		this.amount = Math.max(amount, 1);
		return this;
	}

	/**
	 * Adds fluid to this FluidStack
	 * @param amount Amount of fluid to add
	 * @return Size added
	 */
	public int add(int amount) {
		int original = amount();
		setAmount(original + amount);
		return amount() - original;
	}

	/**
	 * Removes fluid to this FluidStack
	 * @param amount Amount of fluid to remove
	 * @return Fluid removed
	 */
	public int remove(int amount) {
		int original = amount();
		setAmount(original - amount);
		return original - amount();
	}

	@Override
	public Fluid clone() {
		Fluid cloned = factory.build().setAmount(amount());
		return cloned;
	}

	/**
	 * Returns new FluidStack of the same {@link Fluid} with specified fluid
	 * @param amount Amount of fluid in cloned FluidStack
	 * @return new FluidStack
	 */
	public Fluid withAmount(int amount) {
		Fluid cloned = clone();
		cloned.setAmount(amount);
		return cloned;
	}

	/**
	 * Gets the block associated with this fluid.
	 * @return The block. There may be no block associated with this fluid.
	 */
	public Optional<BlockFactory> getBlockFactory() {
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Fluid)) {
			return false;
		}
		Fluid i = (Fluid) o;
		return sameType(i) && i.amount == amount;
	}

	/**
	 * Check if this FluidStack is of type of another FluidStack
	 * @param stack The another Fluid
	 * @return Result
	 */
	public boolean sameType(Fluid stack) {
		return stack.getID().equals(getID());
	}

	@Override
	public final String getID() {
		return factory.getID();
	}

	@Override
	public int hashCode() {
		return 31 * amount + getID().hashCode();
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);
		data.put("id", factory.getID());
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);
		factory = Game.fluids().get(data.get("id")).get();
	}
}
