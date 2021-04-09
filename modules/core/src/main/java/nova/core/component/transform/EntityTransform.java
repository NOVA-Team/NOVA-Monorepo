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

package nova.core.component.transform;

import nova.core.component.UnsidedComponent;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A 3D transform.
 *
 * Note that the implemented Transform3d for entities require a constructor with type ComponentProvider.
 * @author Calclavia
 */
@UnsidedComponent
public class EntityTransform extends WorldTransform<Vector3D> {

	//The rotation of the transform. Can never be null.
	private Rotation rotation;

	//The scale of the transform. Can never be null.
	private Vector3D scale;

	//The center of rotation.
	private Vector3D pivot;

	public Vector3D scale() {
		return scale;
	}

	public Vector3D pivot() {
		return pivot;
	}

	public void setPivot(Vector3D pivot) {
		this.pivot = pivot;
	}

	public void setScale(Vector3D scale) {
		this.scale = scale;
	}

	public Rotation rotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
}
