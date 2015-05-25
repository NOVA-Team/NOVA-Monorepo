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
import nova.core.retention.Data;

import java.util.Set;

/**
 * Utility that manages common NBT queueSave and load methods
 * @author Calclavia
 */
public class DataUtility {

	/**
	 * Converts a Map of objects into NBT.
	 * @param data Map
	 * @return NBT
	 */
	public static NBTTagCompound dataToNBT(Data data) {
		if (data == null) {
			return null;
		}

		return dataToNBT(new NBTTagCompound(), data);
	}

	public static NBTTagCompound dataToNBT(NBTTagCompound nbt, Data data) {
		if (data.className != null) {
			nbt.setString("class", data.className);
		}
		data.forEach((k, v) -> save(nbt, k, v));
		return nbt;
	}

	public static Data nbtToData(NBTTagCompound nbt) {
		Data data = new Data();
		if (nbt != null) {
			data.className = nbt.getString("class");
			Set<String> keys = nbt.func_150296_c();
			keys.forEach(k -> data.put(k, load(nbt, k)));
		}
		return data;
	}

	/**
	 * Saves an unknown object to NBT
	 * @param tag - NBTTagCompound to queueSave the tag too
	 * @param key - name to queueSave the object as
	 * @param value - the actual object
	 * @return the tag when done saving too i
	 */
	public static NBTTagCompound save(NBTTagCompound tag, String key, Object value) {
		if (value instanceof Boolean) {
			tag.setBoolean("isBoolean", true);
			tag.setBoolean(key, (boolean) value);
		} else if (value instanceof Byte) {
			tag.setBoolean("isBoolean", false);
			tag.setByte(key, (byte) value);
		} else if (value instanceof Short) {
			tag.setShort(key, (short) value);
		} else if (value instanceof Integer) {
			tag.setInteger(key, (int) value);
		} else if (value instanceof Long) {
			tag.setLong(key, (long) value);
		} else if (value instanceof Character) {
			tag.setInteger(key, (Character) value);
		} else if (value instanceof Float) {
			tag.setFloat(key, (float) value);
		} else if (value instanceof Double) {
			tag.setDouble(key, (double) value);
		} else if (value instanceof String) {
			tag.setString(key, (String) value);
		} else if (value instanceof Data) {
			NBTTagCompound innerTag = new NBTTagCompound();
			dataToNBT(innerTag, (Data) value);
			tag.setTag(key, innerTag);
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
				if (tag.getBoolean("isBoolean")) {
					return tag.getBoolean(key);
				} else {
					return tag.getByte(key);
				}
			} else if (saveTag instanceof NBTTagLong) {
				return tag.getLong(key);
			} else if (saveTag instanceof NBTTagByteArray) {
				return tag.getByteArray(key);
			} else if (saveTag instanceof NBTTagIntArray) {
				return tag.getIntArray(key);
			} else if (saveTag instanceof NBTTagCompound) {
				NBTTagCompound innerTag = tag.getCompoundTag(key);
				return nbtToData(innerTag);
			}
		}
		return null;
	}
}
