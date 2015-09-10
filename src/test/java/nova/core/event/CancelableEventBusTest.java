package nova.core.event;

import nova.core.event.bus.CancelableEventBus;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author Stan
 */
public class CancelableEventBusTest {
	@Test
	public void testCanceling() {
		CancelableEventBus<TestEvent> listenerList = new CancelableEventBus<>();
		listenerList.on().bind(new TestEventListener("A", true));
		listenerList.on().bind(new TestEventListener("B"));

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
		assertThat(event.isCanceled()).isTrue();
	}
}
