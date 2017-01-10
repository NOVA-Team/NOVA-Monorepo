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

package nova.core.util;

import nova.core.event.bus.Event;
import nova.core.event.bus.EventBus;
import nova.testutils.mod.TestMod;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Used to test {@link ProgressBar}.
 *
 * @author ExE Boss
 */
public class ProgressBarTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	EventBus<Event> events;

    @Before
    public void setUp() {
		this.events = new EventBus<>();
		this.events.on(ProgressBarEvent.class).bind(evt -> System.out.println(evt.message));
	}

	@Test
	public void testProgressBar() {
		ProgressBar progressBar = new AbstractProgressBar() {
			@Override
			protected void stepImpl(String message) {
				events.publish(new ProgressBarEvent(message));
			}
		};
		progressBar.step("Progress bar testing generic String message");
		progressBar.step("Progress bar testing class message", ProgressBarTest.class);
		progressBar.step(ProgressBarTest.class, "Progress bar testing class message");
		progressBar.step(ProgressBarTest.class);
		progressBar.step("Progress bar testing mod message", TestMod.class);
		progressBar.step(TestMod.class, "Progress bar testing mod message");
		progressBar.step(TestMod.class);
		progressBar.finish();
	}

	@Test
	public void testFinish() {
		ProgressBar progressBar = new AbstractProgressBar() {@Override protected void stepImpl(String message) {}};
		progressBar.finish();
		assertThat(progressBar.isFinished()).isTrue();
		thrown.expect(IllegalStateException.class);
		progressBar.step("THROW EXCEPTION");
	}

	static final class ProgressBarEvent extends Event {
		public final String message;

		public ProgressBarEvent(String message) {
			this.message = message;
		}
	}
}
