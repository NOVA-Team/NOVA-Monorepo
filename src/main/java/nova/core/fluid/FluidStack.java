package nova.core.fluid;



public class FluidStack implements Cloneable {
	private int stackSize;
	private Fluid fluid;
	
	/**
	 * Creates new FluidStack
	 * 
	 * @param fluid Fluid to create stack of
	 * @param stackSize Amount of fluid to contain within this stack
	 */
	public FluidStack(Fluid fluid, int stackSize) {
		this.fluid = fluid;
		this.stackSize = stackSize;
	}
	
	/**
	 * Creates new FluidStack with one unit of fluid
	 * 
	 * @param fluid Fluid to create stack of
	 */
	public FluidStack(Fluid fluid){
		this(fluid, 1);
	}
	
	/**
	 * @return {@link Fluid} of which this FluidStack is made of
	 */
	public Fluid getFluid() {
		return fluid;
	}
	
	/**
	 * @return Amount of fluid in this stack
	 */
	public int getStackSize() {
		return stackSize;
	}

	/**
	 * Sets new size of this FluidStack
	 * 
	 * @param stackSize New size
	 */
	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
		if (stackSize < 1) {
			stackSize = 0;
		}
	}
	
	/**
	 * Adds fluid to this FluidStack
	 * 
	 * @param size Amount of fluid to add
	 * @return Size added
	 */
	public int addStackSize(int size) {
		int original = getStackSize();
		setStackSize(original + size);
		return stackSize - original;
	}
	
	/**
	 * Removes fluid to this FluidStack
	 * 
	 * @param size Amount of fluid to remove
	 * @return Fluid removed
	 */
	public int subStackSize(int size) {
		int original = getStackSize();
		setStackSize(original - size);
		return original - stackSize;
	}
	
	@Override
	public FluidStack clone() {
		FluidStack cloned = new FluidStack(fluid, stackSize);
		return cloned;
	}
	
	/**
	 * Returns new FluidStack of the same {@link Fluid} with specified fluid
	 * 
	 * @param amount Amount of fluid in cloned FluidStack
	 * @return new FluidStack
	 */
	public FluidStack withAmount(int amount) {
		FluidStack cloned = clone();
		cloned.setStackSize(amount);
		return cloned;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof FluidStack)) {
			return false;
		}
		FluidStack i = (FluidStack) o;
		return sameStackType(i) && i.stackSize == stackSize;
	}
	
	/**
	 * Check if this FluidStack is of type of another FluidStack
	 * 
	 * @param stack The another FluidStack
	 * @return Result
	 */
	public boolean sameStackType(FluidStack stack) {
		return stack.fluid == fluid;
	}
	
	@Override
	public int hashCode() {
		return 31 * stackSize + fluid.getID().hashCode();
	}
	
}
