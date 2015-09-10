package nova.core.entity.component;

import nova.core.component.Component;
import nova.core.component.Updater;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A rigid body component for entity physics.
 * @author Calclavia
 */
//TODO: Don't only use Scala method convention names
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
