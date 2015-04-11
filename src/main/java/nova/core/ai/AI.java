package nova.core.ai;

import nova.core.entity.Entity;
import nova.core.event.Cancelable;

public class AI<T extends Entity> {

	public final T entity;

	public AI(T entity) {
		this.entity = entity;
	}

	boolean isEventQueued(Event event) {
		return false;
	}

	public Cancelable start(TaskList list) {
		return null;
	}

	public static CTaskBuilder on(Event event) {
		return new TaskBuilder(event);
	}
}
