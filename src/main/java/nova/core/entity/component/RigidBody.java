package nova.core.entity.component;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.component.Updater;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A rigid body component for entity physics.
 * @author Calclavia
 */
//TODO: Don't only use Scala method convention names
public abstract class RigidBody extends Component implements Updater {
	public final ComponentProvider provider;

	private double mass = 1;

	/**
	 * Translation
	 */
	private double drag = 0;
	private Vector3D velocity = Vector3D.ZERO;
	private Vector3D gravity = new Vector3D(0, -9.81, 0);

	/**
	 * Rotation
	 */
	private double angularDrag = 0;
	private Rotation angularVelocity = Rotation.IDENTITY;

	public RigidBody(ComponentProvider provider) {
		this.provider = provider;
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

	/**
	 * Velocity is how fast the body is moving
	 */
	public Vector3D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3D velocity) {
		this.velocity = velocity;
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
