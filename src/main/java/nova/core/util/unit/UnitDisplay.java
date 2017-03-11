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

import nova.core.util.math.MathUtil;

import java.util.Objects;
import java.util.function.DoubleSupplier;

/**
 * An easy way to display information on electricity for the client.
 *
 * @author Calclavia
 */
public class UnitDisplay {
	public final Unit unit;
	public final DoubleSupplier value;
	public final boolean useSymbol;
	public final int decimalPlaces;
	public final boolean isSimple;
	public final boolean removeTrailingZeroes;

	public UnitDisplay(Unit unit, DoubleSupplier value, boolean useSymbol, int decimalPlaces, boolean simple, boolean removeTrailingZeroes) {
		this.unit = Objects.requireNonNull(unit, "unit");
		this.value = Objects.requireNonNull(value, "value");
		this.useSymbol = useSymbol;
		this.decimalPlaces = decimalPlaces;
		this.isSimple = simple;
		this.removeTrailingZeroes = removeTrailingZeroes;
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, boolean useSymbol, int decimalPlaces, boolean simple) {
		this(unit, value, useSymbol, 2, simple, true);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, boolean useSymbol, boolean simple) {
		this(unit, value, useSymbol, 2, simple);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, int decimalPlaces, boolean simple) {
		this(unit, value, true, decimalPlaces, simple);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, boolean useSymbol, int decimalPlaces) {
		this(unit, value, useSymbol, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, int decimalPlaces) {
		this(unit, value, true, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value, boolean simple) {
		this(unit, value, true, 2, simple);
	}

	public UnitDisplay(Unit unit, DoubleSupplier value) {
		this(unit, value, true, 2, false);
	}

	public UnitDisplay(Unit unit, double value, boolean useSymbol, int decimalPlaces, boolean simple) {
		this(unit, () -> value, useSymbol, decimalPlaces, simple);
	}

	public UnitDisplay(Unit unit, double value, boolean useSymbol, boolean simple) {
		this(unit, value, useSymbol, 2, simple);
	}

	public UnitDisplay(Unit unit, double value, int decimalPlaces, boolean simple) {
		this(unit, value, true, decimalPlaces, simple);
	}

	public UnitDisplay(Unit unit, double value, boolean useSymbol, int decimalPlaces) {
		this(unit, value, useSymbol, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, double value, int decimalPlaces) {
		this(unit, value, true, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, double value, boolean simple) {
		this(unit, value, true, 2, simple);
	}

	public UnitDisplay(Unit unit, double value) {
		this(unit, value, true, 2, false);
	}

	public static double roundDecimals(double d) {
		return MathUtil.roundDecimals(d, 2);
	}

	public UnitDisplay multiply(double value) {
		return new UnitDisplay(unit, () -> this.value.getAsDouble() * value, isSimple, decimalPlaces, useSymbol, removeTrailingZeroes);
	}

	public UnitDisplay simple() {
		return (isSimple ? this : new UnitDisplay(unit, value, true, decimalPlaces, useSymbol, removeTrailingZeroes));
	}

	public UnitDisplay notSimple() {
		return (!isSimple ? this : new UnitDisplay(unit, value, false, decimalPlaces, useSymbol, removeTrailingZeroes));
	}

	public UnitDisplay symbol() {
		return symbol(true);
	}

	public UnitDisplay symbol(boolean useSymbol) {
		return (this.useSymbol ^ useSymbol ? new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol, removeTrailingZeroes) : this);
	}

	public UnitDisplay decimal(int decimalPlaces) {
		return (this.decimalPlaces == decimalPlaces ? this : new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol, removeTrailingZeroes));
	}

	public UnitDisplay keepTrailingZeroes() {
		return (this.removeTrailingZeroes ? new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol, false) : this);
	}

	public UnitDisplay removeTrailingZeroes() {
		return (this.removeTrailingZeroes ? this : new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol, false));
	}

	@Override
	public String toString() {
		final String unitName;
		double value = this.value.getAsDouble();
		String prefix = "";

		if (useSymbol)
			unitName = unit.getSymbol();
		else if (value > 1)
			unitName = unit.getPluralName();
		else
			unitName = unit.getLocalizedName();

		if (isSimple) {
			if (value > 1) {
				if (decimalPlaces < 1) {
					return MathUtil.toString(value, removeTrailingZeroes) + " " + (useSymbol ? unit.getSymbol() : unit.getPluralName());
				}

				return MathUtil.toString(MathUtil.roundDecimals(value, decimalPlaces), removeTrailingZeroes) + " " + (useSymbol ? unit.getSymbol() : unit.getPluralName());
			}

			if (decimalPlaces < 1) {
				return MathUtil.toString(value, removeTrailingZeroes) + " " + (useSymbol ? unit.getSymbol() : unit.getLocalizedName());
			}

			return MathUtil.toString(MathUtil.roundDecimals(value, decimalPlaces), removeTrailingZeroes) + " " + (useSymbol ? unit.getSymbol() : unit.getLocalizedName());
		}

		if (value < 0) {
			value = Math.abs(value);
			prefix = "-";
		}

		if (value == 0) {
			return MathUtil.toString(value, removeTrailingZeroes) + " " + unitName;
		} else {
			for (int i = 0; i < UnitPrefix.getPrefixes().size(); i++) {
				UnitPrefix lowerMeasure = UnitPrefix.getPrefixes().get(i);

				if (lowerMeasure.isBellow(value) && i == 0) {
					return prefix + MathUtil.toString(MathUtil.roundDecimals(lowerMeasure.process(value), decimalPlaces), removeTrailingZeroes) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
				if (i + 1 >= UnitPrefix.getPrefixes().size()) {
					return prefix + MathUtil.toString(MathUtil.roundDecimals(lowerMeasure.process(value), decimalPlaces), removeTrailingZeroes) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}

				UnitPrefix upperMeasure = UnitPrefix.getPrefixes().get(i + 1);

				if ((lowerMeasure.isAbove(value) && upperMeasure.isBellow(value)) || lowerMeasure.value == value) {
					return prefix + MathUtil.toString(MathUtil.roundDecimals(lowerMeasure.process(value), decimalPlaces), removeTrailingZeroes) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
			}
		}

		return prefix + MathUtil.toString(MathUtil.roundDecimals(value, decimalPlaces), removeTrailingZeroes) + " " + unitName;
	}
}
