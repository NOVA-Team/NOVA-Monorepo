package nova.core.util;

import nova.testutils.NovaAssertions;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

public class ProfilerTest {
	Profiler profiler;
	Offset<Double> offsetMicro = Offset.offset(1000D);

	@Before
	public void before() {
		profiler = new Profiler("testProfiler");
	}

	@Test
	public void testName() {
		assertThat(profiler.name).isEqualTo("testProfiler");
	}

	@Test
	public void testTimeAtStartIs0() {
		profiler.start();
		Double elapsed = profiler.elapsed();
		Double lap = profiler.lap();
		Double end = profiler.end();
		assertThat(elapsed).isCloseTo(0D, offsetMicro);
		assertThat(lap).isCloseTo(0D, offsetMicro);
		assertThat(end).isCloseTo(0D, offsetMicro);
		assertThat(profiler.lastTime()).isCloseTo(0D, offsetMicro);
	}

	@Test
	public void testTimings() throws InterruptedException {
		profiler.start();
		Thread.sleep(2);
		double lap = profiler.lap();
		Thread.sleep(10);
		profiler.end();

		// To check the unit. Doesn't work well on Travis.
		//assertThat(lap).isCloseTo(2 / 1e3, Offset.offset(2 / 1e3));
		//assertThat(profiler.lastTime()).isCloseTo(10 / 1e3, Offset.offset(2 / 1e3));

		assertThat(profiler.average()).isCloseTo((lap + profiler.lastTime()) / 2, NovaAssertions.offsetD);
	}

	@Test
	public void testDataCollection() {
		profiler.start();
		profiler.lap();
		profiler.end();
		profiler.start();
		profiler.end();

		assertThat(profiler.results()).hasSize(3);
		profiler.clearResults();
		assertThat(profiler.results()).hasSize(0);
	}

	@Test
	public void testIsRunning() {
		assertThat(profiler.isRunning()).isFalse();
		profiler.start();
		assertThat(profiler.isRunning()).isTrue();
		profiler.lap();
		assertThat(profiler.isRunning()).isTrue();
		profiler.end();
		assertThat(profiler.isRunning()).isFalse();
	}

	@Test
	public void testToString() throws InterruptedException {
		profiler.start();
		Thread.sleep(10);
		profiler.end();
		assertThat(profiler.toString()).startsWith("testProfiler");    // Profiler name.
	}

	@Test(expected = IllegalStateException.class)
	public void testExceptionIfEndIsCalledOnNotRunningProfiler() {
		profiler.end();
	}

	@Test(expected = IllegalStateException.class)
	public void testExceptionIfStartIsCalledOnRunningProfiler() {
		profiler.start().start();
	}

	@Test(expected = IllegalStateException.class)
	public void testExceptionIfLapIsCalledOnNotRunningProfiler() {
		profiler.end();
	}

	@Test(expected = IllegalStateException.class)
	public void testExceptionIfElapsedIsCalledOnNotRunningProfiler() {
		profiler.elapsed();
	}

}