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

package nova.core.util.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Metric system of measurement.
 *
 * @author Calclavia
 */
public class UnitPrefix {
	private static final List<UnitPrefix> UNIT_PREFIXES = new ArrayList<>();

//	public static final UnitPrefix YOCTO = new UnitPrefix("Yocto", "y", 0.000000000000000000000001);
//	public static final UnitPrefix ZEPTO = new UnitPrefix("Zepto", "z", 0.000000000000000000001);
//	public static final UnitPrefix ATTO  = new UnitPrefix("Atto",  "a", 0.000000000000000001);
//	public static final UnitPrefix FEMTO = new UnitPrefix("Femto", "p", 0.000000000000001);
//	public static final UnitPrefix PICO  = new UnitPrefix("Pico",  "p", 0.000000000001);
//	public static final UnitPrefix NANO  = new UnitPrefix("Nano",  "n", 0.000000001);
	public static final UnitPrefix MICRO = new UnitPrefix("Micro", "µ", 0.000001);
	public static final UnitPrefix MILLI = new UnitPrefix("Milli", "m", 0.001);
	public static final UnitPrefix BASE  = new UnitPrefix("",      "",  1);
	public static final UnitPrefix KILO  = new UnitPrefix("Kilo",  "k", 1000);
	public static final UnitPrefix MEGA  = new UnitPrefix("Mega",  "M", 1000000);
	public static final UnitPrefix GIGA  = new UnitPrefix("Giga",  "G", 1000000000);
	public static final UnitPrefix TERA  = new UnitPrefix("Tera",  "T", 1000000000000d);
	public static final UnitPrefix PETA  = new UnitPrefix("Peta",  "P", 1000000000000000d);
	public static final UnitPrefix EXA   = new UnitPrefix("Exa",   "E", 1000000000000000000d);
	public static final UnitPrefix ZETTA = new UnitPrefix("Zetta", "Z", 1000000000000000000000d);
	public static final UnitPrefix YOTTA = new UnitPrefix("Yotta", "Y", 1000000000000000000000000d);

	/**
	 * long name for the unit
	 */
	public final String name;
	/**
	 * short unit version of the unit
	 */
	public final String symbol;
	/**
	 * Point by which a number is consider to be of this unit
	 */
	public final double value;

	private UnitPrefix(String name, String symbol, double value) {
		this.name = name;
		this.symbol = symbol;
		this.value = value;
		UNIT_PREFIXES.add(this);
	}

	public String getName(boolean getShort) {
		if (getShort) {
			return symbol;
		} else {
			return name;
		}
	}

	/**
	 * Divides the value by the unit value start
	 */
	public double process(double value) {
		return value / this.value;
	}

	/**
	 * Checks if a value is above the unit value start
	 */
	public boolean isAbove(double value) {
		return value > this.value;
	}

	/**
	 * Checks if a value is lower than the unit value start
	 */
	public boolean isBellow(double value) {
		return value < this.value;
	}

	public static List<UnitPrefix> getPrefixes() {
		return Collections.unmodifiableList(UNIT_PREFIXES);
	}
}
