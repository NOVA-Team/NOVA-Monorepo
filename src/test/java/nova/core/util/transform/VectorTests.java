package nova.core.util.transform;

import nova.core.util.math.VectorUtil;
import nova.core.util.transform.vector.Vector2d;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorTests {

	@Test
	public void testVector3dMethods() throws Exception {
		Random random = new Random();
		Vector3D v1 = new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble());
		Vector3D v2 = new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isEqualTo(new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));
		assertThat(VectorUtil.reciprocal(v1)).isEqualTo(new Vector3D(1 / v1.getX(), 1 / v1.getY(), 1 / v1.getZ()));

		assertThat(v1.crossProduct(v2)).isEqualTo(new Vector3D(
			v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
			v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
			v1.getX() * v2.getY() - v1.getY() * v2.getX()));

		assertThat(v1.dotProduct(v2)).isEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ());
	}

	@Test
	public void testVector3iMethods() throws Exception {
		Random random = new Random();
		Vector3D v1 = new Vector3D(random.nextInt(), random.nextInt(), random.nextInt());
		Vector3D v2 = new Vector3D(random.nextInt(), random.nextInt(), random.nextInt());

		assertThat(v1.add(v2)).isEqualTo(new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));

		assertThat(VectorUtil.reciprocal(v1)).isEqualTo(new Vector3D(1 / v1.getX(), 1 / v1.getY(), 1 / v1.getZ()));

		assertThat(v1.crossProduct(v2)).isEqualTo(new Vector3D(
			v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
			v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
			v1.getX() * v2.getY() - v1.getY() * v2.getX()));

		//Won't work due to the values being integers
		//assertThat(v1.dot(v2)).isEqualTo(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
		assertThat(v1.dotProduct(v2)).isEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ());
	}

	@Test
	public void testVector2dMethods() throws Exception {
		Random random = new Random();
		Vector2d v1 = new Vector2d(random.nextDouble(), random.nextDouble());
		Vector2d v2 = new Vector2d(random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isEqualTo(new Vector2d(v1.x + v2.x, v1.y + v2.y));

		assertThat(v1.reciprocal()).isEqualTo(new Vector2d(1 / v1.x, 1 / v1.y));

		assertThat(v1.dotProduct(v2)).isEqualTo(v1.x * v2.x + v1.y * v2.y);
	}
}
