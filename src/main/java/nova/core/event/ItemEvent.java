/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
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
