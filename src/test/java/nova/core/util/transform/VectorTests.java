package nova.core.util.transform;

import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorTests {

	@Test
	public void testVector3dMethods() throws Exception {
		Random random = new Random();
		Vector3d v1 = new Vector3d(random.nextDouble(), random.nextDouble(), random.nextDouble());
		Vector3d v2 = new Vector3d(random.nextDouble(), random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isEqualTo(new Vector3d(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z));
		assertThat(v1.multiply(v2)).isEqualTo(new Vector3d(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z));

		assertThat(v1.reciprocal()).isEqualTo(new Vector3d(1 / v1.x, 1 / v1.y, 1 / v1.z));

		assertThat(v1.cross(v2)).isEqualTo(new Vector3d(
			v1.y * v2.z - v1.z * v2.y,
			v1.z * v2.x - v1.x * v2.z,
			v1.x * v2.y - v1.y * v2.x));

		assertThat(v1.dot(v2)).isEqualTo(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
	}

	@Test
	public void testVector3iMethods() throws Exception {
		Random random = new Random();
		Vector3i v1 = new Vector3i(random.nextInt(), random.nextInt(), random.nextInt());
		Vector3i v2 = new Vector3i(random.nextInt(), random.nextInt(), random.nextInt());

		assertThat(v1.add(v2)).isEqualTo(new Vector3i(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z));
		assertThat(v1.multiply(v2)).isEqualTo(new Vector3i(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z));

		assertThat(v1.reciprocal()).isEqualTo(new Vector3i(1 / v1.x, 1 / v1.y, 1 / v1.z));

		assertThat(v1.cross(v2)).isEqualTo(new Vector3i(
			v1.y * v2.z - v1.z * v2.y,
			v1.z * v2.x - v1.x * v2.z,
			v1.x * v2.y - v1.y * v2.x));

		//Won't work due to the values being integers
		//assertThat(v1.dot(v2)).isEqualTo(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
		assertThat(v1.dot(v2)).isEqualTo(v1.xd() * v2.xd() + v1.yd() * v2.yd() + v1.zd() * v2.zd());
	}

	@Test
	public void testVector2dMethods() throws Exception {
		Random random = new Random();
		Vector2d v1 = new Vector2d(random.nextDouble(), random.nextDouble());
		Vector2d v2 = new Vector2d(random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isEqualTo(new Vector2d(v1.x + v2.x, v1.y + v2.y));
		assertThat(v1.multiply(v2)).isEqualTo(new Vector2d(v1.x * v2.x, v1.y * v2.y));

		assertThat(v1.reciprocal()).isEqualTo(new Vector2d(1 / v1.x, 1 / v1.y));

		assertThat(v1.dot(v2)).isEqualTo(v1.x * v2.x + v1.y * v2.y);
	}
}
