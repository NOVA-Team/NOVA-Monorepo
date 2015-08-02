package nova.core.wrapper.mc18.wrapper.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;

/**
 * Created by Stan on 3/02/2015.
 */
public class WrappedNBTTagCompound extends NBTTagCompound {
	private final Item item;

	public WrappedNBTTagCompound(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public NBTBase copy() {
		WrappedNBTTagCompound result = new WrappedNBTTagCompound(item);
		getKeySet().forEach(s -> result.setTag((String) s, getTag((String) s).copy()));
		return result;
	}
}
