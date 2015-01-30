package nova.core.fluid;

import java.util.Optional;

import nova.core.util.Registry;

public class FluidManager {
	public final Registry<Fluid> fluidRegistry;

	private FluidManager(Registry<Fluid> fluidRegistry) {
		this.fluidRegistry = fluidRegistry;
	}
	
	public Optional<Fluid> getFluid(String name) {
		return fluidRegistry.get(name);
	}
}
