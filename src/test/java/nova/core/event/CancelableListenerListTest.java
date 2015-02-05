package nova.core.event;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Stan on 5/02/2015.
 */
public class CancelableListenerListTest {
    @Test
    public void testCanceling() {
        CancelableListenerList<TestEvent> listenerList = new CancelableListenerList<>();
        listenerList.add(new TestEventListener("A", true));
        listenerList.add(new TestEventListener("B"));

        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("A", event.toString());
        assertTrue(event.isCanceled());
    }
}
