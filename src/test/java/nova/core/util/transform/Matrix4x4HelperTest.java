package nova.core.util.transform;

import nova.core.util.math.VectorUtil;
import nova.core.util.transform.matrix.MatrixUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;

public class Matrix4x4HelperTest {
	@Test
	public void testTranslation() {
		Vector3D start = Vector3D.ZERO;
		assertThat(MatrixUtil.translationMatrix(0, 0, 0).apply(start)).isEqualTo(start);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(MatrixUtil.translationMatrix(by).apply(start)).isEqualTo(by);
		start = VectorUtil.ONE;
		assertThat(MatrixUtil.translationMatrix(by).apply(start)).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3D start = VectorUtil.ONE;
		assertThat(MatrixUtil.scaleMatrix(0, 0, 0).apply(start)).isEqualTo(Vector3D.ZERO);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(MatrixUtil.scaleMatrix(by).apply(start)).isEqualTo(by);
		start = start.scalarMultiply(2);
		assertThat(MatrixUtil.scaleMatrix(by).apply(start))
			.isEqualTo(by.scalarMultiply(2))
			.isEqualTo(MatrixUtil.scaleMatrix(by.getX(), by.getY(), by.getZ()).apply(start));
	}

	@Test
	public void testRotate() {
		Vector3D start = Vector3D.PLUS_K;
		assertThat(MatrixUtil.rotationMatrix(Vector3D.PLUS_J, -PI / 2).apply(start)).isEqualTo(Vector3D.PLUS_I);
		assertThat(MatrixUtil.rotationMatrix(Vector3D.PLUS_J.scalarMultiply(2), -PI / 2).apply(start)).isEqualTo(Vector3D.PLUS_I);
	}
}
