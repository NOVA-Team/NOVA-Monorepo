package nova.core.component.fluid;

import nova.core.util.Factory;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class FluidFactory extends Factory<FluidFactory, Fluid> {

	public FluidFactory(Supplier<Fluid> constructor) {
		super(constructor);
	}

	public FluidFactory(Supplier<Fluid> constructor, Function<Fluid, Fluid> processor) {
		super(constructor, processor);
	}

	@Override
	public FluidFactory selfConstructor(Supplier<Fluid> constructor, Function<Fluid, Fluid> processor) {
		return new FluidFactory(constructor, processor);
	}
}
