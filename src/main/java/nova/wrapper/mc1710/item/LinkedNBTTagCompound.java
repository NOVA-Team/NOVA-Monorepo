package nova.wrapper.mc1710.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import nova.core.item.Item;

import java.util.Iterator;

/**
 * Created by Stan on 3/02/2015.
 */
public class LinkedNBTTagCompound extends NBTTagCompound {
    private final Item item;

    public LinkedNBTTagCompound(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public NBTBase copy() {
        LinkedNBTTagCompound result = new LinkedNBTTagCompound(item);
        Iterator iterator = this.func_150296_c().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            result.setTag(s, getTag(s).copy());
        }

        return result;
    }
}
