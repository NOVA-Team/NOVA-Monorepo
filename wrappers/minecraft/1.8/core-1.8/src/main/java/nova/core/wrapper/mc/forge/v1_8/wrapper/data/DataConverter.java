/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.wrapper.mc.forge.v1_8.wrapper.data;

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
import nova.core.nativewrapper.NativeConverter;
import nova.core.retention.Data;
import nova.internal.core.Game;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

/**
 * Utility that manages common NBT queueSave and load methods
 * @author Calclavia
 */
public class DataConverter implements NativeConverter<Data, NBTTagCompound> {

	public static DataConverter instance() {
		return Game.natives().getNative(Data.class, NBTTagCompound.class);
	}

	@Override
	public Class<Data> getNovaSide() {
		return Data.class;
	}

	@Override
	public Class<NBTTagCompound> getNativeSide() {
		return NBTTagCompound.class;
	}

	@Override
	public Data toNova(NBTTagCompound nbt) {
		Data data = new Data();
		if (nbt != null) {
			data.className = nbt.getString("class");
			@SuppressWarnings("unchecked")
			Set<String> keys = nbt.getKeySet();
			keys.stream().filter(k -> k != null && !"class".equals(k)).filter(Data.ILLEGAL_SUFFIX.asPredicate().negate()).forEach(k -> data.put(k, load(nbt, k)));
		}
		return data;
	}

	@Override
	public NBTTagCompound toNative(Data data) {
		if (data == null) {
			return null;
		}

		return toNative(new NBTTagCompound(), data);
	}

	public NBTTagCompound toNative(NBTTagCompound nbt, Data data) {
		if (data.className != null) {
			nbt.setString("class", data.className);
		}
		data.forEach((k, v) -> save(nbt, k, v));
		return nbt;
	}

	/**
	 * Saves an unknown object to NBT
	 * @param tag - NBTTagCompound to queueSave the tag too
	 * @param key - name to queueSave the object as
	 * @param value - the actual object
	 * @return the tag when done saving too i
	 */
	public NBTTagCompound save(NBTTagCompound tag, String key, Object value) {
		if (value instanceof Boolean) {
			tag.setBoolean(key + "::nova.isBoolean", true);
			tag.setBoolean(key, (boolean) value);
		} else if (value instanceof Byte) {
			tag.setBoolean(key + "::nova.isBoolean", false);
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
		} else if (value instanceof BigInteger) {
			tag.setBoolean(key + "::nova.isBigInteger", true);
			tag.setString(key, ((BigInteger) value).toString());
		} else if (value instanceof BigDecimal) {
			tag.setBoolean(key + "::nova.isBigDecimal", true);
			tag.setString(key, ((BigDecimal) value).toString());
		} else if (value instanceof String) {
			tag.setString(key, (String) value);
		} else if (value instanceof Data) {
			NBTTagCompound innerTag = new NBTTagCompound();
			toNative(innerTag, (Data) value);
			tag.setTag(key, innerTag);
		}
		return tag;
	}

	/**
	 * Reads an unknown object withPriority a known name from NBT
	 * @param tag - tag to read the value from
	 * @param key - name of the value
	 * @return object or suggestionValue if nothing is found
	 */
	public Object load(NBTTagCompound tag, String key) {
		if (tag != null && key != null) {
			NBTBase saveTag = tag.getTag(key);

			if (saveTag instanceof NBTTagFloat) {
				return tag.getFloat(key);
			} else if (saveTag instanceof NBTTagDouble) {
				return tag.getDouble(key);
			} else if (saveTag instanceof NBTTagInt) {
				return tag.getInteger(key);
			} else if (saveTag instanceof NBTTagString) {
				if (tag.getBoolean(key + "::nova.isBigInteger")) {
					return new BigInteger(tag.getString(key));
				} else if (tag.getBoolean(key + "::nova.isBigDecimal")) {
					return new BigDecimal(tag.getString(key));
				} else {
					return tag.getString(key);
				}
			} else if (saveTag instanceof NBTTagShort) {
				return tag.getShort(key);
			} else if (saveTag instanceof NBTTagByte) {
				if (tag.getBoolean(key + "::nova.isBoolean")) {
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
				return toNova(innerTag);
			}
		}
		return null;
	}
}
