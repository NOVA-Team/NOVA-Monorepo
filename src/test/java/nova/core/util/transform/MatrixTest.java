package nova.core.util.transform;

import org.junit.Test;

import static org.junit.Assert.*;

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
	public void testMultiply() {
		assertEquals(Matrix.IDENTITY, Matrix.IDENTITY.multiply(Matrix.IDENTITY));
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix startMatrix = new Matrix(start);
		assertEquals(startMatrix, Matrix.IDENTITY.multiply(startMatrix));
		assertEquals(startMatrix, startMatrix.multiply(Matrix.IDENTITY));

		double[][] res = {
			{ 38, 14, 17, 20 },
			{ 98, 46, 57, 68 },
			{ 9, 18, 27, 36 },
			{ 0, 0, 0, 0 } };
		assertEquals(new Matrix(res), startMatrix.multiply(startMatrix));

		assertEquals(new Matrix(res), startMatrix.rightlyMultiply(startMatrix));
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

		assertEquals(Matrix.IDENTITY, Matrix.IDENTITY.transpose());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultCtor() {
		assertEquals(Matrix.IDENTITY, new Matrix());
	}

	@Test
	public void testTransform() {
		assertEquals(new Vector3d(2, 3, 4), Matrix.IDENTITY.transform(new Vector3d(2, 3, 4)));
	}

	@Test
	public void testClone() {
		assertEquals(Matrix.IDENTITY, Matrix.IDENTITY.clone());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument1() {
		new Matrix(new double[][]{{1,1},{1,1}});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument2() {
		new Matrix(new double[][]{{1,1},{1,1},{1,1},{1,1}});
	}

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