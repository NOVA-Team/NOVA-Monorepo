package nova.core.item.event;

import nova.core.item.ItemFactory;

/**
 * Created by Stan on 3/02/2015.
 */
public class ItemIDNotFoundEvent {
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
