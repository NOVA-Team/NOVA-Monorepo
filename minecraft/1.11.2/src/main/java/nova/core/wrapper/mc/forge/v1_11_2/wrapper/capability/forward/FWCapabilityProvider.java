/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.capability.forward;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import nova.core.util.EnumSelector;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ExE Boss
 */
public class FWCapabilityProvider implements ICapabilityProvider {

	private final Map<Capability<?>, Object> capabilities = new HashMap<>();
	private final Map<EnumFacing, Map<Capability<?>, Object>> sidedCapabilities = new HashMap<>();

	public FWCapabilityProvider() {
		for (EnumFacing facing : EnumFacing.VALUES)
			sidedCapabilities.put(facing, new HashMap<>());
	}

	public boolean hasCapabilities() {
		return !capabilities.isEmpty() || sidedCapabilities.values().stream().flatMap(map -> map.keySet().stream()).count() > 0;
	}

	public <T> T addCapability(Capability<T> capability, T capabilityInstance, EnumSelector<EnumFacing> facing) {
		if (facing == null || facing.allowsAll()) {
			if (capabilities.containsKey(capability))
				throw new IllegalArgumentException("Already has capability " + capabilityInstance.getClass());

			capabilities.put(capability, capabilityInstance);
		} else {
			facing.forEach(enumFacing -> {
				Map<Capability<?>, Object> capabilities = sidedCapabilities.get(enumFacing);

				if (capabilities.containsKey(capability))
					throw new IllegalArgumentException("Already has capability " + capabilityInstance.getClass());

				capabilities.put(capability, capabilityInstance);
			});
		}
		return capabilityInstance;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (facing != null ? sidedCapabilities.get(facing).containsValue(capability) : capabilities.containsValue(capability));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (!hasCapability(capability, facing)) return null;
		return (T) (facing != null ? sidedCapabilities.get(facing).get(capability) : capabilities.get(capability));
	}
}
