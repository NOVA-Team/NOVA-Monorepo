package nova.core.fluid;

import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Function;

public class FluidManager extends Manager<Fluid, FluidFactory> {
	public final FluidFactory water;
	public final FluidFactory lava;

	private FluidManager(Registry<FluidFactory> fluidRegistry) {
		super(fluidRegistry);
		this.water = register((args) -> new Fluid("water"));
		this.lava = register((args) -> new Fluid("lava"));
	}

	@Override
	public FluidFactory register(Function<Object[], Fluid> constructor) {
		return register(new FluidFactory(constructor));
	}

	@Override
	public FluidFactory register(FluidFactory factory) {
		registry.register(factory);
		return factory;
	}
}
