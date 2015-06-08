package nova.core.util.transform;

import nova.core.util.math.Vector2DUtil;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import java.util.Random;

import static nova.testutils.asserts.AlmostEqualAssertion.assertThat;
import static nova.testutils.asserts.AlmostEqualAssertion.assertThatDouble;

public class VectorTests {

	@Test
	public void testVector3dMethods() throws Exception {
		Random random = new Random();
		Vector3D v1 = new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble());
		Vector3D v2 = new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isAlmostEqualTo(new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));
		assertThat(Vector3DUtil.reciprocal(v1)).isAlmostEqualTo(new Vector3D(1 / v1.getX(), 1 / v1.getY(), 1 / v1.getZ()));

		assertThat(v1.crossProduct(v2)).isAlmostEqualTo(new Vector3D(
				v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
				v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
				v1.getX() * v2.getY() - v1.getY() * v2.getX()));

		assertThatDouble(v1.dotProduct(v2)).isAlmostEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ());
	}

	@Test
	public void testVector3iMethods() throws Exception {
		Random random = new Random();
		Vector3D v1 = new Vector3D(random.nextInt() % (2 << 16), random.nextInt() % (2 << 16), random.nextInt() % (2 << 16));
		Vector3D v2 = new Vector3D(random.nextInt() % (2 << 16), random.nextInt() % (2 << 16), random.nextInt() % (2 << 16));

		assertThat(v1.add(v2)).isEqualTo(new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));

		assertThat(Vector3DUtil.reciprocal(v1)).isEqualTo(new Vector3D(1 / v1.getX(), 1 / v1.getY(), 1 / v1.getZ()));

		assertThat(v1.crossProduct(v2)).isAlmostEqualTo(new Vector3D(
				v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
				v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
				v1.getX() * v2.getY() - v1.getY() * v2.getX()));

		//Won't work due to the values being integers
		//assertThat(v1.dot(v2)).isEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.z * v2.z);
		assertThatDouble(v1.dotProduct(v2)).isAlmostEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ());
	}

	@Test
	public void testVector2dMethods() throws Exception {
		Random random = new Random();
		Vector2D v1 = new Vector2D(random.nextDouble(), random.nextDouble());
		Vector2D v2 = new Vector2D(random.nextDouble(), random.nextDouble());

		assertThat(v1.add(v2)).isEqualTo(new Vector2D(v1.getX() + v2.getX(), v1.getY() + v2.getY()));

		assertThat(Vector2DUtil.reciprocal(v1)).isEqualTo(new Vector2D(1 / v1.getX(), 1 / v1.getY()));

		assertThat(v1.dotProduct(v2)).isEqualTo(v1.getX() * v2.getX() + v1.getY() * v2.getY());
	}
}
