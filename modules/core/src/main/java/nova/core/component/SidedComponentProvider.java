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

package nova.core.component;

import nova.core.util.Direction;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ExE Boss
 */
@SuppressWarnings("rawtypes")
public abstract class SidedComponentProvider extends ComponentProvider<SidedComponentMap> {

	@SuppressWarnings("unchecked")
	public SidedComponentProvider() {
		super(SidedComponentMap.class);
	}

	public final Collection<Component> unsidedComponents() {
		return components();
	}

	public final Map<Direction, Collection<Component>> sidedComponents() {
		return Arrays.stream(Direction.values()).collect(Collectors.toMap(Function.identity(), d -> new HashSet<>(components.getComponents(d).values())));
	}
}
