package nova.core.util.transform;

import org.junit.Test;

import static org.junit.Assert.*;
import static java.lang.Math.PI;

public class MatrixHelperTest {
	@Test
	public void testTranslation() {
		Vector3d start = Vector3d.zero;
		assertEquals(start, MatrixHelper.translationMatrix(0,0,0).transform(start));
		Vector3d by = new Vector3d(3,0,-6);
		assertEquals(by, MatrixHelper.translationMatrix(by).transform(start));
		start = Vector3d.one;
		assertEquals(start.add(by), MatrixHelper.translationMatrix(by).transform(start));
	}
	@Test
	public void testScale() {
		Vector3d start = Vector3d.one;
		assertEquals(Vector3d.zero, MatrixHelper.scaleMatrix(0,0,0).transform(start));
		Vector3d by = new Vector3d(3,0,-6);
		assertEquals(by, MatrixHelper.scaleMatrix(by).transform(start));
		start = start.multiply(2);
		assertEquals(by.multiply(2), MatrixHelper.scaleMatrix(by).transform(start));
		assertEquals(MatrixHelper.scaleMatrix(by).transform(start), MatrixHelper.scaleMatrix(by.xd(), by.yd(), by.zd()).transform(start));
	}

	@Test
	public void testRotate() {
		Vector3d start = Vector3d.zAxis;
		assertEquals(Vector3d.xAxis, MatrixHelper.rotationMatrix(Vector3d.yAxis, -PI/2).transform(start));
		assertEquals(Vector3d.xAxis, MatrixHelper.rotationMatrix(Vector3d.yAxis.multiply(2),-PI/2).transform(start));
	}
}