package nova.core.item;

/**
 * Stack of items
 */
public class ItemStack implements Cloneable {
	private static final int MAX_STACK_SIZE = 64; // TODO
	private final Item item;
	private int stackSize;

	/**
	 * Creates new ItemStack
	 *
	 * @param item Item to create stack of
	 * @param stackSize Number of items in this stack
	 */
	public ItemStack(Item item, int stackSize) {
		this.item = item;
		this.setStackSize(stackSize);
	}

	/**
	 * Creates new ItemStack with one item
	 *
	 * @param item Item to create stack of
	 */
	public ItemStack(Item item) {
		this(item, 1);
	}

	/**
	 * @return {@link Item} of which this ItemStack is made of
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return Size of this stack size
	 */
	public int getStackSize() {
		return stackSize;
	}

	/**
	 * Sets new size of this ItemStack
	 *
	 * @param size New size
	 */
	public void setStackSize(int size) {
		stackSize = Math.min(MAX_STACK_SIZE, size);
		if (stackSize < 1) {
			stackSize = 1;
		}
	}

	/**
	 * Adds size to this ItemStack
	 *
	 * @param size Size to add
	 * @return Size added
	 */
	public int addStackSize(int size) {
		int original = getStackSize();
		setStackSize(original + size);
		return stackSize - original;
	}

	@Override
	public ItemStack clone() {
		ItemStack cloned = new ItemStack(item, stackSize);
		return cloned;
	}

	/**
	 * Returns new ItemStack of the same {@link Item} with specified size
	 *
	 * @param amount Size of cloned ItemStack
	 * @return new ItemStack
	 */
	public ItemStack withAmount(int amount) {
		ItemStack cloned = clone();
		cloned.setStackSize(amount);
		return cloned;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ItemStack)) {
			return false;
		}
		ItemStack i = (ItemStack) o;
		return sameStackType(i) && i.stackSize == stackSize;
	}

	/**
	 * Check if this ItemStack is of type of another ItemStack
	 *
	 * @param stack The another ItemStack
	 * @return Result
	 */
	public boolean sameStackType(ItemStack stack) {
		return stack.item == item;
	}

	@Override
	public int hashCode() {
		return 31 * stackSize + item.getID().hashCode();
	}
}
