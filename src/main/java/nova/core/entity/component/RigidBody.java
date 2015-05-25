package nova.core.entity.component;

import nova.core.component.Component;
import nova.core.component.ComponentProvider;
import nova.core.component.Updater;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

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
	private Vector3d velocity = Vector3d.zero;
	private Vector3d gravity = new Vector3d(0, -9.81, 0);

	/**
	 * Rotation
	 */
	private double angularDrag = 0;
	private Quaternion angularVelocity = Quaternion.identity;

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
	public Vector3d getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3d velocity) {
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
	public Vector3d getGravity() {
		return gravity;
	}

	public void setGravity(Vector3d gravity) {
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

	public Quaternion getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(Quaternion angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	/**
	 * Forces
	 */
	public abstract void addForce(Vector3d force);

	public abstract void addForce(Vector3d force, Vector3d position);

	public abstract void addTorque(Vector3d torque);

	/**
	 * Scala sugar coating
	 */
	public final double mass() {
		return getMass();
	}

	public final Vector3d velocity() {
		return getVelocity();
	}

	public final double drag() {
		return getDrag();
	}

	public final Vector3d gravity() {
		return getGravity();
	}

	public final double angularDrag() {
		return getAngularDrag();
	}

	public final Quaternion angularVelocity() {
		return getAngularVelocity();
	}

	@Override
	public final String getID() {
		return null;
	}
}
