package nova.wrapper.mc1710.forward.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.entity.EntityWrapper;
import nova.core.entity.RigidBody;
import nova.core.util.components.Storable;
import nova.core.util.components.Updater;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.wrapper.mc1710.backward.world.BWWorld;
import nova.wrapper.mc1710.util.NBTUtility;

import java.util.HashMap;

/**
 * Entity wrapper
 * @author Calclavia
 */
public class BWEntity extends net.minecraft.entity.Entity implements EntityWrapper, RigidBody {

	public final Entity wrapped;

	private double mass = 1;

	private double drag = 0;

	private Vector3d gravity = new Vector3d(0, 9.81, 0);

	private double angularDrag = 0;

	private Quaternion angularVelocity = Quaternion.identity;

	private Quaternion angularAcceleration = Quaternion.identity;

	private Vector3d center = Vector3d.zero;

	private Vector3d acceleration = Vector3d.zero;

	public BWEntity(World world, EntityFactory factory) {
		super(world);
		this.wrapped = factory.makeEntity(this, this);
	}

	@Override
	protected void entityInit() {
		wrapped.awake();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		double deltaTime = 0.05;

		if (wrapped instanceof Updater) {
			((Updater) wrapped).update(deltaTime);
		}

		/**
		 * Calculate translational physics
		 */
		//Calculate velocity by applying acceleration to it.
		setVelocity(velocity().add(acceleration.multiply(deltaTime)));
		acceleration = Vector3d.zero;

		//Apply velocity to displacement
		moveEntity(motionX * deltaTime, motionY * deltaTime, motionZ * deltaTime);
		//Apply drag
		setVelocity(velocity().subtract(velocity().multiply(drag)));

		/**
		 * Calculate rotational physics
		 */
		//TODO: We should scale rotation by time.
		//Calculate angular velocity by applying angular acceleration.
		setAngularVelocity(angularVelocity().rightMultiply(angularAcceleration));
		angularAcceleration = Quaternion.identity;

		//Apply angular velocity to angular displacement
		setRotation(rotation().rightMultiply(angularVelocity));

		//Apply drag
		Vector3d eulerAngularVel = angularVelocity.toEuler();
		setAngularVelocity(Quaternion.fromEuler(eulerAngularVel.subtract(eulerAngularVel.multiply(angularDrag))));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (wrapped instanceof Storable) {
			((Storable) wrapped).load(NBTUtility.nbtToMap(nbt));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

		if (wrapped instanceof Storable) {
			HashMap<String, Object> data = new HashMap<>();
			((Storable) wrapped).save(data);
			NBTUtility.mapToNBT(nbt, data);
		}
	}

	/**
	 * Entity Wrapper Methods
	 *
	 * @return
	 */
	@Override
	public boolean isValid() {
		return !isDead;
	}

	@Override
	public nova.core.world.World world() {
		return new BWWorld(worldObj);
	}

	@Override
	public Vector3d position() {
		return new Vector3d(posX, posY, posZ);
	}

	@Override
	public Quaternion rotation() {
		return Quaternion.fromEuler(Math.toRadians(rotationYaw), Math.toRadians(rotationPitch), 0);
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		//TODO: Change entity's world
	}

	@Override
	public void setPosition(Vector3d position) {
		setPosition(position.x, position.y, position.z);
	}

	@Override
	public void setRotation(Quaternion rotation) {
		Vector3d euler = rotation.toEuler();
		setRotation((float) Math.toDegrees(euler.x), (float) Math.toDegrees(euler.y));
	}

	/**
	 * RigidBody Wrapper Methods
	 */
	@Override
	public double mass() {
		return mass;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public Vector3d velocity() {
		return new Vector3d(motionX, motionY, motionZ);
	}

	@Override
	public void setVelocity(Vector3d velocity) {
		this.motionX = velocity.x;
		this.motionY = velocity.y;
		this.motionZ = velocity.z;
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
		acceleration = acceleration.add(force.divide(mass));
	}
}
