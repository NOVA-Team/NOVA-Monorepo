package nova.core.fluid;

import nova.core.util.Registry;

import java.util.Optional;

public class FluidManager {
	public final Registry<Fluid> fluidRegistry;

	private FluidManager(Registry<Fluid> fluidRegistry) {
		this.fluidRegistry = fluidRegistry;
	}

	public Optional<Fluid> getFluid(String name) {
		return fluidRegistry.get(name);
	}
}
