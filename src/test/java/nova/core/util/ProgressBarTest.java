/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util;

import nova.core.event.bus.Event;
import nova.core.event.bus.EventBus;
import nova.testutils.mod.TestMod;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ExE Boss
 */
public class ProgressBarTest {

	EventBus<Event> events;

    @Before
    public void setUp() {
		this.events = new EventBus<>();
	}

	@Test
	public void testProgressBar() {
		events.on(ProgressBarEvent.class).bind(evt -> System.out.println(evt.message));
		FakeProgressBar progressBar = new FakeProgressBar(events);
		progressBar.step("Progress bar testing generic String message");
		progressBar.step("Progress bar testing class message", ProgressBarTest.class);
		progressBar.step(ProgressBarTest.class, "Progress bar testing class message");
		progressBar.step(ProgressBarTest.class);
		progressBar.step("Progress bar testing mod message", TestMod.class);
		progressBar.step(TestMod.class, "Progress bar testing mod message");
		progressBar.step(TestMod.class);
	}

	static final class ProgressBarEvent extends Event {
		public final String message;

		public ProgressBarEvent(String message) {
			this.message = message;
		}
	}

	static final class FakeProgressBar implements ProgressBar {

		private final EventBus<Event> events;

		public FakeProgressBar(EventBus<Event> events) {
			this.events = events;
		}

		@Override
		public void step(String message) {
			this.events.publish(new ProgressBarEvent(message));
		}
	}
}
