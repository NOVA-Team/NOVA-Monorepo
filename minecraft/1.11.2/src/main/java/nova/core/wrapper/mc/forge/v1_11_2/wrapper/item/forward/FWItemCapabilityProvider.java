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
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward;

import net.minecraftforge.common.capabilities.Capability;
import nova.core.item.Item;
import nova.core.util.Direction;
import nova.core.util.EnumSelector;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward.FWCapabilityProvider;

import java.util.Optional;

/**
 * @author ExE Boss
 */
public class FWItemCapabilityProvider extends FWCapabilityProvider {

	private final NovaItem item;

	public FWItemCapabilityProvider(Item item) {
		this.item = new NovaItem(item);
	}

	@Override
	public boolean hasCapabilities() {
		return true;
	}

	@Override
	public <T> T addCapability(Capability<T> capability, T capabilityInstance, EnumSelector<Direction> directions) {
		if (capability == NovaItem.CAPABILITY)
			throw new IllegalArgumentException("Already has capability 'NovaItem'");
		return super.addCapability(capability, capabilityInstance, directions);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, Direction direction) {
		return capability == NovaItem.CAPABILITY || super.hasCapability(capability, direction);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == NovaItem.CAPABILITY)
			return Optional.of((T) item);
		return super.getCapability(capability, direction);
	}
}
