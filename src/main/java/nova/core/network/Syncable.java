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

package nova.core.network;

import nova.core.util.ReflectionUtil;

import java.util.Arrays;

/**
 * @author Calclavia
 */
public interface Syncable {

	/**
	 * Reads a packet.
	 *
	 * @param packet - data encoded into the packet.
	 */
	default void read(Packet packet) {
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.stream(annotation.ids()).anyMatch(i -> i == packet.getID())) {
				try {
					field.setAccessible(true);
					Object o = field.get(this);
					if (o instanceof Syncable) {
						((Syncable) o).read(packet);
					} else {
						field.set(this, packet.read(field.getType()));
					}
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Writes a packet based on the arguments.
	 *
	 * @param packet - data encoded into the packet
	 */
	default void write(Packet packet) {
		ReflectionUtil.forEachRecursiveAnnotatedField(Sync.class, getClass(), (field, annotation) -> {
			if (Arrays.stream(annotation.ids()).anyMatch(i -> i == packet.getID())) {
				try {
					field.setAccessible(true);
					Object value = field.get(this);
					if(value != null) {
						packet.write(value);
					} else {
						throw new NullPointerException(
								String.format("Field %s in class: %s is null. Syncing nulls is not supported. Use Optional instead.",
										field.getName(), getClass()));
					}
					field.setAccessible(false);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
