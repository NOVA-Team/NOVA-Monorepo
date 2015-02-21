package nova.core.entity;

import nova.core.util.components.Updater;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;

/**
 * A simple rigid body class for entity physics.
 *
 * @author Calclavia
 */
public class RigidBody implements Updater {

	private final Entity entity;
	/**
	 * Mass in kilograms
	 */
	public double mass = 1;
	/**
	 * Translation Methods
	 */
	public Vector3d velocity = Vector3d.zero;
	public double drag = 0;
	/**
	 * Gravity is an acceleration.
	 */
	public Vector3d gravity = new Vector3d(0, 9.81, 0);
	/**
	 * Rotation Methods
	 */
	public double angularDrag = 0;
	public Quaternion angularVelocity = Quaternion.identity;
	public Vector3d centerOfMass = Vector3d.zero;
	private Vector3d acceleration = Vector3d.zero;

	public RigidBody(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void update(double deltaTime) {
		//Translation
		velocity.add(acceleration.multiply(deltaTime));
		entity.setPosition(entity.position().add(velocity.multiply(deltaTime)));
		velocity = velocity.subtract(velocity.multiply(drag));

		//Rotation
		//TODO: angularVel should multiply by time.
		entity.setRotation(entity.rotation().rightMultiply(angularVelocity));
		//TODO: Simplify this calculation?
		Vector3d euler = angularVelocity.toEuler();
		angularVelocity = Quaternion.fromEuler(euler.subtract(euler.multiply(angularDrag)));
	}

	public void addForce(Vector3d force) {
		acceleration = acceleration.add(force.divide(mass));
	}

	public void addForce(Vector3d force, Vector3d position) {

	}

	public void addTorque(Vector3d torque) {

	}
}
