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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import nova.core.component.ComponentProvider;
import nova.core.component.SidedComponentMap;
import nova.core.util.Direction;

import java.util.Optional;

/**
 *
 * @author ExE Boss
 */
public class FWCapabilityProvider implements NovaCapabilityProvider {

	private final ComponentProvider<?> componentProvider;

	public FWCapabilityProvider(ComponentProvider<?> componentProvider) {
		this.componentProvider = componentProvider;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, Direction direction) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//			return
//				getComponent(FluidProvider.class, direction).isPresent() ||
//				getComponent(FluidConsumer.class, direction).isPresent() ||
//				getComponent(FluidHandler.class, direction).isPresent() ||
//				componentProvider instanceof SidedTankProvider;
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return false; // TODO: implement
		}

		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getCapability(Capability<T> capability, Direction direction) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//			return (Optional<T>) Optional.of(new FWFluidHandler(
//				getComponent(FluidProvider.class, direction),
//				getComponent(FluidConsumer.class, direction),
//				getComponent(FluidHandler.class, direction),
//				Optional.of(componentProvider)
//					.filter(p -> p instanceof SidedTankProvider)
//					.map(p -> (SidedTankProvider) p), direction)).filter(FWFluidHandler::isPresent);
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return Optional.empty(); // TODO: implement
		}

		return Optional.empty();
	}

	private <T> Optional<T> getComponent(Class<T> component, Direction direction) {
		if (componentProvider.components instanceof SidedComponentMap) {
			return ((SidedComponentMap) componentProvider.components).getOp(component, direction);
		} else {
			return componentProvider.components.getOp(component);
		}
	}
}
