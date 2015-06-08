package nova.core.util.transform;

import nova.core.util.math.MatrixStack;
import nova.core.util.math.TransformUtil;
import nova.core.util.math.VectorUtil;
import nova.core.util.transform.matrix.Matrix4x4;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.assertj.core.api.Assertions.assertThat;

public class Matrix4x4StackTest {
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
		Matrix4x4 one = TransformUtil.translationMatrix(1, 0, 0);
		Matrix4x4 two = TransformUtil.translationMatrix(0, 1, 0);
		Matrix4x4 three = TransformUtil.translationMatrix(0, 0, 1);
		ms.loadMatrix(one);
		ms.pushMatrix();
		ms.loadMatrix(two);
		ms.pushMatrix();
		ms.loadIdentity();
		ms.pushMatrix();
		ms.loadMatrix(three);
		ms.pushMatrix();
		ms.loadIdentity();

		assertThat(ms.getMatrix()).isEqualTo(Matrix4x4.IDENTITY);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(three);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(Matrix4x4.IDENTITY);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(two);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(one);
	}

	@Test
	public void testTransforms() {
		ms.translate(VectorUtil.ONE);
		ms.scale(VectorUtil.ONE.scalarMultiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3D.PLUS_J, Math.PI / 2);
		assertThat(ms.apply(Vector3D.PLUS_K)).isEqualTo(new Vector3D(-1, 1, 1));

		ms.popMatrix();
		ms.transform(TransformUtil.rotationMatrix(Vector3D.PLUS_J, Math.PI / 2));
		assertThat(ms.apply(Vector3D.PLUS_K)).isEqualTo(new Vector3D(-1, 1, 1));

		assertThat(ms.apply(VectorUtil.ONE)).isEqualTo(ms.getMatrix().apply(VectorUtil.ONE));

	}

}
