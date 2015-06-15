package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static nova.core.util.math.Vector3DUtil.*;

import static nova.testutils.NovaAssertions.*;

public class Vector3DUtilTest {

	@Test
	public void testRandomVector() {
		for (int i = 0; i < 20; i++) {
			Vector3D vec = Vector3DUtil.random();
			//Random vector should be of maximum length 1.
			assertThat(vec.getX()).isLessThan(1);
			assertThat(vec.getY()).isLessThan(1);
			assertThat(vec.getZ()).isLessThan(1);
		}
	}

	@Test
	public void testVectorMax() {
		assertThat(max(new Vector3D(1, 2, 3), new Vector3D(3, 2, 1))).isEqualTo(new Vector3D(3, 2, 3));
	}

	@Test
	public void testVectorMin() {
		assertThat(min(new Vector3D(1, 2, 3), new Vector3D(3, 2, 1))).isEqualTo(new Vector3D(1, 2, 1));
	}

}