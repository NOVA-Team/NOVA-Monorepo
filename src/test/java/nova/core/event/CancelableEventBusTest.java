package nova.core.event;

import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Created by Stan on 5/02/2015.
 */
public class CancelableEventBusTest {
	@Test
	public void testCanceling() {
		CancelableEventBus<TestEvent> listenerList = new CancelableEventBus<>();
		listenerList.add(new TestEventListener("A", true));
		listenerList.add(new TestEventListener("B"));

		TestEvent event = new TestEvent();
		listenerList.publish(event);

		assertThat(event.toString()).isEqualTo("A");
		assertThat(event.isCanceled()).isTrue();
	}
}
