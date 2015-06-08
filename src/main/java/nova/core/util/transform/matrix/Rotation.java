/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 ChickenBones
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package nova.core.util.transform.matrix;

import com.google.common.math.DoubleMath;
import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.collection.Tuple2;
import nova.core.util.transform.vector.Transformer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * All rotation operations operate in radians.
 *
 * @author Calclavia, ChickenBones
 */
public class Rotation implements Transformer, Storable {

	public static final Rotation identity = new Rotation(0, 0, 0, 1);

	@Store
	public final double x, y, z, w;

	public Rotation(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * The euler angles describe a 3D rotation. The rotation always in radians.
	 *
	 * Conventions:
	 *
	 * Yaw:
	 * 0 Degrees - Looking at NORTH
	 * 90 - Looking at WEST
	 * 180 - Looking at SOUTH
	 * 270 - Looking at EAST
	 *
	 * Pitch:
	 * 0 Degrees - Looking straight forward towards the horizon.
	 * 90 Degrees - Looking straight up to the sky
	 * -90 Degrees - Looking straight down to the void.
	 *
	 * Yaw - Rotation around y-axis (heading)
	 * Pitch - Rotation around x-axis (bank)
	 * Roll - Rotation around z-axis (attitude)
	 *
	 * @param euler input {@link Vector3D}
	 * @return resulting {@link Rotation}
	 * @author Calclavia
	 */
	public static Rotation fromEuler(Vector3D euler) {
		return fromEuler(euler.getX(), euler.getY(), euler.getZ());
	}

	public static Rotation fromEuler(double yaw, double pitch) {
		return fromEuler(yaw, pitch, 0);
	}

	/**
	 * Calculates a Quaternion from a direction vector.
	 * Note this rotation is NOT relative, and may not be appropriate for all directions
	 *
	 * @param direction A unit vector
	 * @return The quaternion
	 */
	public static Rotation fromDirection(Vector3D direction) {
		double yaw = Math.atan2(direction.getZ(), direction.getX());
		double pitch = Math.asin(direction.getY());
		return fromEuler(yaw, pitch);
	}

	public static Rotation fromEuler(double yaw, double pitch, double roll) {
		// Assuming the angles are in radians.
		double c1 = Math.cos(yaw / 2);
		double s1 = Math.sin(yaw / 2);
		double c2 = Math.cos(roll / 2);
		double s2 = Math.sin(roll / 2);
		double c3 = Math.cos(pitch / 2);
		double s3 = Math.sin(pitch / 2);
		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;
		double w = c1c2 * c3 - s1s2 * s3;
		double x = c1c2 * s3 + s1s2 * c3;
		double y = s1 * c2 * c3 + c1 * s2 * s3;
		double z = c1 * s2 * c3 - s1 * c2 * s3;
		return new Rotation(x, y, z, w);
	}

	public static Rotation fromEulerDegree(double yaw, double pitch, double roll) {
		return fromEuler(Math.toRadians(yaw), Math.toRadians(pitch), Math.toRadians(roll));
	}

	/**
	 * Returns a quaternion from Angle Axis rotation.
	 *
	 * @param axis Axis {@link Vector3D}
	 * @param angle Angle in radians
	 * @return The Quaternion representation of the angle axis rotation.
	 */
	public static Rotation fromAxis(Vector3D axis, double angle) {
		return fromAxis(axis.getX(), axis.getY(), axis.getZ(), angle);
	}

	public static Rotation fromAxis(double ax, double ay, double az, double angle) {
		angle *= 0.5;
		double d4 = Math.sin(angle);
		return new Rotation(ax * d4, ay * d4, az * d4, Math.cos(angle));
	}

	public Rotation multiply(Rotation q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w - y * q.z + z * q.y;
		double d2 = w * q.y + x * q.z + y * q.w - z * q.x;
		double d3 = w * q.z - x * q.y + y * q.x + z * q.w;
		return new Rotation(d1, d2, d3, d);
	}

	/**
	 * Use right crossProduct for compound operations.
	 *
	 * We can for pure rotations, for instance if the first rotation is q1 we have:
	 *
	 * x2 = q1 * x * q1'
	 *
	 * we now apply a second rotation q2 to x2 giving:
	 *
	 * x3 = q2 * (q1 * x * q1') * q2'
	 *
	 * using the associative property of quaternions and the fact that (q2*q1)'=q1'* q2' (see conjugate function) then we get:
	 *
	 * x3 = (q2*q1) * x * (q2*q1)'
	 *
	 * @param q - The quaternion to crossProduct with
	 * @return The new Quaternion
	 */
	public Rotation rightMultiply(Rotation q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w + y * q.z - z * q.y;
		double d2 = w * q.y - x * q.z + y * q.w + z * q.x;
		double d3 = w * q.z + x * q.y - y * q.x + z * q.w;
		return new Rotation(d1, d2, d3, d);
	}

	public Rotation $times(Rotation q) {
		return multiply(q);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Rotation scale(double factor) {
		return new Rotation(x * factor, y * factor, z * factor, w * factor);
	}

	public Rotation normalize() {
		double d = magnitude();
		return new Rotation(x / d, y / d, z / d, w / d);
	}

	@Override
	public Vector3D apply(Vector3D vec) {
		double vX = w * w * vec.getX() + 2 * y * w * vec.getZ() - 2 * z * w * vec.getY() + x * x * vec.getX() + 2 * y * x * vec.getY() + 2 * z * x * vec.getZ() - z * z * vec.getX() - y * y * vec.getX();
		double vY = 2 * x * y * vec.getX() + y * y * vec.getY() + 2 * z * y * vec.getZ() + 2 * w * z * vec.getX() - z * z * vec.getY() + w * w * vec.getY() - 2 * x * w * vec.getZ() - x * x * vec.getY();
		double vZ = 2 * x * z * vec.getX() + 2 * y * z * vec.getY() + z * z * vec.getZ() - 2 * w * y * vec.getX() - y * y * vec.getZ() + 2 * w * x * vec.getY() - x * x * vec.getZ() + w * w * vec.getZ();
		return new Vector3D(vX, vY, vZ);
	}

	public Vector3D toEuler() {
		double sqw = w * w;
		double sqx = x * x;
		double sqy = y * y;
		double sqz = z * z;
		double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
		double test = x * y + z * w;

		// singularity at north pole
		if (test > 0.499 * unit) {
			return new Vector3D(2 * Math.atan2(x, w), 0, Math.PI / 2);
		}

		// singularity at south pole
		if (test < -0.499 * unit) {
			return new Vector3D(-2 * Math.atan2(x, w), 0, -Math.PI / 2);
		}
		return new Vector3D(Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw), Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw), Math.asin(2 * test / unit));
	}

