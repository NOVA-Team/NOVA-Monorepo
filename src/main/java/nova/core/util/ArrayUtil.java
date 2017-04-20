/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.util;

import java.util.Arrays;

/**
 * @author ExE Boss
 */
public class ArrayUtil {

	public static <T> T[] join(T object, T[] array) {
		T[] ret = Arrays.copyOf(array, array.length + 1);
		System.arraycopy(array, 0, ret, 1, array.length);
		ret[0] = object;
		return ret;
	}

	public static <T> T[] join(T[] array1, T[] array2) {
		T[] ret = Arrays.copyOf(array1, array1.length + array2.length);
		System.arraycopy(array2, 0, ret, array1.length, array2.length);
		return ret;
	}

	public static <T> T[] join(T[] array, T object) {
		T[] ret = Arrays.copyOf(array, array.length + 1);
		ret[array.length] = object;
		return ret;
	}
}
