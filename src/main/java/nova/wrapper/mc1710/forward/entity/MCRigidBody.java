package nova.wrapper.mc1710.forward.entity;

import nova.core.entity.RigidBody;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;

/**
 * Based on the Euler Integration because Minecraft stores the following values:
 *
 * Position
 * Velocity
 *
 * @author Calclavia
 */
public class MCRigidBody implements RigidBody {
	private final net.minecraft.entity.Entity entity;

	private double mass = 1;

	/**
	 * Translation
	 */
	private double drag = 0;

	private Vector3d gravity = new Vector3d(0, -9.81, 0);

	private Vector3d netForce = Vector3d.zero;

	/**
	 * Rotation
	 */
	private double angularDrag = 0;

	private Quaternion angularVelocity = Quaternion.identity;

	private Vector3d netTorque = Vector3d.zero;

	private float inertia = 1;

	private Vector3d center = Vector3d.zero;

	public MCRigidBody(net.minecraft.entity.Entity entity) {
		this.entity = entity;
	}

	public void update(double deltaTime) {
		updateTranslation(deltaTime);
		updateRotation(deltaTime);
	}

	void updateTranslation(double deltaTime) {
		//Integrate velocity to displacement
		Vector3d displacement = velocity().multiply(deltaTime);
		entity.moveEntity(displacement.x, displacement.y, displacement.z);

		//Integrate netForce to velocity
		setVelocity(velocity().add(netForce.divide(mass).multiply(deltaTime)));

		//Clear net force
		netForce = Vector3d.zero;

		//Apply drag
		addForce(velocity().inverse().multiply(drag));
		//Apply gravity
		addForce(gravity.multiply(mass));
	}

	void updateRotation(double deltaTime) {

		//Integrate angular velocity to angular displacement
		Quaternion angularVel = angularVelocity();
		Quaternion deltaRotation = angularVel.scale(deltaTime);
		setRotation(rotation().rightMultiply(deltaRotation));

		//Integrate torque to angular velocity
		setAngularVelocity(angularVelocity().rightMultiply(Quaternion.fromEuler(netTorque.multiply(deltaTime))));

		//Clear net torque
		netTorque = Vector3d.zero;

		//Apply drag
		Vector3d eulerAngularVel = angularVelocity.toEuler();
		addTorque(eulerAngularVel.inverse().multiply(angularDrag));
	}

	@Override
	public double mass() {
		return mass;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	public Vector3d position() {
		return new Vector3d(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public Vector3d velocity() {
		return new Vector3d(entity.motionX, entity.motionY, entity.motionZ);
	}

	@Override
	public void setVelocity(Vector3d velocity) {
		entity.motionX = velocity.x;
		entity.motionY = velocity.y;
		entity.motionZ = velocity.z;
	}

	@Override
	public double drag() {
		return drag;
	}

	@Override
	public void setDrag(double drag) {
		this.drag = drag;
	}

	@Override
	public Vector3d gravity() {
		return gravity;
	}

	@Override
	public void setGravity(Vector3d gravity) {
		this.gravity = gravity;
	}

	@Override
	public double angularDrag() {
		return angularDrag;
	}

	@Override
	public void setAngularDrag(double angularDrag) {
		this.angularDrag = angularDrag;
	}

	public Quaternion rotation() {
		return Quaternion.fromEuler(entity.rotationYaw, entity.rotationPitch, 0);
	}

	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		entity.rotationYaw = euler.xf();
		entity.rotationPitch = euler.yf();
	}

	@Override
	public Quaternion angularVelocity() {
		return angularVelocity;
	}

	@Override
	public void setAngularVelocity(Quaternion angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	@Override
	public Vector3d center() {
		return center;
	}

	@Override
	public void setCenter(Vector3d center) {
		this.center = center;
	}

	@Override
	public void addForce(Vector3d force, Vector3d position) {
		//TODO: implement
	}

	@Override
	public void addTorque(Vector3d torque) {
		//TODO: implement
	}

	@Override
	public void addForce(Vector3d force) {
		netForce = netForce.add(force.divide(mass));
	}
}
