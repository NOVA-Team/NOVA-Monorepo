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

package nova.core.event;

import nova.core.event.bus.EventBus;
import nova.core.event.bus.EventListenerHandle;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Stan Hebben
 */
public class EventBusTest {
	@Test
	public void testEmpty() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("");
	}

	@Test
	public void testInvocation() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.on().bind(new TestEventListener("A"));
		listenerList.on().bind(new TestEventListener("B"));

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("AB");
	}

	@Test
	public void testOrdering() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.on().withPriority(1).bind(new TestEventListener("A"));
		listenerList.on().withPriority(1).bind(new TestEventListener("B"));
		listenerList.on().withPriority(2).bind(new TestEventListener("C"));

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("CAB");
	}

	@Test
	public void testRemovalByHandle() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.on().bind(new TestEventListener("A"));
		EventListenerHandle<TestEvent> handle = listenerList.on().bind(new TestEventListener("B"));
		handle.close();

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
	}

	@Test
	public void testRemovalByObject() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.on().bind(new TestEventListener("A"));

		TestEventListener listener = new TestEventListener("B");
		listenerList.on().bind(listener);
		listenerList.remove(listener);

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
	}
}
