package nova.core.item;

public class ItemStack implements Cloneable {
	private static final int MAX_STACK_SIZE = 64; // TODO
	private final Item item;
	private int stackSize;

	public ItemStack(Item item, int stackSize) {
		this.item = item;
		this.setStackSize(stackSize);
	}

	public ItemStack(Item item) {
		this(item, 1);
	}

	public Item getItem() {
		return item;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int size) {
		stackSize = Math.min(MAX_STACK_SIZE, size);
		if (stackSize < 1) {
			stackSize = 1;
		}
	}

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

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ItemStack)) {
			return false;
		}
		ItemStack i = (ItemStack) o;
		return sameStackType(i) && i.stackSize == stackSize;
	}

	public boolean sameStackType(ItemStack i) {
		return i.item == item;
	}

	@Override
	public int hashCode() {
		return 31 * stackSize + item.getID().hashCode();
	}
}
