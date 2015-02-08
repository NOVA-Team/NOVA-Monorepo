package nova.core.util.transform;

import junit.framework.TestCase;

public class QuaternionTest extends TestCase {

	Quaternion[] quaternions = new Quaternion[] {
		Quaternion.fromEuler(0, 0, 0),
		Quaternion.fromEuler(Math.PI / 2, 0, 0),
		Quaternion.fromEuler(0, Math.PI / 2, 0),
		Quaternion.fromEuler(0, 0, Math.PI / 2),
		Quaternion.fromEuler(Math.PI / 4, 0, 0),
		Quaternion.fromEuler(0, Math.PI / 4, 0),
		Quaternion.fromEuler(0, 0, Math.PI / 4)
	};

	public void testEquality(){
		for (Quaternion quaternion: quaternions){
			assertEquals(quaternion,quaternion);
		}
	}


	public void testTwoWay() {
		for (Quaternion in : quaternions) {
			Quaternion out = Quaternion.fromEuler(in.toEuler());
			assertEquals(in,out);
		}
	}

	public void testNoTransform() {
		assertEquals(Quaternion.fromEuler(0, 0, 0).transform(Vector3d.one), Vector3d.one);

		for (Quaternion quaternion : quaternions) {
			assertEquals(Vector3d.zero,quaternion.transform(Vector3d.zero));
		}
	}

	public void testTransform() {
		Quaternion q0 = Quaternion.fromEuler(0, 0, 0);
		assertEquals(new Vector3d(1, 0, 0), q0.transform(new Vector3d(1, 0, 0)));
		assertEquals(new Vector3d(1, 1, 0), q0.transform(new Vector3d(1, 1, 0)));
		assertEquals(new Vector3d(1, 0, 1), q0.transform(new Vector3d(1, 0, 1)));

		Quaternion q = Quaternion.fromEulerDegree(90,0,0);
		assertEquals(new Vector3d(1, 0, 0), q.transform(new Vector3d(0, 0, 1)));
		assertEquals(new Vector3d(-1, 0, 0), q.transform(new Vector3d(0, 0, -1)));
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(-1, 0, 0)));
		assertEquals(new Vector3d(0, 0, -1), q.transform(new Vector3d(1, 0, 0)));

		assertEquals(new Vector3d(0.5, 0, 0), q.transform(new Vector3d(0, 0, 0.5)));
		assertEquals(new Vector3d(1, 0, -1), q.transform(new Vector3d(1, 0, 1)));

		q = Quaternion.fromEulerDegree(-90,0,0);

		assertEquals(new Vector3d(-1, 0, 0), q.transform(new Vector3d(0, 0, 1)));
		assertEquals(new Vector3d(1, 0, 0), q.transform(new Vector3d(0, 0, -1)));
		assertEquals(new Vector3d(0, 0, -1), q.transform(new Vector3d(-1, 0, 0)));
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(1, 0, 0)));

		assertEquals(new Vector3d(-0.5, 0, 0), q.transform(new Vector3d(0, 0, 0.5)));
		assertEquals(new Vector3d(-1, 0, 1), q.transform(new Vector3d(1, 0, 1)));

		q = Quaternion.fromEulerDegree(180,0,0);

		assertEquals(new Vector3d(-1, 0, 0), q.transform(new Vector3d(1, 0, 0)));
		assertEquals(new Vector3d(1, 0, 0), q.transform(new Vector3d(-1, 0, 0)));
		assertEquals(new Vector3d(0, 0, -1), q.transform(new Vector3d(0, 0, 1)));
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(0, 0, -1)));

		assertEquals(new Vector3d(0, 0, -0.5), q.transform(new Vector3d(0, 0, 0.5)));
		assertEquals(new Vector3d(-1, 0, -1), q.transform(new Vector3d(1, 0, 1)));

		q = Quaternion.fromEulerDegree(360,0,0);

		assertEquals(new Vector3d(1, 0, 0), q.transform(new Vector3d(1, 0, 0)));
		assertEquals(new Vector3d(-1, 0, 0), q.transform(new Vector3d(-1, 0, 0)));
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(0, 0, 1)));
		assertEquals(new Vector3d(0, 0, -1), q.transform(new Vector3d(0, 0, -1)));

		assertEquals(new Vector3d(0, 0, 0.5), q.transform(new Vector3d(0, 0, 0.5)));
		assertEquals(new Vector3d(1, 0, 1), q.transform(new Vector3d(1, 0, 1)));

		q = Quaternion.fromEulerDegree(0,90,0);
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(0, 0, 1)));
		assertEquals(new Vector3d(1, 0, 0), q.transform(new Vector3d(0, -1, 0)));

		q = Quaternion.fromEulerDegree(90,90,0);
		assertEquals(new Vector3d(0, 0, 1), q.transform(new Vector3d(0, 1, 0)));
		assertEquals(new Vector3d(0, 1, 0), q.transform(new Vector3d(1, 0, 0)));

	}
}