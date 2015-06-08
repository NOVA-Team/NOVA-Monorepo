package nova.core.util.transform;

import nova.core.util.math.TransformUtil;
import nova.core.util.math.VectorUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static java.lang.Math.PI;

public class Matrix4x4HelperTest {
	@Test
	public void testTranslation() {
		Vector3D start = Vector3D.ZERO;
		assertThat(TransformUtil.translationMatrix(0, 0, 0).apply(start)).isEqualTo(start);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.translationMatrix(by).apply(start)).isEqualTo(by);
		start = VectorUtil.ONE;
		assertThat(TransformUtil.translationMatrix(by).apply(start)).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3D start = VectorUtil.ONE;
		assertThat(TransformUtil.scaleMatrix(0, 0, 0).apply(start)).isEqualTo(Vector3D.ZERO);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.scaleMatrix(by).apply(start)).isEqualTo(by);
		start = start.scalarMultiply(2);
		assertThat(TransformUtil.scaleMatrix(by).apply(start))
			.isEqualTo(by.scalarMultiply(2))
			.isEqualTo(TransformUtil.scaleMatrix(by.getX(), by.getY(), by.getZ()).apply(start));
	}

	@Test
	public void testRotate() {
		Vector3D start = Vector3D.PLUS_K;
		assertThat(TransformUtil.rotationMatrix(Vector3D.PLUS_J, -PI / 2).apply(start)).isEqualTo(Vector3D.PLUS_I);
		assertThat(TransformUtil.rotationMatrix(Vector3D.PLUS_J.scalarMultiply(2), -PI / 2).apply(start)).isEqualTo(Vector3D.PLUS_I);
	}
}
