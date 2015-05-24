package nova.core.util.transform;

import nova.core.util.transform.vector.Vector3d;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuaternionTest {

	Quaternion[] quaternions = new Quaternion[] {
		Quaternion.fromEuler(0, 0, 0),
		Quaternion.fromEuler(Math.PI / 2, 0, 0),
		Quaternion.fromEuler(0, Math.PI / 2, 0),
		Quaternion.fromEuler(0, 0, Math.PI / 2),
		Quaternion.fromEuler(Math.PI / 4, 0, 0),
		Quaternion.fromEuler(0, Math.PI / 4, 0),
		Quaternion.fromEuler(0, 0, Math.PI / 4)
	};

	@Test
	public void testEquality() {
		for (Quaternion quaternion : quaternions) {
			assertThat(quaternion).isEqualTo(quaternion);
		}
	}

	@Test
	public void testTwoWay() {
		for (Quaternion in : quaternions) {
			Quaternion out = Quaternion.fromEuler(in.toEuler());
			assertThat(out).isEqualTo(in);
		}
	}

	@Test
	public void testNoTransform() {
		assertThat(Quaternion.fromEuler(0, 0, 0).transform(Vector3d.one)).isEqualTo(Vector3d.one);

		for (Quaternion quaternion : quaternions) {
			assertThat(quaternion.transform(Vector3d.zero)).isEqualTo(Vector3d.zero);
		}
	}

	@Test
	public void testTransform() {
		Quaternion q0 = Quaternion.fromEuler(0, 0, 0);
		assertThat(q0.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(1, 0, 0));
		assertThat(q0.transform(new Vector3d(1, 1, 0))).isEqualTo(new Vector3d(1, 1, 0));
		assertThat(q0.transform(new Vector3d(1, 0, 1))).isEqualTo(new Vector3d(1, 0, 1));

		Quaternion q = Quaternion.fromEulerDegree(90, 0, 0);
		assertThat(q.transform(new Vector3d(0, 0, 1))).isEqualTo(new Vector3d(1, 0, 0));
		assertThat(q.transform(new Vector3d(0, 0, -1))).isEqualTo(new Vector3d(-1, 0, 0));
		assertThat(q.transform(new Vector3d(-1, 0, 0))).isEqualTo(new Vector3d(0, 0, 1));
		assertThat(q.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(0, 0, -1));

		assertThat(q.transform(new Vector3d(0, 0, 0.5))).isEqualTo(new Vector3d(0.5, 0, 0));
		assertThat(q.transform(new Vector3d(1, 0, 1))).isEqualTo(new Vector3d(1, 0, -1));

		q = Quaternion.fromEulerDegree(-90, 0, 0);

		assertThat(q.transform(new Vector3d(0, 0, 1))).isEqualTo(new Vector3d(-1, 0, 0));
		assertThat(q.transform(new Vector3d(0, 0, -1))).isEqualTo(new Vector3d(1, 0, 0));
		assertThat(q.transform(new Vector3d(-1, 0, 0))).isEqualTo(new Vector3d(0, 0, -1));
		assertThat(q.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(0, 0, 1));

		assertThat(q.transform(new Vector3d(0, 0, 0.5))).isEqualTo(new Vector3d(-0.5, 0, 0));
		assertThat(q.transform(new Vector3d(1, 0, 1))).isEqualTo(new Vector3d(-1, 0, 1));

		q = Quaternion.fromEulerDegree(180, 0, 0);

		assertThat(q.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(-1, 0, 0));
		assertThat(q.transform(new Vector3d(-1, 0, 0))).isEqualTo(new Vector3d(1, 0, 0));
		assertThat(q.transform(new Vector3d(0, 0, 1))).isEqualTo(new Vector3d(0, 0, -1));
		assertThat(q.transform(new Vector3d(0, 0, -1))).isEqualTo(new Vector3d(0, 0, 1));

		assertThat(q.transform(new Vector3d(0, 0, 0.5))).isEqualTo(new Vector3d(0, 0, -0.5));
		assertThat(q.transform(new Vector3d(1, 0, 1))).isEqualTo(new Vector3d(-1, 0, -1));

		q = Quaternion.fromEulerDegree(360, 0, 0);

		assertThat(q.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(1, 0, 0));
		assertThat(q.transform(new Vector3d(-1, 0, 0))).isEqualTo(new Vector3d(-1, 0, 0));
		assertThat(q.transform(new Vector3d(0, 0, 1))).isEqualTo(new Vector3d(0, 0, 1));
		assertThat(q.transform(new Vector3d(0, 0, -1))).isEqualTo(new Vector3d(0, 0, -1));

		assertThat(q.transform(new Vector3d(0, 0, 0.5))).isEqualTo(new Vector3d(0, 0, 0.5));
		assertThat(q.transform(new Vector3d(1, 0, 1))).isEqualTo(new Vector3d(1, 0, 1));

		q = Quaternion.fromEulerDegree(0, 90, 0);
		assertThat(q.transform(new Vector3d(0, 0, 1))).isEqualTo(new Vector3d(0, 0, 1));
		assertThat(q.transform(new Vector3d(0, -1, 0))).isEqualTo(new Vector3d(1, 0, 0));

		q = Quaternion.fromEulerDegree(90, 90, 0);
		assertThat(q.transform(new Vector3d(0, 1, 0))).isEqualTo(new Vector3d(0, 0, 1));
		assertThat(q.transform(new Vector3d(1, 0, 0))).isEqualTo(new Vector3d(0, 1, 0));

	}
}
