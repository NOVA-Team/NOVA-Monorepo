package nova.core.util.math;

import nova.core.util.math.MatrixStack;
import nova.core.util.math.TransformUtil;
import nova.core.util.math.Vector3DUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static nova.testutils.NovaAssertions.assertThat;

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
		RealMatrix one = TransformUtil.translationMatrix(1, 0, 0);
		RealMatrix two = TransformUtil.translationMatrix(0, 1, 0);
		RealMatrix three = TransformUtil.translationMatrix(0, 0, 1);
		ms.loadMatrix(one);
		ms.pushMatrix();
		ms.loadMatrix(two);
		ms.pushMatrix();
		ms.loadIdentity();
		ms.pushMatrix();
		ms.loadMatrix(three);
		ms.pushMatrix();
		ms.loadIdentity();

		assertThat(ms.getMatrix()).isEqualTo(MatrixUtils.createRealIdentityMatrix(4));
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(three);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(MatrixUtils.createRealIdentityMatrix(4));
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(two);
		ms.popMatrix();
		assertThat(ms.getMatrix()).isEqualTo(one);
	}

	@Test
	public void testTransforms() {
		ms.translate(Vector3DUtil.ONE);
		ms.scale(Vector3DUtil.ONE.scalarMultiply(2));
		ms.pushMatrix();
		ms.rotate(Vector3D.PLUS_J, Math.PI / 2);
		assertThat(ms.apply(Vector3D.PLUS_K)).isAlmostEqualTo(new Vector3D(-1, 1, 1));

		ms.popMatrix();
		ms.transform(MatrixUtils.createRealMatrix(new Rotation(Vector3D.PLUS_J, Math.PI / 2).getMatrix()));
		assertThat(ms.apply(Vector3D.PLUS_K)).isAlmostEqualTo(new Vector3D(-1, 1, 1));

		assertThat(ms.apply(Vector3DUtil.ONE)).isAlmostEqualTo(ms.apply(Vector3DUtil.ONE));

	}

}
