package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.util.Identifiable;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.Positioned;

public abstract class Entity extends Positioned<EntityWrapper, Vector3d> implements Identifiable, Stateful, EntityWrapper {

	@Override
	public double mass() {
		return wrapper.mass();
	}

	@Override
	public void setMass(double mass) {
		wrapper.setMass(mass);
	}

	@Override
	public Vector3d velocity() {
		return wrapper.velocity();
	}

	@Override
	public void setVelocity(Vector3d velocity) {
		wrapper.setVelocity(velocity);
	}

	@Override
	public double drag() {
		return wrapper.drag();
	}

	@Override
	public void setDrag(double drag) {
		wrapper.setDrag(drag);
	}

	@Override
	public Vector3d gravity() {
		return wrapper.gravity();
	}

	@Override
	public void setGravity(Vector3d gravity) {
		wrapper.setGravity(gravity);
	}

	@Override
	public double angularDrag() {
		return wrapper.angularDrag();
	}

	@Override
	public void setAngularDrag(double angularDrag) {
		wrapper.setAngularDrag(angularDrag);
	}

	@Override
	public Quaternion angularVelocity() {
		return wrapper.angularVelocity();
	}

	@Override

	public void setAngularVelocity(Quaternion angularVelocity) {
		wrapper.setAngularVelocity(angularVelocity);
	}

	@Override
	public Vector3d center() {
		return wrapper.center();
	}

	@Override
	public void setCenter(Vector3d center) {
		wrapper.setCenter(center);
	}

	@Override
	public void addForce(Vector3d force) {
		wrapper.addForce(force);
	}

	@Override
	public void addForce(Vector3d force, Vector3d position) {
		wrapper.addForce(force, position);
	}

	@Override
	public void addTorque(Vector3d torque) {
		wrapper.addTorque(torque);
	}

	@Override
	public Quaternion rotation() {
		return wrapper.rotation();
	}

	/**
	 * Sets the rotation of this entity.
	 * @param rotation Position to set.
	 */
	public void setRotation(Quaternion rotation) {
		wrapper.setRotation(rotation);
	}

}
