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
 */package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * An interface applied to objects that can act as vector transformers.
 * @author Calclavia
 */
@FunctionalInterface
public interface Transformer {
	/**
	 * Called to transform a vector.
	 * @param vec - The vector being transformed
	 * @return The transformed vector.
	 */
	Vector3D apply(Vector3D vec);
}
