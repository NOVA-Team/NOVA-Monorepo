package nova.core.util.transform;

import nova.core.util.math.VectorUtil;
import nova.core.util.transform.matrix.Quaternion;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
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
		assertThat(Quaternion.fromEuler(0, 0, 0).apply(VectorUtil.ONE)).isEqualTo(VectorUtil.ONE);

		for (Quaternion quaternion : quaternions) {
			assertThat(quaternion.apply(Vector3D.ZERO)).isEqualTo(Vector3D.ZERO);
		}
	}

	@Test
	public void testEulerToVector() {
		Quaternion rot = Quaternion.fromEuler(0, 0, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_K.negate());

		rot = Quaternion.fromEuler(Math.PI / 2, 0, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_I.negate());

		rot = Quaternion.fromEuler(-Math.PI / 2, 0, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_I);

		rot = Quaternion.fromEuler(Math.PI, 0, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_K);

		rot = Quaternion.fromEuler(0, Math.PI / 2, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_J);

		rot = Quaternion.fromEuler(0, 0, Math.PI / 2);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_K.negate());

		rot = Quaternion.fromEuler(Math.PI / 2, Math.PI / 2, 0);
		assertThat(rot.toForwardVector()).isEqualTo(Vector3D.PLUS_J);
	}

	@Test
	public void testTransform() {
		Quaternion q0 = Quaternion.fromEuler(0, 0, 0);
		assertThat(q0.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q0.apply(new Vector3D(1, 1, 0))).isEqualTo(new Vector3D(1, 1, 0));
		assertThat(q0.apply(new Vector3D(1, 0, 1))).isEqualTo(new Vector3D(1, 0, 1));

		Quaternion q = Quaternion.fromEulerDegree(90, 0, 0);
		assertThat(q.apply(new Vector3D(0, 0, 1))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q.apply(new Vector3D(0, 0, -1))).isEqualTo(new Vector3D(-1, 0, 0));
		assertThat(q.apply(new Vector3D(-1, 0, 0))).isEqualTo(new Vector3D(0, 0, 1));
		assertThat(q.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(0, 0, -1));

		assertThat(q.apply(new Vector3D(0, 0, 0.5))).isEqualTo(new Vector3D(0.5, 0, 0));
		assertThat(q.apply(new Vector3D(1, 0, 1))).isEqualTo(new Vector3D(1, 0, -1));

		q = Quaternion.fromEulerDegree(-90, 0, 0);

		assertThat(q.apply(new Vector3D(0, 0, 1))).isEqualTo(new Vector3D(-1, 0, 0));
		assertThat(q.apply(new Vector3D(0, 0, -1))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q.apply(new Vector3D(-1, 0, 0))).isEqualTo(new Vector3D(0, 0, -1));
		assertThat(q.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(0, 0, 1));

		assertThat(q.apply(new Vector3D(0, 0, 0.5))).isEqualTo(new Vector3D(-0.5, 0, 0));
		assertThat(q.apply(new Vector3D(1, 0, 1))).isEqualTo(new Vector3D(-1, 0, 1));

		q = Quaternion.fromEulerDegree(180, 0, 0);

		assertThat(q.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(-1, 0, 0));
		assertThat(q.apply(new Vector3D(-1, 0, 0))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q.apply(new Vector3D(0, 0, 1))).isEqualTo(new Vector3D(0, 0, -1));
		assertThat(q.apply(new Vector3D(0, 0, -1))).isEqualTo(new Vector3D(0, 0, 1));

		assertThat(q.apply(new Vector3D(0, 0, 0.5))).isEqualTo(new Vector3D(0, 0, -0.5));
		assertThat(q.apply(new Vector3D(1, 0, 1))).isEqualTo(new Vector3D(-1, 0, -1));

		q = Quaternion.fromEulerDegree(360, 0, 0);

		assertThat(q.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q.apply(new Vector3D(-1, 0, 0))).isEqualTo(new Vector3D(-1, 0, 0));
		assertThat(q.apply(new Vector3D(0, 0, 1))).isEqualTo(new Vector3D(0, 0, 1));
		assertThat(q.apply(new Vector3D(0, 0, -1))).isEqualTo(new Vector3D(0, 0, -1));

		assertThat(q.apply(new Vector3D(0, 0, 0.5))).isEqualTo(new Vector3D(0, 0, 0.5));
		assertThat(q.apply(new Vector3D(1, 0, 1))).isEqualTo(new Vector3D(1, 0, 1));

		q = Quaternion.fromEulerDegree(0, 90, 0);
		assertThat(q.apply(new Vector3D(0, 0, 1))).isEqualTo(new Vector3D(0, -1, 0));
		assertThat(q.apply(new Vector3D(0, -1, 0))).isEqualTo(new Vector3D(0, 0, -1));

		q = Quaternion.fromEulerDegree(90, 90, 0);
		assertThat(q.apply(new Vector3D(0, 1, 0))).isEqualTo(new Vector3D(1, 0, 0));
		assertThat(q.apply(new Vector3D(1, 0, 0))).isEqualTo(new Vector3D(0, 0, -1));

	}
}
