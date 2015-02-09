package nova.core.util.transform;

import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

public class MatrixStackTest {
	MatrixStack ms;
	@Before
	public void setUp() {
		ms = new MatrixStack();
	}

	@Test(expected = EmptyStackException.class)
	public void testThrowsOnEmpty() {
		ms.popMatrix();
	}

	@Test
	public void testStack() {
		Matrix one = MatrixHelper.translationMatrix(1,0,0);
		Matrix two = MatrixHelper.translationMatrix(0,1,0);
		Matrix three = MatrixHelper.translationMatrix(0,0,1);
		ms.loadMatrix(one);
		ms.pushMatrix();
		ms.loadMatrix(two);
		ms.pushMatrix();
		ms.loadIdentity();
		ms.pushMatrix();
		ms.loadMatrix(three);
		ms.pushMatrix();
		ms.loadIdentity();

		assertEquals(Matrix.IDENTITY,ms.getMatrix());
		ms.popMatrix();
		assertEquals(three, ms.getMatrix());
		ms.popMatrix();
		assertEquals(Matrix.IDENTITY, ms.getMatrix());
		ms.popMatrix();
		assertEquals(two,ms.getMatrix());
		ms.popMatrix();
		assertEquals(one,ms.getMatrix());
	}
	@Test
	public void testTransforms() {
		ms.translate(Vector3d.one);
		ms.scale(Vector3d.one.multiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3d.yAxis, Math.PI/2);
		assertEquals(MatrixHelper.translationMatrix(Vector3d.one).multiply(MatrixHelper.scaleMatrix(Vector3d.one.multiply(2))).multiply(MatrixHelper.rotationMatrix(Vector3d.yAxis,Math.PI/2)),ms.getMatrix());

		ms.popMatrix();
		ms.transform(MatrixHelper.rotationMatrix(Vector3d.yAxis, Math.PI / 2));
		assertEquals(MatrixHelper.translationMatrix(Vector3d.one).multiply(MatrixHelper.scaleMatrix(Vector3d.one.multiply(2))).multiply(MatrixHelper.rotationMatrix(Vector3d.yAxis, Math.PI / 2)), ms.getMatrix());

		assertEquals(ms.getMatrix().transform(Vector3d.one), ms.transform(Vector3d.one));

	}

}