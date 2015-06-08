package nova.core.util.transform;

import nova.core.util.math.VectorUtil;
import nova.core.util.transform.matrix.MatrixHelper;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;

public class Matrix4x4HelperTest {
	@Test
	public void testTranslation() {
		Vector3D start = Vector3D.ZERO;
		assertThat(MatrixHelper.translationMatrix(0, 0, 0).transform(start)).isEqualTo(start);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(MatrixHelper.translationMatrix(by).transform(start)).isEqualTo(by);
		start = VectorUtil.ONE;
		assertThat(MatrixHelper.translationMatrix(by).transform(start)).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3D start = VectorUtil.ONE;
		assertThat(MatrixHelper.scaleMatrix(0, 0, 0).transform(start)).isEqualTo(Vector3D.ZERO);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(MatrixHelper.scaleMatrix(by).transform(start)).isEqualTo(by);
		start = start.scalarMultiply(2);
		assertThat(MatrixHelper.scaleMatrix(by).transform(start))
			.isEqualTo(by.scalarMultiply(2))
			.isEqualTo(MatrixHelper.scaleMatrix(by.getX(), by.getY(), by.getZ()).transform(start));
	}

	@Test
	public void testRotate() {
		Vector3D start = Vector3D.PLUS_K;
		assertThat(MatrixHelper.rotationMatrix(Vector3D.PLUS_J, -PI / 2).transform(start)).isEqualTo(Vector3D.PLUS_I);
		assertThat(MatrixHelper.rotationMatrix(Vector3D.PLUS_J.scalarMultiply(2), -PI / 2).transform(start)).isEqualTo(Vector3D.PLUS_I);
	}
}
