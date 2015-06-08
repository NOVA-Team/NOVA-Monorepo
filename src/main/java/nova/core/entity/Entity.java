package nova.core.entity;

import nova.core.block.Stateful;
import nova.core.component.ComponentProvider;
import nova.core.component.transform.EntityTransform;
import nova.core.event.EventBus;
import nova.core.util.Identifiable;
import nova.core.util.UniqueIdentifiable;
import nova.core.util.transform.matrix.Rotation;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * An entity is an object in the world that has a position.
 */
public abstract class Entity extends ComponentProvider implements UniqueIdentifiable, Identifiable, Stateful {

	public final EventBus<Stateful.LoadEvent> loadEvent = new EventBus<>();
	public final EventBus<Stateful.UnloadEvent> unloadEvent = new EventBus<>();

	public final EntityTransform transform() {
		return get(EntityTransform.class);
	}

	public final World world() {
		return transform().world();
	}

	public final Vector3D position() {
		return transform().position();
	}

	public final Vector3D scale() {
		return transform().scale();
	}

	public final Vector3D pivot() {
		return transform().pivot();
	}

	public final Rotation rotation() {
		return transform().rotation();
	}

	public final double x() {
		return position().getX();
	}

	public final double y() {
		return position().getY();
	}

	public final double z() {
		return position().getZ();
	}

	public void setWorld(World world) {
		transform().setWorld(world);
	}

	public void setPosition(Vector3D pos) {
		transform().setPosition(pos);
	}

	public void setScale(Vector3D scale) {
		transform().setScale(scale);
	}

	public void setPivot(Vector3D pivot) {
		transform().setPivot(pivot);
	}

	public void setRotation(Rotation rotation) {
		transform().setRotation(rotation);
	}

	@Override
	public String getUniqueID() {
		//TODO: Is this safe?
		// I'm not sure what to do here
		return get(UniqueIdentifiable.class).getUniqueID();
	}
}
