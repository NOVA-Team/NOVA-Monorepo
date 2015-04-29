package nova.core.util.transform;

import org.junit.Test;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;

public class Matrix4x4HelperTest {
	@Test
	public void testTranslation() {
		Vector3d start = Vector3d.zero;
		assertThat(MatrixHelper.translationMatrix(0, 0, 0).transform(start)).isEqualTo(start);
		Vector3d by = new Vector3d(3, 0, -6);
		assertThat(MatrixHelper.translationMatrix(by).transform(start)).isEqualTo(by);
		start = Vector3d.one;
		assertThat(MatrixHelper.translationMatrix(by).transform(start)).isEqualTo(start.add(by));
	}

	@Test
	public void testScale() {
		Vector3d start = Vector3d.one;
		assertThat(MatrixHelper.scaleMatrix(0, 0, 0).transform(start)).isEqualTo(Vector3d.zero);
		Vector3d by = new Vector3d(3, 0, -6);
		assertThat(MatrixHelper.scaleMatrix(by).transform(start)).isEqualTo(by);
		start = start.multiply(2);
		assertThat(MatrixHelper.scaleMatrix(by).transform(start))
			.isEqualTo(by.multiply(2))
			.isEqualTo(MatrixHelper.scaleMatrix(by.xd(), by.yd(), by.zd()).transform(start));
	}

	@Test
	public void testRotate() {
		Vector3d start = Vector3d.zAxis;
		assertThat(MatrixHelper.rotationMatrix(Vector3d.yAxis, -PI / 2).transform(start)).isEqualTo(Vector3d.xAxis);
		assertThat(MatrixHelper.rotationMatrix(Vector3d.yAxis.multiply(2), -PI / 2).transform(start)).isEqualTo(Vector3d.xAxis);
	}
}
