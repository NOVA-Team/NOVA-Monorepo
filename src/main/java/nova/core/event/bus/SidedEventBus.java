/*
 * Copyright (c) 2015 NOVA, All rights reserved.
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

package nova.core.event.bus;

import nova.core.network.NetworkTarget;
import nova.core.network.NetworkTarget.Side;
import nova.core.network.Syncable;

import java.util.HashMap;

/**
 * {@link EventBus} that can differentiate {@link NetworkTarget NetworkTargets}
 * and allows registration of handlers that only listen on a specific
 * {@link Side}. <b>Remember to {@link Side#reduce() reduce} the scope of any
 * {@link SidedEvent} that was sent over the network!</b>
 * @param <T> -Describe me-
 * @author Vic Nightfall
 */
public class SidedEventBus<T extends CancelableEvent> extends CancelableEventBus<T> {

	private NetworkEventProcessor eventProcessor;
	private boolean checkListenedBeforeSend = true;
	private HashMap<Class<?>, Side> listenedNetworkEvents = new HashMap<>();

	public SidedEventBus(NetworkEventProcessor eventProcessor) {
		this.eventProcessor = eventProcessor;
	}

	private void add(Class<?> clazz, Side side) {
		if (side == Side.NONE) {
			throw new IllegalArgumentException("Can't specify a sided event without a scope!");
		}
		Side side2 = listenedNetworkEvents.get(clazz);
		if (side2 != null) {
			if (side2 != Side.BOTH && side2 != side) {
				listenedNetworkEvents.put(clazz, Side.BOTH);
			}
		} else {
			listenedNetworkEvents.put(clazz, side);
		}
	}

	private boolean contains(Class<?> clazz, Side side) {
		Class<?> clazz2 = clazz;
		while (true) {
			Side side2 = listenedNetworkEvents.get(clazz2);
			if (side2 == side || side2 == Side.BOTH) {
				listenedNetworkEvents.put(clazz, side2);
				return true;
			}
			if (clazz2 == Object.class) {
				break;
			}
			clazz2 = clazz2.getSuperclass();
		}
		return false;
	}

	@Override
	public void publish(T event) {
		if (event instanceof SidedEventBus.SidedEvent) {
			SidedEventBus.SidedEvent sidedEvent = (SidedEventBus.SidedEvent) event;
			Side currentSide = Side.get();

			if (sidedEvent.getTarget().opposite().targets(currentSide)) {
				super.publish(event);
			}

			// Check if the event needs to be sent over the network.
			if (currentSide.targets(sidedEvent.getTarget())) {
				boolean send = !checkListenedBeforeSend;
				if (!send) {
					send = contains(event.getClass(), currentSide.opposite());
				}
				if (send) {
					eventProcessor.handleEvent(sidedEvent);
				}
			}
		} else {
			super.publish(event);
		}
	}

	@FunctionalInterface
	public interface NetworkEventProcessor {

		/**
		 * Gets called if the parent {@link SidedEventBus.SidedEventListener}
		 * received an event that needs to be sent over the network.
		 * @param event The event
		 */
		void handleEvent(SidedEvent event);
	}

	/**
	 * An event that specifies a {@link NetworkTarget}. Set the target by either
	 * overriding {@link #getTarget()} or by using the annotation
	 * {@link NetworkTarget} on the inherited class.
	 * @author Vic Nightfall
	 */
	public interface SidedEvent extends Syncable {

		default Side getTarget() {
			NetworkTarget target = getClass().getAnnotation(NetworkTarget.class);
			return target != null ? target.value() : Side.BOTH;
		}
	}

	protected static class SidedEventListener<E extends T, T> extends TypedEventListener<E, T> {

		public final Side side;

		public SidedEventListener(EventListener<E> wrappedListener, Class<E> eventClass, Side side) {
			super(wrappedListener, eventClass);
			this.side = side;
		}

		@Override
		public void onEvent(T event) {
			if (event instanceof SidedEventBus.SidedEvent) {
				SidedEventBus.SidedEvent sidedEvent = (SidedEventBus.SidedEvent) event;
				if (sidedEvent.getTarget().opposite().targets(side)) {
					super.onEvent(event);
				}
			} else {
				super.onEvent(event);
			}
		}
	}
}
