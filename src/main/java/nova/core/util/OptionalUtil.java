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

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * @author ExE Boss
 */
public final class OptionalUtil {

	private OptionalUtil() {}

	// Optional

	public static <T> Optional<T> mapToObj(OptionalInt op, IntFunction<T> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return Optional.of(function.apply(op.getAsInt()));
	}
	public static <T> Optional<T> mapToObj(OptionalLong op, LongFunction<T> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return Optional.of(function.apply(op.getAsLong()));
	}
	public static <T> Optional<T> mapToObj(OptionalDouble op, DoubleFunction<T> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return Optional.of(function.apply(op.getAsDouble()));
	}

	public static <T> Optional<T> flatMapToObj(OptionalInt op, IntFunction<Optional<T>> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return function.apply(op.getAsInt());
	}
	public static <T> Optional<T> flatMapToObj(OptionalLong op, LongFunction<Optional<T>> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return function.apply(op.getAsLong());
	}
	public static <T> Optional<T> flatMapToObj(OptionalDouble op, DoubleFunction<Optional<T>> function) {
		if (!op.isPresent())
			return Optional.empty();
		else
			return function.apply(op.getAsDouble());
	}

	// OptionalInt

	public static <T> OptionalInt mapToInt(Optional<T> op, ToIntFunction<T> function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return OptionalInt.of(function.applyAsInt(op.get()));
	}
	public static OptionalInt mapToInt(OptionalLong op, LongToIntFunction function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return OptionalInt.of(function.applyAsInt(op.getAsLong()));
	}
	public static OptionalInt mapToInt(OptionalDouble op, DoubleToIntFunction function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return OptionalInt.of(function.applyAsInt(op.getAsDouble()));
	}

	public static <T> OptionalInt flatMapToInt(Optional<T> op, Function<T, OptionalInt> function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return function.apply(op.get());
	}
	public static OptionalInt flatMapToInt(OptionalLong op, LongFunction<OptionalInt> function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return function.apply(op.getAsLong());
	}
	public static OptionalInt flatMapToInt(OptionalDouble op, DoubleFunction<OptionalInt> function) {
		if (!op.isPresent())
			return OptionalInt.empty();
		else
			return function.apply(op.getAsDouble());
	}

	public static OptionalInt parseInt(String string) {
		try {
			return OptionalInt.of(Integer.parseInt(string));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}
	public static OptionalInt parseInt(String string, int radix) {
		try {
			return OptionalInt.of(Integer.parseInt(string, radix));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public static OptionalInt parseUnsignedInt(String string) {
		try {
			return OptionalInt.of(Integer.parseUnsignedInt(string));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}
	public static OptionalInt parseUnsignedInt(String string, int radix) {
		try {
			return OptionalInt.of(Integer.parseUnsignedInt(string, radix));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	// OptionalLong

	public static <T> OptionalLong mapToLong(Optional<T> op, ToLongFunction<T> function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return OptionalLong.of(function.applyAsLong(op.get()));
	}
	public static OptionalLong mapToLong(OptionalInt op, IntToLongFunction function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return OptionalLong.of(function.applyAsLong(op.getAsInt()));
	}
	public static OptionalLong mapToLong(OptionalDouble op, DoubleToLongFunction function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return OptionalLong.of(function.applyAsLong(op.getAsDouble()));
	}

	public static <T> OptionalLong flatMapToLong(Optional<T> op, Function<T, OptionalLong> function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return function.apply(op.get());
	}
	public static OptionalLong flatMapToLong(OptionalInt op, IntFunction<OptionalLong> function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return function.apply(op.getAsInt());
	}
	public static OptionalLong flatMapToLong(OptionalDouble op, DoubleFunction<OptionalLong> function) {
		if (!op.isPresent())
			return OptionalLong.empty();
		else
			return function.apply(op.getAsDouble());
	}

	public static OptionalLong parseLong(String string) {
		try {
			return OptionalLong.of(Long.parseLong(string));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}
	public static OptionalLong parseLong(String string, int radix) {
		try {
			return OptionalLong.of(Long.parseLong(string, radix));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	public static OptionalLong parseUnsignedLong(String string) {
		try {
			return OptionalLong.of(Long.parseUnsignedLong(string));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}
	public static OptionalLong parseUnsignedLong(String string, int radix) {
		try {
			return OptionalLong.of(Long.parseUnsignedLong(string, radix));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	// OptionalDouble

	public static <T> OptionalDouble mapToDouble(Optional<T> op, ToDoubleFunction<T> function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return OptionalDouble.of(function.applyAsDouble(op.get()));
	}
	public static OptionalDouble mapToDouble(OptionalInt op, IntToDoubleFunction function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return OptionalDouble.of(function.applyAsDouble(op.getAsInt()));
	}
	public static OptionalDouble mapToDouble(OptionalLong op, LongToDoubleFunction function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return OptionalDouble.of(function.applyAsDouble(op.getAsLong()));
	}

	public static <T> OptionalDouble flatMapToDouble(Optional<T> op, Function<T, OptionalDouble> function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return function.apply(op.get());
	}
	public static OptionalDouble flatMapToDouble(OptionalInt op, IntFunction<OptionalDouble> function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return function.apply(op.getAsInt());
	}
	public static OptionalDouble flatMapToDouble(OptionalLong op, LongFunction<OptionalDouble> function) {
		if (!op.isPresent())
			return OptionalDouble.empty();
		else
			return function.apply(op.getAsLong());
	}

	public static OptionalDouble parseDouble(String string) {
		try {
			return OptionalDouble.of(Double.parseDouble(string));
		} catch (NumberFormatException e) {
			return OptionalDouble.empty();
		}
	}
}
