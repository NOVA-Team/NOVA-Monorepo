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

package nova.core.command;

import nova.core.command.Command.CommandException;

/**
 * Class for type conversion and general validation of arguments passed to a
 * {@link Command}.
 * 
 * @author Vic Nightfall
 */
public class Args {

	private String[] args;

	public Args(String[] args) {
		this.args = args;
	}

	/**
	 * Returns an immutable copy of the internal argument array
	 * 
	 * @return
	 */
	public String[] args() {
		return args.clone();
	}

	// String

	public String checkString(int index) {
		checkArg(String.class, index);
		return args[index];
	}

	public boolean isString(int index) {
		return hasArg(index);
	}

	public String optString(int index, String def) {
		if (!hasArg(index))
			return def;
		return args[index];
	}

	// Integer

	public int checkInt(int index) {
		checkArg(int.class, index);
		try {
			return Integer.parseInt(args[index]);
		} catch (NumberFormatException e) {
			throw TypeException.create(int.class, args[index], index);
		}
	}

	public boolean isInt(int index) {
		if (!hasArg(index))
			return false;
		try {
			Integer.parseInt(args[index]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public int optInt(int index, int def) {
		if (!hasArg(index))
			return def;
		return checkInt(index);
	}

	// Float

	public float checkFloat(int index) {
		checkArg(float.class, index);
		try {
			return Float.parseFloat(args[index]);
		} catch (NumberFormatException e) {
			throw TypeException.create(float.class, args[index], index);
		}
	}

	public boolean isFloat(int index) {
		if (!hasArg(index))
			return false;
		try {
			Float.parseFloat(args[index]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public float optFloat(int index, float def) {
		if (!hasArg(index))
			return def;
		return checkFloat(index);
	}

	// Double

	public double checkDouble(int index) {
		checkArg(double.class, index);
		try {
			return Double.parseDouble(args[index]);
		} catch (NumberFormatException e) {
			throw TypeException.create(double.class, args[index], index);
		}
	}

	public boolean isDouble(int index) {
		if (!hasArg(index))
			return false;
		try {
			Double.parseDouble(args[index]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public double optDouble(int index, double def) {
		if (!hasArg(index))
			return def;
		return checkDouble(index);
	}

	// Long

	public long checkLong(int index) {
		checkArg(long.class, index);
		try {
			return Long.parseLong(args[index]);
		} catch (NumberFormatException e) {
			throw TypeException.create(long.class, args[index], index);
		}
	}

	public boolean isLong(int index) {
		if (!hasArg(index))
			return false;
		try {
			Long.parseLong(args[index]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public double optLong(int index, long def) {
		if (!hasArg(index))
			return def;
		return checkLong(index);
	}

	// Boolean

	public boolean checkBoolean(int index) {
		checkArg(long.class, index);
		if (!isBoolean(index))
			throw TypeException.create(boolean.class, args[index], index);
		return args[index].equalsIgnoreCase("true");
	}

	public boolean isBoolean(int index) {
		if (!hasArg(index))
			return false;
		return args[index].equalsIgnoreCase("true") || args[index].equalsIgnoreCase("false");
	}

	public boolean optBoolean(int index, boolean def) {
		if (!hasArg(index))
			return def;
		return checkBoolean(index);
	}

	// Enum

	public <E extends Enum<E>> E check(Class<E> enumClass, int index) {
		checkArg(enumClass, index);
		try {
			return Enum.valueOf(enumClass, args[index]);
		} catch (IllegalArgumentException e) {
			throw TypeException.createEnum(enumClass, args[index], index);
		}
	}

	public <E extends Enum<E>> boolean is(Class<E> enumClass, int index) {
		if (!hasArg(index))
			return false;
		try {
			Enum.valueOf(enumClass, args[index]);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public <E extends Enum<E>> E opt(Class<E> enumClass, int index, E def) {
		if (!hasArg(index))
			return def;
		return check(enumClass, index);
	}

	// TODO: Player check

	// Misc

	public boolean hasArg(int index) {
		return index < args.length;
	}

	private boolean checkArg(Class<?> expected, int index) {
		if (hasArg(index))
			return true;
		else
			throw new RangeException(expected, index);
	}

	public static class RangeException extends ArgException {
		public RangeException(Class<?> expected, int index) {
			super("Expected %s at index %s, argument not supplied", expected.getClass(), index);
		}
	}

	public static class TypeException extends ArgException {

		private TypeException(String message, Object... parameters) {
			super(message, parameters);
		}

		public static TypeException create(Class<?> expected, Object received, int index) {
			return new TypeException("Illegal argument at index %s: Got %s, expected %s", index, received, expected.getSimpleName());
		}

		public static <E extends Enum<E>> TypeException createEnum(Class<E> expected, Object received, int index) {
			return new TypeException("Illegal argument at index %s: %s is not a %s %s", index, received, expected.getSimpleName(), expected.getEnumConstants());
		}
	}

	public static class ArgException extends CommandException {
		public ArgException(String message) {
			super(message);
		}

		public ArgException(String message, Object... parameters) {
			super(message, parameters);
		}
	}
}
