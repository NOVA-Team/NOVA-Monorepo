package nova.core.item;

public class ItemStack {
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

	public void addStackSize(int size) {
		setStackSize(getStackSize() + size);
	}
}
