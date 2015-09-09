package nova.core.component.fluid;

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
}
