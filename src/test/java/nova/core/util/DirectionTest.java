package nova.core.util;

import nova.core.util.transform.vector.Vector3d;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Calclavia
 */
public class DirectionTest {

	@Test
	public void testOrdinal() {
		for (int i = 0; i < 6; i++) {
			Direction actual = Direction.fromOrdinal(i);
			assertThat(actual.ordinal()).isEqualTo(i);
		}
	}

	@Test
	public void testVector() {
		Vector3d v1 = new Vector3d(1, 2, 1).normalize();
		assertThat(Direction.fromVector(v1)).isEqualTo(Direction.UP);

		Vector3d v2 = new Vector3d(2, -3, 1).normalize();
		assertThat(Direction.fromVector(v2)).isEqualTo(Direction.DOWN);

		Vector3d v3 = new Vector3d(4, -3, -11).normalize();
		assertThat(Direction.fromVector(v3)).isEqualTo(Direction.NORTH);

		Vector3d v4 = new Vector3d(4, -3, 6).normalize();
		assertThat(Direction.fromVector(v4)).isEqualTo(Direction.SOUTH);

		Vector3d v5 = new Vector3d(4, -3, 1).normalize();
		assertThat(Direction.fromVector(v5)).isEqualTo(Direction.EAST);

		Vector3d v6 = new Vector3d(-4, -3, 1).normalize();
		assertThat(Direction.fromVector(v6)).isEqualTo(Direction.WEST);
	}
}
