package nova.core.util.transform;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatrixTest {

	@Test
	public void testImmutability() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix startMatrix = new Matrix(start);
		start[3][3] = 15;
		Matrix endMatrix = new Matrix(start);
		assertThat(endMatrix).isNotSameAs(startMatrix);
	}

	@Test
	public void testSwap() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };

		double[][] end = {
			{ 5, 6, 7, 8 },
			{ 1, 2, 3, 4 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };

		Matrix startMatrix = new Matrix(start);
		Matrix endMatrix = new Matrix(end);

		assertThat(startMatrix.swap(0, 1)).isEqualTo(endMatrix);
	}

	@Test
	public void testAdd() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 2 },
				{ 5, 6 } }
		);

		Matrix end = new Matrix(
			new double[][] {
				{ 2, 4 },
				{ 10, 12 } }
		);

		assertThat(end)
			.isEqualTo(start.add(start))
			.isEqualTo(start.multiply(2));
	}

	@Test
	public void testAugment() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 2 },
				{ 5, 6 } }
		);

		Matrix end = new Matrix(
			new double[][] {
				{ 1, 2, 3 },
				{ 5, 6, 3 } }
		);

		assertThat(start.augment(new Vector2d(3, 3).toMatrix())).isEqualTo(end);
	}

	@Test
	public void testSubmatrix() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 2, 3 },
				{ 5, 6, 3 } }
		);
		Matrix sub = new Matrix(
			new double[][] {
				{ 1, 2 },
				{ 5, 6 } }
		);

		assertThat(start.submatrix(0, 1, 0, 1)).isEqualTo(sub);
	}

	@Test
	public void testRref() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 3 },
				{ -5, 0 } }
		);

		assertThat(Matrix.identity(2)).isEqualTo(start.rref());

		start = new Matrix(new double[][] { { 0, 3, 4 }, { 5, 2, 3 }, { 1, 5, 1 } });
		assertThat(Matrix.identity(3)).isEqualTo(start.rref());
	}

	@Test
	public void testInverse() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 3 },
				{ -5, 0 } }
		);
		Matrix inverse = new Matrix(
			new double[][] {
				{ 0, -1 / 5d },
				{ 1 / 3d, 1 / 15d } }
		);

		Matrix reciprocal = start.reciprocal();
		assertThat(inverse.subtract(reciprocal).isAlmostZero()).isTrue();
	}

/*	@Test
	@Ignore
	public void testDeterminant() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, -3, 0 },
				{ -2, 4, 1 },
				{ 5, -2, 2 } }
		);

		assertThat(start.determinant()).isCloseTo(-17, within(0.0001));
	}*/

	@Test
	public void testSolve() {
		Matrix A = new Matrix(
			new double[][] {
				{ 1, 2, 3 },
				{ 3, 4, 7 },
				{ 6, 5, 9 } }
		);

		Matrix B = new Matrix(new double[][] {
			{ 0 },
			{ 2 },
			{ 11 },
		});

		Matrix x = A.solve(B);
		Matrix expectedX = new Matrix(new double[][] {
			{ 4 },
			{ 1 },
			{ -2 }
		});
		assertThat(x.subtract(expectedX).isAlmostZero()).isTrue();
	}

	@Test
	public void testMultiply() {
		for (int i = 1; i < 10; i++)
			assertThat(Matrix.identity(i).multiply(Matrix.identity(i))).isEqualTo(Matrix.identity(i));

		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };

		Matrix startMatrix = new Matrix(start);
		assertThat(Matrix.identity(startMatrix.rows).multiply(startMatrix)).isEqualTo(startMatrix);
		assertThat(startMatrix.multiply(Matrix.identity(startMatrix.rows))).isEqualTo(startMatrix);

		double[][] res = {
			{ 38, 14, 17, 20 },
			{ 98, 46, 57, 68 },
			{ 9, 18, 27, 36 },
			{ 0, 0, 0, 0 } };
		assertThat(startMatrix.multiply(startMatrix)).isEqualTo(new Matrix(res));
	}

	@Test
	public void testTranspose() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix startMatrix = new Matrix(start);
		double[][] end = {
			{ 1, 5, 9, 0 },
			{ 2, 6, 0, 0 },
			{ 3, 7, 0, 0 },
			{ 4, 8, 0, 0 } };
		Matrix endMatrix = new Matrix(end);
		assertThat(endMatrix.transpose()).isEqualTo(startMatrix);

		for (int i = 1; i < 10; i++)
			assertThat(Matrix.identity(i).transpose()).isEqualTo(Matrix.identity(i));
	}

	@Test
	public void testTransform() {
		assertThat(Matrix.identity(4).transform(new Vector3d(2, 3, 4))).isEqualTo(new Vector3d(2, 3, 4));
	}

	@Test
	public void testClone() {
		for (int i = 1; i < 10; i++)
			assertThat(Matrix.identity(i).clone()).isEqualTo(Matrix.identity(i));
	}

	@Test
	public void testUpdate() {
		Matrix startMatrix = new Matrix(
			new double[][] {
				{ 1, 2, 3, 4 },
				{ 5, 6, 7, 8 },
				{ 9, 0, 0, 0 },
				{ 0, 0, 0, 0 } }
		);
		Matrix endMatrix = startMatrix.clone();
		endMatrix.update(0, 0, 7);
		assertThat(startMatrix).isNotEqualTo(endMatrix);

		assertThat(endMatrix).isEqualTo(new Matrix(
			new double[][] {
				{ 7, 2, 3, 4 },
				{ 5, 6, 7, 8 },
				{ 9, 0, 0, 0 },
				{ 0, 0, 0, 0 } }
		));

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testIllegalArgument1() {
		new Matrix(new double[][] { { 1, 1, 2 }, { 1, 1 } });
	}

	@Test
	public void testEquals() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix firstMatrix = new Matrix(start);
		start[2][3] = 17;
		Matrix secondMatrix = new Matrix(start);
		assertThat(firstMatrix.equals(secondMatrix)).isFalse();
		assertThat(firstMatrix.equals(firstMatrix)).isTrue();
		assertThat(firstMatrix.equals("test")).isFalse();
	}
}
