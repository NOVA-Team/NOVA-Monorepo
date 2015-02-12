package nova.core.util.transform;

import org.junit.Test;

import static org.junit.Assert.*;

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
		assertNotSame(startMatrix, endMatrix);

	}

	@Test
	public void testMultiply() {
		assertEquals(Matrix4x4.IDENTITY, Matrix4x4.IDENTITY.multiply(Matrix4x4.IDENTITY));
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 startMatrix = new Matrix4x4(start);
		assertEquals(startMatrix, Matrix4x4.IDENTITY.multiply(startMatrix));
		assertEquals(startMatrix, startMatrix.multiply(Matrix4x4.IDENTITY));

		double[][] res = {
			{ 38, 14, 17, 20 },
			{ 98, 46, 57, 68 },
			{ 9, 18, 27, 36 },
			{ 0, 0, 0, 0 } };
		assertEquals(new Matrix4x4(res), startMatrix.multiply(startMatrix));

		assertEquals(new Matrix4x4(res), startMatrix.rightlyMultiply(startMatrix));
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
		assertEquals(startMatrix, endMatrix.transpose());

		assertEquals(Matrix4x4.IDENTITY, Matrix4x4.IDENTITY.transpose());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultCtor() {
		assertEquals(Matrix4x4.IDENTITY, new Matrix4x4());
	}

	@Test
	public void testTransform() {
		assertEquals(new Vector3d(2, 3, 4), Matrix4x4.IDENTITY.transform(new Vector3d(2, 3, 4)));
	}

	@Test
	public void testClone() {
		assertEquals(Matrix4x4.IDENTITY, Matrix4x4.IDENTITY.clone());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument1() {
		new Matrix4x4(new double[][]{{1,1},{1,1}});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument2() {
		new Matrix4x4(new double[][]{{1,1},{1,1},{1,1},{1,1}});
	}

	public void testEquals() {
		double[][] start = {
			{ 1, 2, 3, 4 },
			{ 5, 6, 7, 8 },
			{ 9, 0, 0, 0 },
			{ 0, 0, 0, 0 } };
		Matrix4x4 firstMatrix = new Matrix4x4(start);
		start[2][3] = 17;
		Matrix4x4 secondMatrix = new Matrix4x4(start);
		assertFalse(firstMatrix.equals(secondMatrix));
		assertTrue(firstMatrix.equals(firstMatrix));
		assertFalse(firstMatrix.equals("test"));
	}
}