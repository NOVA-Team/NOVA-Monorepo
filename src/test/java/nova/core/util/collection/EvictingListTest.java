package nova.core.util.collection;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.*;

public class EvictingListTest {

	EvictingList<String> list;

	@Before
	public void setUp() {
		list = new EvictingList<>(5);
	}

	@Test
	public void testCapacity() {
		assertThat(list.limit()).isEqualTo(5);
	}

	@Test
	public void testMaximumSize() {
		fill();
		assertThat(list.size()).isEqualTo(list.limit());
	}

	@Test
	public void testOldestAndNewest() {
		fill();
		assertThat(list.getOldest()).isEqualTo("5");
		assertThat(list.getLastest()).isEqualTo("9");
	}

	private void fill() {
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");
	}
}