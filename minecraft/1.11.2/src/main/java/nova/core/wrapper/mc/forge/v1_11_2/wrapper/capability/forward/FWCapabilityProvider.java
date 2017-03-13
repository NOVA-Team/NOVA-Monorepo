/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward;

import net.minecraftforge.common.capabilities.Capability;
import nova.core.util.Direction;
import nova.core.util.EnumSelector;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ExE Boss
 */
public class FWCapabilityProvider implements NovaCapabilityProvider {

	private final EnumMap<Direction, Map<Capability<?>, Object>> capabilities = new EnumMap<>(Direction.class);

	public FWCapabilityProvider() {
		for (Direction facing : Direction.values())
			capabilities.put(facing, new ConcurrentHashMap<>());
	}

	@Override
	public boolean hasCapabilities() {
		return capabilities.values().parallelStream().map(map -> map.keySet().parallelStream()).count() > 0;
	}

	@Override
	public <T> T addCapability(Capability<T> capability, T capabilityInstance, EnumSelector<Direction> facing) {
		if (facing.allowsAll()) {
			if (capabilities.get(Direction.UNKNOWN).containsKey(capability))
				throw new IllegalArgumentException("Already has capability " + capabilityInstance.getClass());

			capabilities.get(Direction.UNKNOWN).put(capability, capabilityInstance);
		} else {
			facing.forEach(enumFacing -> {
				Map<Capability<?>, Object> caps = capabilities.get(enumFacing);

				if (caps.containsKey(capability))
					throw new IllegalArgumentException("Already has capability " + capabilityInstance.getClass());

				caps.put(capability, capabilityInstance);
			});
		}
		return capabilityInstance;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, Direction direction) {
		return Optional.of(direction)
			.filter(d -> d != Direction.UNKNOWN)
			.map(capabilities::get)
			.map(caps -> caps.containsValue(capability))
			.orElseGet(() -> capabilities.get(Direction.UNKNOWN).containsValue(capability));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, Direction direction) {
		return (T) Optional.of(direction)
			.filter(d -> d != Direction.UNKNOWN)
			.map(capabilities::get)
			.map(caps -> caps.get(capability))
			.orElseGet(() -> capabilities.get(Direction.UNKNOWN).get(capability));
	}
}
