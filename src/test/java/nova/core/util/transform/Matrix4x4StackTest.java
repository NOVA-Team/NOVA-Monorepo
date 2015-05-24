package nova.core.util.transform;

import nova.core.util.transform.matrix.Matrix4x4;
import nova.core.util.transform.matrix.MatrixHelper;
import nova.core.util.transform.matrix.MatrixStack;
import nova.core.util.transform.vector.Vector3d;
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
		Matrix4x4 one = MatrixHelper.translationMatrix(1, 0, 0);
		Matrix4x4 two = MatrixHelper.translationMatrix(0, 1, 0);
		Matrix4x4 three = MatrixHelper.translationMatrix(0, 0, 1);
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
		ms.translate(Vector3d.one);
		ms.scale(Vector3d.one.multiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3d.yAxis, Math.PI / 2);
		assertThat(ms.transform(Vector3d.zAxis)).isEqualTo(new Vector3d(-1, 1, 1));

		ms.popMatrix();
		ms.transform(MatrixHelper.rotationMatrix(Vector3d.yAxis, Math.PI / 2));
		assertThat(ms.transform(Vector3d.zAxis)).isEqualTo(new Vector3d(-1, 1, 1));

		assertThat(ms.transform(Vector3d.one)).isEqualTo(ms.getMatrix().transform(Vector3d.one));

	}

}
