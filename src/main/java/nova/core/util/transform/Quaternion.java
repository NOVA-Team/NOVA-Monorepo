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

	public Quaternion(Quaternion Quaternion) {
		x = Quaternion.x;
		y = Quaternion.y;
		z = Quaternion.z;
		w = Quaternion.w;
	}

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
		return fromEuler(euler);
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
		//TODO: Check this
		double d = -x * vec.xd() - y * vec.yd() - z * vec.zd();
		double d1 = w * vec.xd() + y * vec.zd() - z * vec.yd();
		double d2 = w * vec.yd() - x * vec.zd() + z * vec.xd();
		double d3 = w * vec.zd() + x * vec.yd() - y * vec.xd();
		return new Vector3d(d1 * w - d * x - d2 * z + d3 * y, d2 * w - d * y + d1 * z - d3 * x, d3 * w - d * z - d1 * y + d2 * x);
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

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quaternion[" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ", " + new BigDecimal(w, cont) + "]";
	}
}
