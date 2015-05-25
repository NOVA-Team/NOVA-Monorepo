package nova.wrapper.mc1710.forward.entity;

import nova.core.component.ComponentProvider;
import nova.core.entity.Entity;
import nova.core.entity.component.RigidBody;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * Based on the Euler Integration because Minecraft stores the following values:
 *
 * Position
 * Velocity
 * @author Calclavia
 */
public class MCRigidBody extends RigidBody {
	private Entity entity;
	private net.minecraft.entity.Entity mcEntity;

	/**
	 * Translation
	 */
	private Vector3d netForce = Vector3d.zero;

	/**
	 * Rotation
	 */
	private Vector3d netTorque = Vector3d.zero;

	public MCRigidBody(ComponentProvider provider) {
		super(provider);
		//TODO: This nullable provider is horrible. Change this.
		if (provider != null) {
			entity = (nova.core.entity.Entity) provider;
			mcEntity = entity.get(MCEntityWrapper.class).get().wrapper;
		}
	}

	@Override
	public void update(double deltaTime) {
		updateTranslation(deltaTime);
		updateRotation(deltaTime);
	}

	void updateTranslation(double deltaTime) {
		//Integrate velocity to displacement
		Vector3d displacement = velocity().multiply(deltaTime);
		mcEntity.moveEntity(displacement.x, displacement.y, displacement.z);

		//Integrate netForce to velocity
		setVelocity(velocity().add(netForce.divide(mass()).multiply(deltaTime)));

		//Clear net force
		netForce = Vector3d.zero;

		//Apply drag
		addForce(velocity().negate().multiply(drag()));
		//Apply gravity
		addForce(gravity().multiply(mass()));
	}

	void updateRotation(double deltaTime) {

		//Integrate angular velocity to angular displacement
		Quaternion angularVel = angularVelocity();
		Quaternion deltaRotation = angularVel.scale(deltaTime);
		entity.transform().setRotation(entity.rotation().rightMultiply(deltaRotation));

		//Integrate torque to angular velocity
		setAngularVelocity(angularVelocity().rightMultiply(Quaternion.fromEuler(netTorque.multiply(deltaTime))));

		//Clear net torque
		netTorque = Vector3d.zero;

		//Apply drag
		Vector3d eulerAngularVel = angularVelocity().toEuler();
		addTorque(eulerAngularVel.negate().multiply(angularDrag()));
	}

	@Override
	public Vector3d getVelocity() {
		return new Vector3d(mcEntity.motionX, mcEntity.motionY, mcEntity.motionZ);
	}

	@Override
	public void setVelocity(Vector3d velocity) {
		mcEntity.motionX = velocity.x;
		mcEntity.motionY = velocity.y;
		mcEntity.motionZ = velocity.z;
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
		netForce = netForce.add(force.divide(mass()));
	}
}
