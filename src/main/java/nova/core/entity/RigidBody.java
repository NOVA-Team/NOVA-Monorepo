package nova.core.entity;

import nova.core.util.transform.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * A simple rigid body class for entity physics.
 *
 * @author Calclavia
 */
public interface RigidBody {
	/**
	 * Mass in kilograms. Default is 1 kg.
	 */
	double mass();

	void setMass(double mass);

	/**
	 * Velocity is how fast the body is moving
	 */
	Vector3d velocity();

	void setVelocity(Vector3d velocity);

	double drag();

	void setDrag(double drag);

	/**
	 * Gravity is an acceleration.
	 */
	Vector3d gravity();

	void setGravity(Vector3d gravity);

	/**
	 * Rotation Methods
	 */
	double angularDrag();

	void setAngularDrag(double angularDrag);

	Quaternion angularVelocity();

	void setAngularVelocity(Quaternion angularVelocity);

	Vector3d center();

	void setCenter(Vector3d center);

	/**
	 * Force methods
	 */
	void addForce(Vector3d force);

	void addForce(Vector3d force, Vector3d position);

	void addTorque(Vector3d torque);
}
