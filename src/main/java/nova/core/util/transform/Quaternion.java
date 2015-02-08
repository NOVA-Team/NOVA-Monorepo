/**
 The MIT License (MIT)

 Copyright (c) 2014 ChickenBones

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE. 
 */

package nova.core.util.transform;

import com.google.common.math.DoubleMath;
import nova.core.util.collection.Pair;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * All rotation operations operate in radians.
 * @author Calclavia, ChickenBones
 */
public class Quaternion implements Transform {

	public static final Quaternion identity = new Quaternion(0, 0, 0, 1);

	public final double x;
	public final double y;
	public final double z;
	public final double w;

	public Quaternion(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * The euler angles describe a 3D rotation. The rotation always in radians.
	 *
	 * Note: The rotational system Minecraft uses is non-standard. The angles and vector calculations
	 * have been calibrated to match. DEFINITIONS:
	 *
	 * Yaw: 0 Degrees - Looking at NORTH 90 - Looking at WEST 180 - Looking at SOUTH 270 - Looking at
	 * EAST
	 *
	 * Pitch: 0 Degrees - Looking straight forward towards the horizon. 90 Degrees - Looking straight up
	 * to the sky. -90 Degrees - Looking straight down to the void.
	 *
	 * Make sure all models use the Techne Model loader, they will naturally follow this rule.
	 * @param euler input {@link Vector3}
	 * @return resulting {@link Quaternion}
	 * @author Calclavia
	 */
	public static Quaternion fromEuler(Vector3<?> euler) {
		return fromEuler(euler.xd(), euler.yd(), euler.zd());
	}

	public static Quaternion fromEuler(double yaw, double pitch, double roll) {
		// Assuming the angles are in radians.
		double c1 = Math.cos(yaw / 2);
		double s1 = Math.sin(yaw / 2);
		double c2 = Math.cos(pitch / 2);
		double s2 = Math.sin(pitch / 2);
		double c3 = Math.cos(roll / 2);
		double s3 = Math.sin(roll / 2);
		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;
		double w = c1c2 * c3 - s1s2 * s3;
		double x = c1c2 * s3 + s1s2 * c3;
		double y = s1 * c2 * c3 + c1 * s2 * s3;
		double z = c1 * s2 * c3 - s1 * c2 * s3;
		return new Quaternion(x, y, z, w);
	}

	public static Quaternion fromEulerDegree(double yaw, double pitch, double roll) {
		return fromEuler(Math.toRadians(yaw), Math.toRadians(pitch), Math.toRadians(roll));
	}

	/**
	 * Returns a quaternion from Angle Axis rotation.
	 * @param axis Axis {@link Vector3}
	 * @param angle Angle
	 * @return The Quaternion representation of the angle axis rotation.
	 */
	public static Quaternion fromAxis(Vector3<?> axis, double angle) {
		return fromAxis(axis.xd(), axis.yd(), axis.zd(), angle);
	}

	public static Quaternion fromAxis(double ax, double ay, double az, double angle) {
		angle *= 0.5;
		double d4 = Math.sin(angle);
		return new Quaternion(ax * d4, ay * d4, az * d4, Math.cos(angle));
	}

	public Quaternion multiply(Quaternion q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w - y * q.z + z * q.y;
		double d2 = w * q.y + x * q.z + y * q.w - z * q.x;
		double d3 = w * q.z - x * q.y + y * q.x + z * q.w;
		return new Quaternion(d1, d2, d3, d);
	}

	/**
	 * Use right multiply for compound operations.
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
	 * @param q - The quaternion to multiply with
	 * @return The new Quaternion
	 */
	public Quaternion rightMultiply(Quaternion q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w + y * q.z - z * q.y;
		double d2 = w * q.y - x * q.z + y * q.w + z * q.x;
		double d3 = w * q.z + x * q.y - y * q.x + z * q.w;
		return new Quaternion(d1, d2, d3, d);
	}

	public Quaternion $times(Quaternion q) {
		return multiply(q);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize() {
		double d = magnitude();
		return new Quaternion(x / d, y / d, z / d, w / d);
	}

	@Override
	public Vector3d transform(Vector3<?> vec) {
		double vX = w * w * vec.xd() + 2 * y * w * vec.zd() - 2 * z * w * vec.yd() + x * x * vec.xd() + 2 * y * x * vec.yd() + 2 * z * x * vec.zd() - z * z * vec.xd() - y * y * vec.xd();
		double vY = 2 * x * y * vec.xd() + y * y * vec.yd() + 2 * z * y * vec.zd() + 2 * w * z * vec.xd() - z * z * vec.yd() + w * w * vec.yd() - 2 * x * w * vec.zd() - x * x * vec.yd();
		double vZ = 2 * x * z * vec.xd() + 2 * y * z * vec.yd() + z * z * vec.zd() - 2 * w * y * vec.xd() - y * y * vec.zd() + 2 * w * x * vec.yd() - x * x * vec.zd() + w * w * vec.zd();
		return new Vector3d(vX, vY, vZ);
	}

	public Vector3d toEuler() {
		double sqw = w * w;
		double sqx = x * x;
		double sqy = y * y;
		double sqz = z * z;
		double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
		double test = x * y + z * w;

		// singularity at north pole
		if (test > 0.499 * unit) {
			return new Vector3d(2 * Math.atan2(x, w), Math.PI / 2, 0);
		}

		// singularity at south pole
		if (test < -0.499 * unit) {
			return new Vector3d(-2 * Math.atan2(x, w), -Math.PI / 2, 0);
		}
		return new Vector3d(Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw), Math.asin(2 * test / unit), Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw));
	}

	public Pair<Vector3d, Double> toAngleAxis() {
		return new Pair<>(new Vector3d(x / Math.sqrt(1 - w * w), y / Math.sqrt(1 - w * w), z / Math.sqrt(1 - w * w)), 2 * Math.acos(w));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quaternion) {
			Quaternion other = ((Quaternion) obj);
			return DoubleMath.fuzzyEquals(this.x, other.x, 0.000001) && DoubleMath.fuzzyEquals(this.y, other.y, 0.000001) && DoubleMath.fuzzyEquals(this.z, other.z, 0.000001) && DoubleMath.fuzzyEquals(this.w, other.w, 0.000001);
		}
		return this == obj;
	}

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quaternion[" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ", " + new BigDecimal(w, cont) + "]";
	}
}
