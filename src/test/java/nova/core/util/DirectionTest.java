package nova.core.util;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

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
		Vector3D v1 = new Vector3D(1, 2, 1).normalize();
		assertThat(Direction.fromVector(v1)).isEqualTo(Direction.UP);

		Vector3D v2 = new Vector3D(2, -3, 1).normalize();
		assertThat(Direction.fromVector(v2)).isEqualTo(Direction.DOWN);

		Vector3D v3 = new Vector3D(4, -3, -11).normalize();
		assertThat(Direction.fromVector(v3)).isEqualTo(Direction.NORTH);

		Vector3D v4 = new Vector3D(4, -3, 6).normalize();
		assertThat(Direction.fromVector(v4)).isEqualTo(Direction.SOUTH);

		Vector3D v5 = new Vector3D(4, -3, 1).normalize();
		assertThat(Direction.fromVector(v5)).isEqualTo(Direction.EAST);

		Vector3D v6 = new Vector3D(-4, -3, 1).normalize();
		assertThat(Direction.fromVector(v6)).isEqualTo(Direction.WEST);
	}

	@Test
	public void testOpposite() {
		assertThat(Direction.DOWN.opposite()).isEqualTo(Direction.UP);
		assertThat(Direction.EAST.opposite().opposite()).isEqualTo(Direction.EAST);
		assertThat(Direction.UNKNOWN.opposite()).isEqualTo(Direction.UNKNOWN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ordinalTooSmall() {
		Direction.fromOrdinal(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void ordinalTooLarge() {
		Direction.fromOrdinal(7);
	}

}
