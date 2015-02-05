package nova.core.event;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Stan on 5/02/2015.
 */
public class EventListenerListTest {
    @Test
    public void testEmpty() {
        EventListenerList<TestEvent> listenerList = new EventListenerList<>();
        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("", event.toString());
    }

    @Test
    public void testInvocation() {
        EventListenerList<TestEvent> listenerList = new EventListenerList<>();
        listenerList.add(new TestEventListener("A"));
        listenerList.add(new TestEventListener("B"));

        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("AB", event.toString());
    }

    @Test
    public void testOrdering() {
        EventListenerList<TestEvent> listenerList = new EventListenerList<>();
        listenerList.add(new TestEventListener("A"), 1);
        listenerList.add(new TestEventListener("B"), 1);
        listenerList.add(new TestEventListener("C"), 2);

        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("CAB", event.toString());
    }

    @Test
    public void testRemovalByHandle() {
        EventListenerList<TestEvent> listenerList = new EventListenerList<>();
        listenerList.add(new TestEventListener("A"));
        EventListenerHandle<TestEvent> handle = listenerList.add(new TestEventListener("B"));
        handle.close();

        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("A", event.toString());
    }

    @Test
    public void testRemovalByObject() {
        EventListenerList<TestEvent> listenerList = new EventListenerList<>();
        listenerList.add(new TestEventListener("A"));

        TestEventListener listener = new TestEventListener("B");
        listenerList.add(listener);
        listenerList.remove(listener);

        TestEvent event = new TestEvent();
        listenerList.publish(event);

        assertEquals("A", event.toString());
    }
}
