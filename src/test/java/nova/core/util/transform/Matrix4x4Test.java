package nova.core.util.transform;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class Matrix4x4Test {

	@Test
	public void testImmutability() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 startMatrix = new Matrix4x4(start);
		start[3][3] = 15;
		Matrix4x4 endMatrix = new Matrix4x4(start);
		assertThat(endMatrix).isNotSameAs(startMatrix);

	}

	@Test
	public void testMultiply() {
		assertThat(Matrix4x4.IDENTITY.multiply(Matrix4x4.IDENTITY)).isEqualTo(Matrix4x4.IDENTITY);
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 startMatrix = new Matrix4x4(start);
		assertThat(Matrix4x4.IDENTITY.multiply(startMatrix)).isEqualTo(startMatrix);
		assertThat(startMatrix.multiply(Matrix4x4.IDENTITY)).isEqualTo(startMatrix);

		double[][] res = {
			{ 38, 14, 17, 20 },
			{ 98, 46, 57, 68 },
			{ 9, 18, 27, 36 },
			{ 0, 0, 0, 0 } };
		assertThat(startMatrix.multiply(startMatrix)).isEqualTo(new Matrix4x4(res));

		assertThat(startMatrix.rightMultiply(startMatrix)).isEqualTo(new Matrix4x4(res));
	}

	@Test
	public void testTranspose() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 startMatrix = new Matrix4x4(start);
		double[][] end = {
			{ 1, 5, 9, 0 },
			{ 2, 6, 0, 0 },
			{ 3, 7, 0, 0 },
			{ 4, 8, 0, 0 } };
		Matrix4x4 endMatrix = new Matrix4x4(end);
		assertThat(endMatrix.transpose()).isEqualTo(startMatrix);

		assertThat(Matrix4x4.IDENTITY.transpose()).isEqualTo(Matrix4x4.IDENTITY);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultCtor() {
		assertThat(new Matrix4x4()).isEqualTo(Matrix4x4.IDENTITY);
	}

	@Test
	public void testTransform() {
		assertThat(Matrix4x4.IDENTITY.transform(new Vector3d(2, 3, 4))).isEqualTo(new Vector3d(2, 3, 4));
	}

	@Test
	public void testClone() {
		assertThat(Matrix4x4.IDENTITY.clone()).isEqualTo(Matrix4x4.IDENTITY);
	}

	@Test(expected = AssertionError.class)
	public void testIllegalArgument1() {
		new Matrix4x4(new double[][]{{1,1},{1,1}});
	}

	@Test(expected = AssertionError.class)
	public void testIllegalArgument2() {
		new Matrix4x4(new double[][]{{1,1},{1,1},{1,1},{1,1}});
	}
	@Test
	public void testEquals() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 firstMatrix = new Matrix4x4(start);
		start[2][3] = 17;
		Matrix4x4 secondMatrix = new Matrix4x4(start);
		assertThat(firstMatrix.equals(secondMatrix)).isFalse();
		assertThat(firstMatrix.equals(firstMatrix)).isTrue();
		assertThat(firstMatrix.equals("test")).isFalse();
	}
}
