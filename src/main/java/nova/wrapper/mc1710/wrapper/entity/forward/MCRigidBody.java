package nova.wrapper.mc1710.wrapper.entity.forward;

import nova.core.component.ComponentProvider;
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
public class MCRigidBody extends RigidBody {
	private Entity entity;

	/**
	 * Translation
	 */
	private Vector3D netForce = Vector3D.ZERO;

	/**
	 * Rotation
	 */
	private Vector3D netTorque = Vector3D.ZERO;

	public MCRigidBody(ComponentProvider provider) {
		super(provider);
		//TODO: This nullable provider is horrible. Change this.
		if (provider != null) {
			entity = (nova.core.entity.Entity) provider;
		}
	}

	private net.minecraft.entity.Entity mcEntity() {
		return entity.get(MCEntityTransform.class).wrapper;
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
		//Apply gravity
		addForce(gravity().scalarMultiply(mass()));
	}

	void updateRotation(double deltaTime) {

		//Integrate angular velocity to angular displacement
		Rotation angularVel = angularVelocity();
		Rotation deltaRotation = RotationUtil.slerp(Rotation.IDENTITY, angularVel, deltaTime);
		entity.transform().setRotation(entity.rotation().applyTo(deltaRotation));

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
}
