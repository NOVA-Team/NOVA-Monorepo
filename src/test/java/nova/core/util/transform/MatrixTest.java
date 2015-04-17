package nova.core.util.transform;

import org.junit.Test;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@Ignore
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
		assertNotSame(startMatrix, endMatrix);
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
		assertEquals(startMatrix.swap(0, 1), endMatrix);
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

		assertEquals(start.add(start), end);
		assertEquals(start.multiply(2), end);
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

		assertEquals(start.augment(new Vector2d(3, 3).toMatrix()), end);
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

		assertEquals(sub, start.submatrix(0, 1, 0, 1));
	}

	@Test
	public void testRref() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, 3 },
				{ -5, 0 } }
		);

		assertEquals(start.rref(), Matrix.identity(2));
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
		assertTrue(inverse.subtract(reciprocal).isAlmostZero());
	}

	@Test
	public void testDeterminant() {
		Matrix start = new Matrix(
			new double[][] {
				{ 1, -3, 0 },
				{ -2, 4, 1 },
				{ 5, -2, 2 } }
		);

		assertEquals(-17, start.determinant(), 0.0001);
	}

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
		assertTrue(x.subtract(expectedX).isAlmostZero());
	}

	@Test
	public void testMultiply() {
		for (int i = 1; i < 10; i++)
			assertEquals(Matrix.identity(i), Matrix.identity(i).multiply(Matrix.identity(i)));

		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };

		Matrix startMatrix = new Matrix(start);
		assertEquals(startMatrix, Matrix.identity(startMatrix.rows).multiply(startMatrix));
		assertEquals(startMatrix, startMatrix.multiply(Matrix.identity(startMatrix.rows)));

		double[][] res = {
			{ 38, 14, 17, 20 },
			{ 98, 46, 57, 68 },
			{ 9, 18, 27, 36 },
			{ 0, 0, 0, 0 } };
		assertEquals(new Matrix(res), startMatrix.multiply(startMatrix));
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
		assertEquals(startMatrix, endMatrix.transpose());

		for (int i = 1; i < 10; i++)
			assertEquals(Matrix.identity(i), Matrix.identity(i).transpose());
	}

	@Test
	public void testTransform() {
		assertEquals(new Vector3d(2, 3, 4), Matrix.identity(4).transform(new Vector3d(2, 3, 4)));
	}

	@Test
	public void testClone() {
		for (int i = 1; i < 10; i++)
			assertEquals(Matrix.identity(i), Matrix.identity(i).clone());
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
		assertNotEquals(startMatrix, endMatrix);

		assertEquals(endMatrix, new Matrix(
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
		assertFalse(firstMatrix.equals(secondMatrix));
		assertTrue(firstMatrix.equals(firstMatrix));
		assertFalse(firstMatrix.equals("test"));
	}
}