	public Tuple2<Vector3D, Double> toAngleAxis() {
		Rotation normalQuat = this;

		// if w>1 acos and sqrt will produce errors, this cant happen if quaternion is normalised
		if (normalQuat.w > 1) {
			normalQuat = normalize();
		}

		double angle = 2 * Math.acos(normalQuat.w);

		// Assuming quaternion normalised then w is less than 1, so term always positive.
		double s = Math.sqrt(1 - normalQuat.w * normalQuat.w);
		if (s < 0.001) {
			// test to avoid divide by zero, s is always positive due to sqrt
			// if s close to zero then direction of axis not important
			// if it is important that axis is normalised then replace with x=1; y=z=0;
			return new Tuple2<>(new Vector3D(normalQuat.x, normalQuat.y, normalQuat.z), 2 * Math.acos(w));
		} else {
			return new Tuple2<>(new Vector3D(normalQuat.x / s, normalQuat.y / s, normalQuat.z / s), 2 * Math.acos(w));
		}

	}

	/**
	 * @return A direction vector representing the rotation.
	 */
	public Vector3D toXVector() {
		return apply(Vector3D.PLUS_I);
	}

	/**
	 * @return A direction vector representing the rotation.
	 */
	public Vector3D toYVector() {
		return apply(Vector3D.PLUS_J);
	}

	/**
	 * @return A direction vector representing the rotation.
	 */
	public Vector3D toZVector() {
		return apply(Vector3D.PLUS_K);
	}

	/**
	 * The default forward for games
	 *
	 * @return A direction vector representing the rotation.
	 */
	public Vector3D toForwardVector() {
		return apply(Vector3D.PLUS_K.negate());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rotation) {
			Rotation other = ((Rotation) obj);
			return DoubleMath.fuzzyEquals(this.x, other.x, 0.000001) && DoubleMath.fuzzyEquals(this.y, other.y, 0.000001) && DoubleMath.fuzzyEquals(this.z, other.z, 0.000001) && DoubleMath.fuzzyEquals(this.w, other.w, 0.000001);
		}
		return this == obj;
	}

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quaternion[" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ", " + new BigDecimal(w, cont) + "]";
	}
}
