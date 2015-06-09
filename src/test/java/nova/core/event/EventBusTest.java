package nova.core.event;

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
		listenerList.add(new TestEventListener("A"));
		listenerList.add(new TestEventListener("B"));

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("AB");
	}

	@Test
	public void testOrdering() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.add(new TestEventListener("A"), 1);
		listenerList.add(new TestEventListener("B"), 1);
		listenerList.add(new TestEventListener("C"), 2);

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("CAB");
	}

	@Test
	public void testRemovalByHandle() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.add(new TestEventListener("A"));
		EventListenerHandle<TestEvent> handle = listenerList.add(new TestEventListener("B"));
		handle.close();

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
	}

	@Test
	public void testRemovalByObject() {
		EventBus<TestEvent> listenerList = new EventBus<>();
		listenerList.add(new TestEventListener("A"));

		TestEventListener listener = new TestEventListener("B");
		listenerList.add(listener);
		listenerList.remove(listener);

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
	}
}
