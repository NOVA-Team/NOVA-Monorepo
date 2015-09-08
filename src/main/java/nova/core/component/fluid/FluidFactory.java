package nova.core.component.fluid;

import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Function;

/**
 * @author Calclavia
 */
public class FluidFactory extends Factory<Fluid> implements Identifiable {

	public FluidFactory(Function<Object[], Fluid> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of this Fluid.
	 * @return A new Fluid instance with these parameters.
	 */
	public Fluid makeFluid(Object... args) {
		Fluid newFluid = constructor.apply(args);
		return newFluid;
	}
}
