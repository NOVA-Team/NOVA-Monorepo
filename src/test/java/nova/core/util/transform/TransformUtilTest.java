package nova.core.util.transform;

import nova.core.util.math.TransformUtil;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

import static java.lang.Math.PI;
import static nova.testutils.NovaAssertions.assertThat;

public class TransformUtilTest {
	@Test
	public void testTranslation() {
		Vector3D start = Vector3D.ZERO;
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(0, 0, 0))).isEqualTo(start);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(by))).isEqualTo(by);
		start = Vector3DUtil.ONE;
		assertThat(TransformUtil.transform(start, TransformUtil.translationMatrix(by))).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3D start = Vector3DUtil.ONE;
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(0, 0, 0))).isEqualTo(Vector3D.ZERO);
		Vector3D by = new Vector3D(3, 0, -6);
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(by))).isEqualTo(by);
		start = start.scalarMultiply(2);
		assertThat(TransformUtil.transform(start, TransformUtil.scaleMatrix(by)))
			.isEqualTo(Vector3DUtil.cartesianProduct(start, by));
	}

	@Test
	public void testRotate() {
		Vector3D start = Vector3D.PLUS_K;
		assertThat(TransformUtil.transformDirectionless(start, MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J, -PI / 2).getMatrix()))).isAlmostEqualTo(Vector3D.PLUS_I);
		assertThat(TransformUtil.transformDirectionless(start, MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J.scalarMultiply(2), -PI / 2).getMatrix()))).isAlmostEqualTo(Vector3D.PLUS_I);
	}
}
