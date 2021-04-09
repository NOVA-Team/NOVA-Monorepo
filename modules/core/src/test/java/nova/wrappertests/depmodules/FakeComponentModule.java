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

package nova.wrappertests.depmodules;

import nova.core.entity.component.RigidBody;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeComponentModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RigidBody.class).to(FakeRigidBody.class);
	}

	public static class FakeRigidBody extends RigidBody {
		@Override
		public double getMass() {
			return 0;
		}

		@Override
		public void setMass(double mass) {

		}

		@Override
		public Vector3D getVelocity() {
			return null;
		}

		@Override
		public void setVelocity(Vector3D velocity) {

		}

		@Override
		public double getDrag() {
			return 0;
		}

		@Override
		public void setDrag(double drag) {

		}

		@Override
		public Vector3D getGravity() {
			return null;
		}

		@Override
		public void setGravity(Vector3D gravity) {

		}

		@Override
		public double getAngularDrag() {
			return 0;
		}

		@Override
		public void setAngularDrag(double angularDrag) {

		}

		@Override
		public Rotation getAngularVelocity() {
			return null;
		}

		@Override
		public void setAngularVelocity(Rotation angularVelocity) {

		}

		@Override
		public void addForce(Vector3D force) {

		}

		@Override
		public void addForce(Vector3D force, Vector3D position) {

		}

		@Override
		public void addTorque(Vector3D torque) {

		}
	}

}
