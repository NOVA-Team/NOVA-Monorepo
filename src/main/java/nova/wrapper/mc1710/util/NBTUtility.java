package nova.wrapper.mc1710.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import nova.core.util.components.Storable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility that manages common NBT queueSave and load methods
 * @author Calclavia
 */
public class NBTUtility {

	/**
	 * Converts a Map of objects into NBT.
	 * @param map Map
	 * @return NBT
	 */
	public static NBTTagCompound mapToNBT(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		return mapToNBT(new NBTTagCompound(), map);
	}

	public static NBTTagCompound mapToNBT(NBTTagCompound nbt, Map<String, Object> map) {
		map.forEach((k, v) -> save(nbt, k, v));
		return nbt;
	}

	public static Map<String, Object> nbtToMap(NBTTagCompound nbt) {
		Map<String, Object> map = new HashMap<>();
		if (nbt != null) {
			Set<String> keys = nbt.func_150296_c();
			keys.forEach(k -> map.put(k, load(nbt, k)));
		}
		return map;
	}

	/**
	 * Saves an unknown object to NBT
	 * @param tag - NBTTagCompound to queueSave the tag too
	 * @param key - name to queueSave the object as
	 * @param value - the actual object
	 * @return the tag when done saving too i
	 */
	public static NBTTagCompound save(NBTTagCompound tag, String key, Object value) {
		if (value instanceof Map) {
			NBTTagCompound innerTag = new NBTTagCompound();
			Map<String, Object> map = (Map) value;
			map.forEach((k, v) -> save(innerTag, k, v));
			tag.setTag(key, innerTag);
		} else if (value instanceof Storable) {
			HashMap<String, Object> map = new HashMap<>();
			((Storable) value).save(map);
			save(tag, key, map);
		} else if (value instanceof Float) {
			tag.setFloat(key, (float) value);
		} else if (value instanceof Double) {
			tag.setDouble(key, (double) value);
		} else if (value instanceof Integer) {
			tag.setInteger(key, (int) value);
		} else if (value instanceof String) {
			tag.setString(key, (String) value);
		} else if (value instanceof Short) {
			tag.setShort(key, (short) value);
		} else if (value instanceof Byte) {
			tag.setByte(key, (byte) value);
		} else if (value instanceof Long) {
			tag.setLong(key, (long) value);
		} else if (value instanceof Boolean) {
			tag.setBoolean(key, (boolean) value);
		} else if (value instanceof byte[]) {
			tag.setByteArray(key, (byte[]) value);
		} else if (value instanceof int[]) {
			tag.setIntArray(key, (int[]) value);
		}

		return tag;
	}

	/**
	 * Reads an unknown object with a known name from NBT
	 * @param tag - tag to read the value from
	 * @param key - name of the value
	 * @return object or suggestionValue if nothing is found
	 */
	public static Object load(NBTTagCompound tag, String key) {
		if (tag != null && key != null) {
			NBTBase saveTag = tag.getTag(key);

			if (saveTag instanceof NBTTagFloat) {
				return tag.getFloat(key);
			} else if (saveTag instanceof NBTTagDouble) {
				return tag.getDouble(key);
			} else if (saveTag instanceof NBTTagInt) {
				return tag.getInteger(key);
			} else if (saveTag instanceof NBTTagString) {
				return tag.getString(key);
			} else if (saveTag instanceof NBTTagShort) {
				return tag.getShort(key);
			} else if (saveTag instanceof NBTTagByte) {
				return tag.getByte(key);
			} else if (saveTag instanceof NBTTagLong) {
				return tag.getLong(key);
			} else if (saveTag instanceof NBTTagByteArray) {
				return tag.getByteArray(key);
			} else if (saveTag instanceof NBTTagIntArray) {
				return tag.getIntArray(key);
			} else if (saveTag instanceof NBTTagCompound) {
				NBTTagCompound compoundTag = tag.getCompoundTag(key);
				HashMap<String, Object> map = new HashMap<>();
				Set<String> keys = compoundTag.func_150296_c();
				keys.forEach(k -> map.put(k, load(compoundTag, k)));
				return map;
			}
		}
		return null;
	}
}
