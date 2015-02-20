package nova.core.fluid;

import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class FluidFactory extends Factory<Fluid> implements Identifiable {

	public FluidFactory(Supplier<Fluid> constructor) {
		super(constructor);
	}

	/**
	 * Creates a new instance of this Fluid.
	 *
	 * @return A new Fluid instance with these parameters.
	 */
	public Fluid makeFluid() {
		Fluid newFluid = constructor.get();
		return newFluid;
	}
}
