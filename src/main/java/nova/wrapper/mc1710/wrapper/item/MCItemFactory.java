package nova.wrapper.mc1710.wrapper.item;

import net.minecraft.nbt.NBTTagCompound;
import nova.internal.Game;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.retention.Data;

/**
 * A Minecraft wrapped item factory.
 * @author Stan
 * @since 3/02/2015.
 */
public class MCItemFactory extends ItemFactory {
	private final net.minecraft.item.Item item;
	private final int meta;

	public MCItemFactory(net.minecraft.item.Item item, int meta) {
		super((args) -> new BWItem(item, meta, null));

		this.item = item;
		this.meta = meta;
	}

	public net.minecraft.item.Item getItem() {
		return item;
	}

	public int getMeta() {
		return meta;
	}

	@Override
	public Item makeItem(Data data, Object... args) {
		int meta = (Integer) data.getOrDefault("damage", this.meta);
		NBTTagCompound nbtData = Game.natives().toNative(data);
		return new BWItem(item, meta, nbtData);
	}

	@Override
	public Data saveItem(Item item) {
		if (!(item instanceof BWItem)) {
			throw new IllegalArgumentException("This factory can only handle MCItems");
		}

		BWItem mcItem = (BWItem) item;

		Data result = Game.natives().toNova(mcItem.getTag());
		if (result == null) {
			result = new Data();
		}

		if (mcItem.getMeta() != meta) {
			result.put("damage", mcItem.getMeta());
		}

		return result;
	}
}
