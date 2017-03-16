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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author ExE Boss
 */
public class FWCapabilityProvider implements NovaCapabilityProvider {

	private final EnumMap<Direction, Map<Capability<?>, Object>> capabilities = new EnumMap<>(Direction.class);

	public FWCapabilityProvider() {
		for (Direction direction : Direction.values())
			capabilities.put(direction, new HashMap<>());
	}

	@Override
	public boolean hasCapabilities() {
		return capabilities.values().parallelStream().map(map -> map.keySet().parallelStream()).count() > 0;
	}

	@Override
	public <T> T addCapability(Capability<T> capability, T capabilityInstance, EnumSelector<Direction> directions) {
		if (directions.allowsAll()) {
			if (capabilities.get(Direction.UNKNOWN).containsKey(capability))
				throw new IllegalArgumentException("Already has capability '" + capabilityInstance.getClass().getSimpleName() + '\'');

			capabilities.get(Direction.UNKNOWN).put(capability, capabilityInstance);
		} else {
			directions.forEach(direction -> {
				Map<Capability<?>, Object> caps = capabilities.get(direction);

				if (caps.containsKey(capability))
					throw new IllegalArgumentException("Already has capability '" + capabilityInstance.getClass().getSimpleName() + '\'');

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
	public <T> Optional<T> getCapability(Capability<T> capability, Direction direction) {
		return Optional.ofNullable((T) Optional.of(direction)
			.filter(d -> d != Direction.UNKNOWN)
			.map(capabilities::get)
			.map(caps -> caps.get(capability))
			.orElseGet(() -> capabilities.get(Direction.UNKNOWN).get(capability)));
	}
}
