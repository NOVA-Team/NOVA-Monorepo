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
	 *
	 * @return The mass of this rigid body.
	 */
	public abstract double getMass();

	public abstract void setMass(double mass);

	/**
	 * Velocity is how fast the body is moving
	 *
	 * @return The velocity vector of this rigid body.
	 */
	public abstract Vector3D getVelocity();

	public abstract void setVelocity(Vector3D velocity);

	public abstract double getDrag();

	public abstract void setDrag(double drag);

	/**
	 * Gravity is an acceleration.
	 *
	 * @return The gravity vector of this rigid body.
	 */
	public abstract Vector3D getGravity();

	public abstract void setGravity(Vector3D gravity);

	/**
	 * Rotation Methods
	 *
	 * @return The angular drag of this rigid body.
	 */
	public abstract double getAngularDrag();

	public abstract void setAngularDrag(double angularDrag);

	public abstract Rotation getAngularVelocity();

	public abstract void setAngularVelocity(Rotation angularVelocity);

	/**
	 * Forces
	 *
	 * @param force The force to apply.
	 */
	public abstract void addForce(Vector3D force);

	public abstract void addForce(Vector3D force, Vector3D position);

	public abstract void addTorque(Vector3D torque);

	/**
	 * Alias for {@link #getMass()}.
	 *
	 * @return The mass of this rigid body.
	 * @see #getMass()
	 */
	public final double mass() {
		return getMass();
	}

	/**
	 * Alias for {@link #getVelocity()}.
	 *
	 * @return The velocity vector of this rigid body.
	 * @see #getVelocity()
	 */
	public final Vector3D velocity() {
		return getVelocity();
	}

	/**
	 * Alias for {@link #getDrag()}.
	 *
	 * @return The drag of this rigid body.
	 * @see #getDrag()
	 */
	public final double drag() {
		return getDrag();
	}

	/**
	 * Alias for {@link #getGravity()}.
	 *
	 * @return The gravity vector of this rigid body.
	 * @see #getGravity()
	 */
	public final Vector3D gravity() {
		return getGravity();
	}

	/**
	 * Alias for {@link #getAngularDrag()}.
	 *
	 * @return The angular drag of this rigid body.
	 * @see #getAngularDrag()
	 */
	public final double angularDrag() {
		return getAngularDrag();
	}

	/**
	 * Alias for {@link #getAngularVelocity()}.
	 *
	 * @return The angular velocity of this rigid body.
	 * @see #getAngularVelocity()
	 */
	public final Rotation angularVelocity() {
		return getAngularVelocity();
	}
}
