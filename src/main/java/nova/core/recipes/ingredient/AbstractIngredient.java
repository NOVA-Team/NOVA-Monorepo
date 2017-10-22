/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */
package nova.core.recipes.ingredient;

import java.util.Optional;

/**
 * @author ExE Boss
 */
public abstract class AbstractIngredient implements ItemIngredient {

	private Optional<String> tag = Optional.empty();

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return getExampleItems().stream().allMatch(ingredient::matches);
	}

	@Override
	public Optional<String> getTag() {
		return tag;
	}

	@Override
	public ItemIngredient withTag(String tag) {
		this.tag = Optional.of(tag);
		return this;
	}
}
