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

package nova.core.util.unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Convert between different units. (Where supported)
 *
 * @author ExE Boss
 */
public class UnitConversion {

	private static final Map<Unit, Map<Unit, UnitConversion>> CONVERSION = new HashMap<>();

	private final Unit unit1, unit2;
	private final double ratio, reverseRatio;
	private final UnitConversion reverse;

	private UnitConversion(Unit unit1, Unit unit2, double ratio) {
		this.unit1 = unit1;
		this.unit2 = unit2;
		this.ratio = ratio;
		this.reverseRatio = 1/ratio;
		this.reverse = new UnitConversion(unit1, unit2, this.reverseRatio, this);
	}
	private UnitConversion(Unit unit1, Unit unit2, double ratio, UnitConversion reverse) {
		this.unit1 = unit1;
		this.unit2 = unit2;
		this.ratio = ratio;
		this.reverse = reverse;
		this.reverseRatio = this.reverse.ratio;
	}
	public double convert(double value) {
		return (this.reverseRatio == 0 && this.ratio != 0 ? value / ratio : value * this.reverseRatio);
	}

	public Unit unit1() {
		return unit1;
	}
	public Unit unit2() {
		return unit2;
	}
	public double ratio() {
		return ratio;
	}
	public UnitConversion reverse() {
		return reverse;
	}

	public static Optional<UnitConversion> getConvertion(Unit unit1, Unit unit2) {
		Map<Unit, UnitConversion> conv = CONVERSION.get(unit1);
		if (conv == null) return Optional.empty();
		return Optional.ofNullable(conv.get(unit2));
	}

	/**
	 *
	 * @param unit1 The unit to convert from
	 * @param unit2 The unit to convert to
	 * @param ratio unit1/unit2
	 * @return The UnitConversion instance.
	 * @throws IllegalArgumentException If ratio is 0
	 */
	public static UnitConversion registerConversion(Unit unit1, Unit unit2, double ratio) throws IllegalArgumentException {
		if (ratio == 0)
			throw new IllegalArgumentException("Ratio cannot be 0");

		Map<Unit, UnitConversion> conv1 = CONVERSION.get(unit1);
		if (conv1 == null) {
			conv1 = new HashMap<>();
			CONVERSION.put(unit1, conv1);
		}
		if (conv1.containsKey(unit2)) return conv1.get(unit2);

		Map<Unit, UnitConversion> conv2 = CONVERSION.get(unit2);
		if (conv2 == null) {
			conv2 = new HashMap<>();
			CONVERSION.put(unit2, conv2);
		}

		UnitConversion uc = new UnitConversion(unit1, unit2, ratio);

		conv1.put(unit2, uc);
		conv2.put(unit1, uc.reverse());

		calculateConversions();
		return uc;
	}

	/**
	 * If UnitA can be converted to UnitB
	 * and UnitB can be converted to UnitC,
	 * then UnitA must be convertible to UnitC.
	 */
	public static void calculateConversions() {
		// TODO
	}
}
