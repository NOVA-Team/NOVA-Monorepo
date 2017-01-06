/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.event;

import nova.core.event.bus.CancelableEvent;
import nova.core.event.bus.Event;
import nova.core.item.ItemFactory;

/**
 * @author ExE Boss
 */
public abstract class ItemEvent extends CancelableEvent {

	public static class Register extends CancelableEvent {
		public ItemFactory itemFactory;

		public Register(ItemFactory itemFactory) {
			this.itemFactory = itemFactory;
		}
	}

	/**
	 * @author Stan
	 */
	public static class IDNotFound extends Event {
		public final String id;

		private ItemFactory remappedFactory = null;

		public IDNotFound(String id) {
			this.id = id;
		}

		public ItemFactory getRemappedFactory() {
			return remappedFactory;
		}

		public void setRemappedFactory(ItemFactory remappedFactory) {
			this.remappedFactory = remappedFactory;
		}
	}
}
