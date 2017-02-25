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

import nova.core.util.Identifiable;
import nova.core.util.registry.Registry;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ExE Boss
 */
public final class Unit implements Identifiable {
	private static final Registry<Unit> REGISTRY = new Registry<>();

	public static final Unit METRE = getOrCreateUnit("nova:metre", "Meter", "m");
	public static final Unit GRAM = getOrCreateUnit("nova:gram", "Gram", "g", UnitPrefix.KILO);
	// TODO: Define More Units

	public static final Unit AMPERE = getOrCreateUnit("nova:ampere", "Amp", "I");
	public static final Unit AMP_HOUR = getOrCreateUnit("nova:amp_hour", "Amp Hour", "Ah");
	public static final Unit VOLTAGE = getOrCreateUnit("nova:voltage", "Volt", "V");
	public static final Unit WATT = getOrCreateUnit("nova:watt", "Watt", "W");
	public static final Unit WATT_HOUR = getOrCreateUnit("nova:watt_hour", "Watt Hour", "Wh");
	public static final Unit RESISTANCE = getOrCreateUnit("nova:resistance", "Ohm", "R");
	public static final Unit CONDUCTANCE = getOrCreateUnit("nova:conductance", "Siemen", "S");
	public static final Unit JOULE = getOrCreateUnit("nova:joule", "Joule", "J");
	public static final Unit LITER = getOrCreateUnit("nova:liter", "Liter", "L");
	public static final Unit NEWTON_METER = getOrCreateUnit("nova:newton_meter", "Newton Meter", "Nm");

	private final String id;
	public final String name;
	public final String symbol;
	private String plural = null;
	private UnitPrefix basePrefix = null;

	private Unit(String id, String name, String symbol) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
	}

	private Unit setPlural(String plural) {
		if (this.plural == null)
			this.plural = plural;
		return this;
	}

	private Unit setBasePrefix(UnitPrefix basePrefix) {
		if (this.basePrefix == null)
			this.basePrefix = basePrefix;
		return this;
	}

	public String getPlural() {
		return this.plural == null ? this.name + "s" : this.plural;
	}

	@Override
	public String getID() {
		return id;
	}

	// TODO: Move to Registry
	public Set<Unit> getUnitsFromMod(String modId) {
		return REGISTRY.stream().filter((e) -> {
			String id = e.getID();
			if (id.contains(":")) {
				return id.substring(0, id.lastIndexOf(':')).equals(modId);
			} else {
				return modId == null || modId.isEmpty();
			}
		}).collect(Collectors.toSet());
	}

	public Optional<Unit> getUnit(String id) {
		return REGISTRY.get(id);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit));
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, String plural) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setPlural(plural);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, UnitPrefix basePrefix) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setBasePrefix(basePrefix);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, String plural, UnitPrefix basePrefix) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setPlural(plural).setBasePrefix(basePrefix);
	}
}
