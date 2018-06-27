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

package nova.core.wrapper.mc.forge.v1_8.wrapper.entity.forward;

import nova.core.entity.Entity;
import nova.core.entity.component.RigidBody;
import nova.core.util.math.RotationUtil;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Based on the Euler Integration because Minecraft stores the following values:
 *
 * Position
 * Velocity
 * @author Calclavia
 */
public class BWRigidBody extends RigidBody {
	private double mass = 1;

	/**
	 * Translation
	 */
	private double drag = 0;

	private Vector3D gravity = new Vector3D(0, -9.81, 0);

	/**
	 * Rotation
	 */
	private double angularDrag = 0;
	private Rotation angularVelocity = Rotation.IDENTITY;

	/**
	 * Translation
	 */
	private Vector3D netForce = Vector3D.ZERO;

	/**
	 * Rotation
	 */
	private Vector3D netTorque = Vector3D.ZERO;

	private net.minecraft.entity.Entity mcEntity() {
		return getProvider().components.get(MCEntityTransform.class).wrapper;
	}

	private Entity entity() {
		return (Entity) getProvider();
	}

	@Override
	public void update(double deltaTime) {
		updateTranslation(deltaTime);
		updateRotation(deltaTime);
	}

	void updateTranslation(double deltaTime) {
		//Integrate velocity to displacement
		Vector3D displacement = velocity().scalarMultiply(deltaTime);
		mcEntity().moveEntity(displacement.getX(), displacement.getY(), displacement.getZ());

		//Integrate netForce to velocity
		setVelocity(velocity().add(netForce.scalarMultiply(1 / mass()).scalarMultiply(deltaTime)));

		//Clear net force
		netForce = Vector3D.ZERO;

		//Apply drag
		addForce(velocity().negate().scalarMultiply(drag()));
		if (!mcEntity().onGround) {
			//Apply gravity
			addForce(gravity().scalarMultiply(mass()));
		}
	}

	void updateRotation(double deltaTime) {

		//Integrate angular velocity to angular displacement
		Rotation angularVel = angularVelocity();
		Rotation deltaRotation = RotationUtil.slerp(Rotation.IDENTITY, angularVel, deltaTime);
		entity().transform().setRotation(entity().rotation().applyTo(deltaRotation));

		//Integrate torque to angular velocity
		Vector3D torque = netTorque.scalarMultiply(deltaTime);
		if (!Vector3D.ZERO.equals(torque)) {
			setAngularVelocity(angularVelocity().applyTo(new Rotation(Vector3DUtil.FORWARD, torque)));
		}

		//Clear net torque
		netTorque = Vector3D.ZERO;

		//Apply drag
		Vector3D eulerAngularVel = angularVelocity().applyInverseTo(Vector3DUtil.FORWARD);
		addTorque(eulerAngularVel.negate().scalarMultiply(angularDrag()));
	}

	@Override
	public Vector3D getVelocity() {
		return new Vector3D(mcEntity().motionX, mcEntity().motionY, mcEntity().motionZ);
	}

	@Override
	public void setVelocity(Vector3D velocity) {
		mcEntity().motionX = velocity.getX();
		mcEntity().motionY = velocity.getY();
		mcEntity().motionZ = velocity.getZ();
	}

	@Override
	public void addForce(Vector3D force, Vector3D position) {
		//TODO: implement
	}

	@Override
	public void addTorque(Vector3D torque) {
		//TODO: implement
	}

	@Override
	public void addForce(Vector3D force) {
		netForce = netForce.add(force.scalarMultiply(1 / mass()));
	}

	/**
	 * Mass in kilograms. Default is 1 kg.
	 */
	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}

	/**
	 * Gravity is an acceleration.
	 */
	public Vector3D getGravity() {
		return gravity;
	}

	public void setGravity(Vector3D gravity) {
		this.gravity = gravity;
	}

	/**
	 * Rotation Methods
	 */
	public double getAngularDrag() {
		return angularDrag;
	}

	public void setAngularDrag(double angularDrag) {
		this.angularDrag = angularDrag;
	}

	public Rotation getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(Rotation angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

}
