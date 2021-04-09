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

package nova.core.util;

import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class Ray {
	public final Vector3D origin;
	public final Vector3D dir;
	public final Vector3D invDir;

	public final boolean ignoreX;
	public final boolean ignoreY;
	public final boolean ignoreZ;

	public final boolean signDirX;
	public final boolean signDirY;
	public final boolean signDirZ;

	/**
	 * @param origin The ray's beginning
	 * @param dir The ray's direction (unit vector)
	 */
	public Ray(Vector3D origin, Vector3D dir) {
		this.origin = origin;
		this.dir = dir;
		this.invDir = Vector3DUtil.reciprocal(dir);
		this.signDirX = invDir.getX() < 0;
		this.signDirY = invDir.getY() < 0;
		this.signDirZ = invDir.getZ() < 0;

		this.ignoreX = Math.abs(dir.getX()) < 0.0000001;
		this.ignoreY = Math.abs(dir.getY()) < 0.0000001;
		this.ignoreZ = Math.abs(dir.getZ()) < 0.0000001;
	}

	public static Ray fromInterval(Vector3D start, Vector3D end) {
		return new Ray(start, end.subtract(start).normalize());
	}
}
