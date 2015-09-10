package nova.core.component.fluid;

import nova.core.util.Manager;
import nova.core.util.Registry;

import java.util.function.Supplier;

public class FluidManager extends Manager<Fluid, FluidFactory> {
	public final FluidFactory water;
	public final FluidFactory lava;

	private FluidManager(Registry<FluidFactory> fluidRegistry) {
		super(fluidRegistry);
		//TODO: Too Minecraft specific. Implementation should be hidden.
		this.water = register(() -> new Fluid("water"));
		this.lava = register(() -> new Fluid("lava"));
	}

	@Override
	public FluidFactory register(Supplier<Fluid> constructor) {
		return register(new FluidFactory(constructor));
	}

	@Override
	public FluidFactory register(FluidFactory factory) {
		registry.register(factory);
		return factory;
	}

}
