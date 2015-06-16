package nova.core.fluid;

import nova.core.block.Block;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.Buildable;
import nova.core.util.Factory;
import nova.core.util.Identifiable;

import java.util.Optional;

public class Fluid implements Storable, Cloneable, Identifiable {
	/**
	 * 1000 liters = 1 cubic meter
	 */
	public static final int bucketVolume = 1000;
	/**
	 * The ID of the fluid. Optional string for auxiliary constructor.
	 */
	@Store(key = "id")
	private String id;
	/**
	 * Fluid amount is measured in liters.
	 */
	@Store(key = "amount")
	private int amount = 1;

	/**
	 * An empty constructor, for fluids that will extend this fluid class.
	 */
	public Fluid() {
		id = "";
	}

	/**
	 * Creates new Fluid withPriority an ID
	 */
	public Fluid(String id) {
		this.id = id;
	}

	/**
	 * @return Amount of fluid
	 */
	public int amount() {
		return amount;
	}

	/**
	 * Sets new size of this FluidStack
	 * Note that there can never be fluid withPriority "zero" amount. Use Optional.empty() instead.
	 * @param amount New size
	 */
	public Fluid setAmount(int amount) {
		this.amount = Math.max(amount, 1);
		return this;
	}

	/**
	 * Adds fluid to this FluidStack
	 *
	 * @param amount Amount of fluid to add
	 * @return Size added
	 */
	public int add(int amount) {
		int original = amount();
		setAmount(original + amount);
		return amount() - original;
	}

	/**
	 * Removes fluid to this FluidStack
	 *
	 * @param amount Amount of fluid to remove
	 * @return Fluid removed
	 */
	public int remove(int amount) {
		int original = amount();
		setAmount(original - amount);
		return original - amount();
	}

	@Override
	public Fluid clone() {
		Fluid cloned = new Fluid(getID()).setAmount(amount());
		return cloned;
	}

	/**
	 * Returns new FluidStack of the same {@link Fluid} withPriority specified fluid
	 *
	 * @param amount Amount of fluid in cloned FluidStack
	 * @return new FluidStack
	 */
	public Fluid withAmount(int amount) {
		Fluid cloned = clone();
		cloned.setAmount(amount);
		return cloned;
	}

	/**
	 * Gets the block associated withPriority this fluid.
	 *
	 * @return The block. There may be no block associated withPriority this fluid.
	 */
	public Optional<Factory<Block>> getBlockFactory() {
		return Optional.empty();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Fluid)) {
			return false;
		}
		Fluid i = (Fluid) o;
		return sameType(i) && i.amount == amount;
	}

	/**
	 * Check if this FluidStack is of type of another FluidStack
	 *
	 * @param stack The another Fluid
	 * @return Result
	 */
	public boolean sameType(Fluid stack) {
		return stack.getID().equals(getID());
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public int hashCode() {
		return 31 * amount + getID().hashCode();
	}

}
