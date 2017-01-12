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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Used to test {@link ProgressBar}.
 *
 * @author ExE Boss
 */
public class ProgressBarTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testProgressBar() {
		Object[][] testContents = new Object[][] {
			{"Progress bar testing generic String message"},
			{"Progress bar testing class message", ProgressBarTest.class},
			{ProgressBarTest.class, "Progress bar testing class message"},
			{ProgressBarTest.class},
			{"Progress bar testing mod message", TestMod.class},
			{TestMod.class, "Progress bar testing mod message"},
			{TestMod.class},
			{"Progress bar testing null message", null},
			{null, "Progress bar testing null message"},
			{(String) null},
			{(Class) null},
			{ProgressBarTest.class, null},
			{null, ProgressBarTest.class}
		};
		String[] expectedResults = new String[] {
			"Progress bar testing generic String message",
			"Progress bar testing class message: ProgressBarTest",
			"ProgressBarTest: Progress bar testing class message",
			"ProgressBarTest",
			"Progress bar testing mod message: Test Mod",
			"Test Mod: Progress bar testing mod message",
			"Test Mod",
			"Progress bar testing null message: null",
			"null: Progress bar testing null message",
			null,
			null,
			"ProgressBarTest",
			"ProgressBarTest"
		};
		List<String> testResults = new LinkedList<>();
		ProgressBar progressBar = new AbstractProgressBar() {
			@Override
			protected void stepImpl(String message) {
				testResults.add(message);
			}
		};
		for (Object[] testArg : testContents) {
			step(progressBar, testArg);
		}
		progressBar.finish();

		assertThat(testResults.size()).isEqualTo(testContents.length);

		assertThat(testResults).containsExactly(expectedResults);
	}

	private static void step(ProgressBar progressBar, Object... args) {
		if (args.length == 1) {
			if (args[0] == null || args[0] instanceof String) {
				progressBar.step((String) args[0]);
				return;
			} else if (args[0] == null || args[0] instanceof Class) {
				progressBar.step((Class) args[0]);
				return;
			}
		} else if (args.length == 2) {
			if ((args[0] == null || args[0] instanceof String) && (args[1] == null || args[1] instanceof Class)) {
				progressBar.step((String) args[0], (Class) args[1]);
				return;
			} else if ((args[0] == null || args[0] instanceof Class) && (args[1] == null || args[1] instanceof String)) {
				progressBar.step((Class) args[0], (String) args[1]);
				return;
			}
		}

		throw new IllegalArgumentException("Wrong arguments");
	}

	@Test
	public void testFinish() {
		ProgressBar progressBar = new AbstractProgressBar() {@Override protected void stepImpl(String message) {}};
		progressBar.finish();
		assertThat(progressBar.isFinished()).isTrue();
		thrown.expect(IllegalStateException.class);
		progressBar.step("THROW EXCEPTION");
	}
}
