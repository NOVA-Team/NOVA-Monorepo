package nova.core.fluid;

import nova.core.util.Registry;
import nova.core.util.exception.NovaException;

import java.util.Optional;
import java.util.function.Supplier;

public class FluidManager {
	public final Registry<FluidFactory> registry;

	private FluidManager(Registry<FluidFactory> fluidRegistry) {
		this.registry = fluidRegistry;
	}

	/**
	 * Registers a block with no constructor arguments
	 *
	 * @param fluid Fluid to register
	 * @return New block instance
	 */
	public Fluid register(Class<? extends Fluid> fluid) {
		return register(new FluidFactory(() -> {
			try {
				return fluid.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new NovaException();
			}
		}));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 *
	 * @param constructor Block instance {@link java.util.function.Supplier}
	 * @return Dummy block
	 */
	public Fluid register(Supplier<Fluid> constructor) {
		return register(new FluidFactory(constructor));
	}

	public Fluid register(FluidFactory factory) {
		registry.register(factory);
		return factory.getDummy();
	}

	public Optional<FluidFactory> getFluidFactory(String name) {
		return registry.get(name);
	}

	public Optional<Fluid> getFluid(String name) {

		if (getFluidFactory(name).isPresent()) {
			return Optional.of(getFluidFactory(name).get().makeFluid());
		}
		return Optional.empty();
	}
}
