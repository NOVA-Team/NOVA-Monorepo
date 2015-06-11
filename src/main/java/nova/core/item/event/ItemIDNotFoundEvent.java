package nova.core.item.event;

import nova.core.item.Item;
import nova.core.util.Factory;

public class ItemIDNotFoundEvent {
	public final String id;

	private Factory<Item> remappedFactory = null;

	public ItemIDNotFoundEvent(String id) {
		this.id = id;
	}

	public Factory<Item> getRemappedFactory() {
		return remappedFactory;
	}

	public void setRemappedFactory(Factory<Item> remappedFactory) {
		this.remappedFactory = remappedFactory;
	}
}
