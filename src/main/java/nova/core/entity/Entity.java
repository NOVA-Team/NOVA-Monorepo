package nova.core.entity;

import nova.core.block.Stateful;
import nova.core.component.ComponentProvider;
import nova.core.component.transform.EntityTransform;
import nova.core.event.EventBus;
import nova.core.util.Identifiable;
import nova.core.util.UniqueIdentifiable;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.World;

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

	public final Vector3d position() {
		return transform().position();
	}

	public final Vector3d scale() {
		return transform().scale();
	}

	public final Vector3d pivot() {
		return transform().pivot();
	}

	public final Quaternion rotation() {
		return transform().rotation();
	}

	public final double x() {
		return position().x;
	}

	public final double y() {
		return position().y;
	}

	public final double z() {
		return position().z;
	}

	public void setWorld(World world) {
		transform().setWorld(world);
	}

	public void setPosition(Vector3d pos) {
		transform().setPosition(pos);
	}

	public void setScale(Vector3d scale) {
		transform().setScale(scale);
	}

	public void setPivot(Vector3d pivot) {
		transform().setPivot(pivot);
	}

	public void setRotation(Quaternion rotation) {
		transform().setRotation(rotation);
	}

	@Override
	public String getUniqueID() {
		//TODO: Is this safe?
		return get(UniqueIdentifiable.class).getUniqueID();
	}
}
