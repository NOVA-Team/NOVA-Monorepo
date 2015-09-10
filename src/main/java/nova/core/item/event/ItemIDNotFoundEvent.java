package nova.core.item.event;

import nova.core.event.bus.Event;
import nova.core.item.ItemFactory;

/**
 * @author Stan
 */
public class ItemIDNotFoundEvent extends Event {
	public final String id;

	private ItemFactory remappedFactory = null;

	public ItemIDNotFoundEvent(String id) {
		this.id = id;
	}

	public ItemFactory getRemappedFactory() {
		return remappedFactory;
	}

	public void setRemappedFactory(ItemFactory remappedFactory) {
		this.remappedFactory = remappedFactory;
	}
}
