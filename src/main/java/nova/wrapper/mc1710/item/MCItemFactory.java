package nova.wrapper.mc1710.item;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.wrapper.mc1710.util.StoreUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Stan on 3/02/2015.
 */
public class MCItemFactory extends ItemFactory {
    private final net.minecraft.item.Item item;
    private final int meta;

    public MCItemFactory(net.minecraft.item.Item item, int meta) {
        super(() -> new MCItem(item, meta, null));

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
    public Item makeItem(Map<String, Object> data) {
        int meta = (Integer) data.getOrDefault("damage", this.meta);
		NBTTagCompound nbtData = StoreUtility.mapToNBT(data);
		return new MCItem(item, meta, nbtData);
    }

    @Override
    public Map<String, Object> saveItem(Item item) {
        if (!(item instanceof MCItem))
            throw new IllegalArgumentException("This factory can only handle MCItems");

        MCItem mcItem = (MCItem) item;

		Map<String, Object> result = StoreUtility.nbtToMap(mcItem.getTag());
		if (result == null)
            result = new HashMap<String, Object>();

        if (mcItem.getMeta() != meta)
            result.put("damage", mcItem.getMeta());

        return result;
    }
}
