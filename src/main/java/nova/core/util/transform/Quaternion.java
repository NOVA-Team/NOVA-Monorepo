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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * All rotation operations operate in radians.
 * @author Calclavia, ChickenBones
 */
public class Quaternion {

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

	public Quaternion(double d, double d1, double d2, double d3) {
		x = d1;
		y = d2;
		z = d3;
		w = d;
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
	 *
	 * @author Calclavia
	 */
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
		return new Quaternion(w, x, y, z);
	}

	public static Quaternion fromAxis(double ax, double ay, double az, double angle) {
		angle *= 0.5;
		double d4 = Math.sin(angle);
		return new Quaternion(Math.cos(angle), ax * d4, ay * d4, az * d4);
	}

	public static Quaternion fromAxis(Vector3<?> axis, double angle) {
		return fromAxis(axis.xd(), axis.yd(), axis.zd(), angle);
	}

	public Quaternion multiply(Quaternion q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w - y * q.z + z * q.y;
		double d2 = w * q.y + x * q.z + y * q.w - z * q.x;
		double d3 = w * q.z - x * q.y + y * q.x + z * q.w;
		return new Quaternion(d, d1, d2, d3);
	}

	public Quaternion rightMultiply(Quaternion q) {
		double d = w * q.w - x * q.x - y * q.y - z * q.z;
		double d1 = w * q.x + x * q.w + y * q.z - z * q.y;
		double d2 = w * q.y - x * q.z + y * q.w + z * q.x;
		double d3 = w * q.z + x * q.y - y * q.x + z * q.w;
		return new Quaternion(d, d1, d2, d3);
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

	public Vector3d rotate(Vector3<?> vec) {
		double d = -x * vec.xd() - y * vec.yd() - z * vec.zd();
		double d1 = w * vec.xd() + y * vec.zd() - z * vec.yd();
		double d2 = w * vec.yd() - x * vec.zd() + z * vec.xd();
		double d3 = w * vec.zd() + x * vec.yd() - y * vec.xd();
		return new Vector3d(d1 * w - d * x - d2 * z + d3 * y, d2 * w - d * y + d1 * z - d3 * x, d3 * w - d * z - d1 * y + d2 * x);
	}

	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quaternion[" + new BigDecimal(w, cont) + ", " + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + "]";
	}
}
