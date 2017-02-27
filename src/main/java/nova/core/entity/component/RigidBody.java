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

package nova.core.entity.component;

import nova.core.component.Component;
import nova.core.component.UnsidedComponent;
import nova.core.component.Updater;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A rigid body component for entity physics.
 * @author Calclavia
 */
@UnsidedComponent
public abstract class RigidBody extends Component implements Updater {
	/**
	 * Mass in kilograms. Default is 1 kg.
	 */
	public abstract double getMass();

	public abstract void setMass(double mass);

	/**
	 * Velocity is how fast the body is moving
	 */
	public abstract Vector3D getVelocity();

	public abstract void setVelocity(Vector3D velocity);

	public abstract double getDrag();

	public abstract void setDrag(double drag);

	/**
	 * Gravity is an acceleration.
	 */
	public abstract Vector3D getGravity();

	public abstract void setGravity(Vector3D gravity);

	/**
	 * Rotation Methods
	 */
	public abstract double getAngularDrag();

	public abstract void setAngularDrag(double angularDrag);

	public abstract Rotation getAngularVelocity();

	public abstract void setAngularVelocity(Rotation angularVelocity);

	/**
	 * Forces
	 */
	public abstract void addForce(Vector3D force);

	public abstract void addForce(Vector3D force, Vector3D position);

	public abstract void addTorque(Vector3D torque);

	/**
	 * Scala sugar coating
	 */
	public final double mass() {
		return getMass();
	}

	public final Vector3D velocity() {
		return getVelocity();
	}

	public final double drag() {
		return getDrag();
	}

	public final Vector3D gravity() {
		return getGravity();
	}

	public final double angularDrag() {
		return getAngularDrag();
	}

	public final Rotation angularVelocity() {
		return getAngularVelocity();
	}
}
